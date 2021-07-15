package com.lingokids.mtg.services.impl;

import com.lingokids.mtg.model.Card;
import com.lingokids.mtg.services.FilterService;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This service takes a list of cards and a Map of filters (key, value)
 * and apply the filters.
 *
 * It has a very long switch statement. I tried to do it first with Reflection but the
 * code was too complicated and very hard to understand. So I switched to a easy to read
 * but very long switch statement.
 *
 * Filtering is done using Functional Programming: Java 8 streams + Lambdas.
 *
 */
public class FilterServiceImpl implements FilterService {

    private static final String TRUE = "true";
    private static final String FALSE = "false";

    @Override
    public List<Card> filter(List<Card> cards, Map<String, String> filters) {
        if (filters == null || filters.isEmpty()) {
            return cards;
        }

        System.err.println("Applying filters = " + filters + " to " + cards.size() + " cards.");

        List<Card> filteredCards = cards;
        for (Map.Entry<String, String> filter : filters.entrySet()) {
            String field = filter.getKey();
            Set<String> values = convertArrayToSet(filter.getValue());

            if (values != null && !values.isEmpty()) {
                filteredCards = filteredCards.stream()
                        .filter(card -> containsValue(card, field, values))
                        .collect(Collectors.toList());
            }
        }

        System.err.println(filteredCards.size() + " cards after filtering.");

        return filteredCards;
    }

    /**
     * Converts a String containing values separated by commas to a Set (no duplicates) and
     * in lowercase to ease comparison in next steps
     * <p>
     * Example "Red,BLUE" -> Set[red, blue]
     *
     * @param value Comma separated values
     * @return Set containing the values and without duplicates
     */
    private Set<String> convertArrayToSet(String value) {
        String[] values = value.split(",");
        return Arrays.stream(values)
                .filter(text -> text != null && text.length() > 0)
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
    }

    private boolean containsValue(Card card, String fieldName, Set<String> values) {
        boolean contains = false;

        switch (fieldName.toLowerCase()) {
            case "id":
                contains = values.contains(card.getId().toLowerCase());
                break;

            case "layout":
                contains = values.contains(card.getLayout().toLowerCase());
                break;

            case "name":
                contains = values.contains(card.getName().toLowerCase());
                break;

            case "names":
                contains = intersectionHasSameSize(card.getNames(), values);
                break;

            case "manacost":
                contains = values.contains(card.getManaCost().toLowerCase());
                break;

            case "cmc":
                contains = values.contains(Double.toString(card.getCmc()));
                break;

            case "colors":
                contains = intersectionHasSameSize(card.getColors(), values);
                break;

            case "coloridentity":
                contains = intersectionHasSameSize(card.getColorIdentity(), values);
                break;

            case "type":
                contains = values.contains(card.getType().toLowerCase());
                break;

            case "supertypes":
                contains = intersectionHasSameSize(card.getSupertypes(), values);
                break;

            case "types":
                contains = intersectionHasSameSize(card.getTypes(), values);
                break;

            case "subtypes":
                contains = intersectionHasSameSize(card.getSubtypes(), values);
                break;

            case "rarity":
                contains = values.contains(card.getRarity().toLowerCase());
                break;

            case "text":
                contains = values.contains(card.getText().toLowerCase());
                break;

            case "originaltext":
                contains = values.contains(card.getOriginalText().toLowerCase());
                break;

            case "originaltype":
                contains = values.contains(card.getOriginalType().toLowerCase());
                break;

            case "flavor":
                contains = values.contains(card.getFlavor().toLowerCase());
                break;

            case "artist":
                contains = values.contains(card.getArtist().toLowerCase());
                break;

            case "number":
                contains = values.contains(card.getNumber().toLowerCase());
                break;

            case "power":
                contains = values.contains(card.getPower().toLowerCase());
                break;

            case "toughness":
                contains = values.contains(card.getToughness().toLowerCase());
                break;

            case "loyalty":
                contains = values.contains(card.getLoyalty().toLowerCase());
                break;

            case "multiverseid":
                contains = values.contains(Integer.toString(card.getMultiverseid()));
                break;

            case "variations":
                contains = intersectionHasSameSize(card.getVariations(), values);
                break;

            case "imagename":
                contains = values.contains(card.getImageName().toLowerCase());
                break;

            case "watermark":
                contains = values.contains(card.getWatermark().toLowerCase());
                break;

            case "border":
                contains = values.contains(card.getBorder().toLowerCase());
                break;

            case "timeshifted":
                contains = values.contains(card.isTimeshifted() ? TRUE : FALSE);
                break;

            case "hand":
                contains = values.contains(Integer.toString(card.getHand()));
                break;

            case "life":
                contains = values.contains(Integer.toString(card.getLife()));
                break;

            case "reserved":
                contains = values.contains(card.isReserved() ? TRUE : FALSE);
                break;

            case "releasedate":
                contains = values.contains(card.getReleaseDate().toLowerCase());
                break;

            case "starter":
                contains = values.contains(card.isStarter() ? TRUE : FALSE);
                break;

            case "set":
                contains = values.contains(card.getSet().toLowerCase());
                break;

            case "setname":
                contains = values.contains(card.getSetName().toLowerCase());
                break;

            case "printings":
                contains = intersectionHasSameSize(card.getPrintings(), values);
                break;

            case "imageurl":
                contains = values.contains(card.getImageUrl().toLowerCase());
                break;

            case "pricehigh":
                contains = values.contains(card.getPriceHigh().toString());
                break;

            case "pricemid":
                contains = values.contains(card.getPriceMid().toString());
                break;

            case "pricelow":
                contains = values.contains(card.getPriceLow().toString());
                break;

            case "onlinepricehigh":
                contains = values.contains(card.getOnlinePriceHigh().toString());
                break;

            case "onlinepricemid":
                contains = values.contains(card.getOnlinePriceHigh().toString());
                break;

            case "onlinepricelow":
                contains = values.contains(card.getOnlinePriceHigh().toString());
                break;

            case "legalities":
            case "rulings":
            case "foreignnames":
                throw new IllegalArgumentException("Field '" + fieldName + "' not supported for filtering.");

            default:
                throw new IllegalArgumentException("Field '" + fieldName + "' does not exist for filtering.");
        }

        return contains;
    }

    private boolean intersectionHasSameSize(List<String> list, Set<String> values) {
        if (list == null || list.isEmpty()) {
            return false;
        }

        List<String> intersectionList = list.stream()
                .map(String::toLowerCase)
                .filter(values::contains)
                .collect(Collectors.toList());

        return !intersectionList.isEmpty() &&
                intersectionList.size() == values.size() &&
                list.size() == values.size();
    }
}
