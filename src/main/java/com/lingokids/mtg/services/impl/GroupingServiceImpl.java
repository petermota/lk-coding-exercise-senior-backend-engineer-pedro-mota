package com.lingokids.mtg.services.impl;

import com.lingokids.mtg.model.Card;
import com.lingokids.mtg.services.GroupingService;

import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.groupingBy;

/**
 * This service groups cards by 1 or 2 fields. If no groupingBy field is provided
 * the original list is returned.
 *
 * If 1 field is provided it return a:
 *
 * "Map<String, List<Card>>"
 *
 * If 2 fields are provided then returns a:
 *
 * "Map<String, Map<String, List<Card>>>"
 *
 * Grouping is done using Functional Programming: Java 8 streams + Lambdas
 *
 */
public class GroupingServiceImpl implements GroupingService {

    @Override
    public Object groupBy(List<Card> cards, List<String> groupBy) {
        if (groupBy == null || groupBy.isEmpty()) {
            return cards;
        }

        if (groupBy.size() == 1) {
            return cards.stream().collect(groupingBy(getFunction(groupBy.get(0))));
        }

        if (groupBy.size() == 2) {
            return cards.stream().collect(groupingBy(getFunction(groupBy.get(0)), groupingBy(getFunction(groupBy.get(1)))));
        }

        throw new IllegalArgumentException("Grouping with more than 2 fields not implemented.");
    }

    private Function<Card, Object> getFunction(String fieldName) {
        switch (fieldName.toLowerCase()) {
            case "id":
                return Card::getId;

            case "layout":
                return Card::getLayout;

            case "name":
                return Card::getName;

            case "manacost":
                return Card::getManaCost;

            case "cmc":
                return Card::getCmc;

            case "type":
                return Card::getType;

            case "rarity":
                return Card::getRarity;

            case "text":
                return Card::getText;

            case "originaltext":
                return Card::getOriginalText;

            case "originaltype":
                return Card::getOriginalType;

            case "flavor":
                return Card::getFlavor;

            case "artist":
                return Card::getArtist;

            case "number":
                return Card::getNumber;

            case "power":
                return Card::getPower;

            case "toughness":
                return Card::getToughness;

            case "loyalty":
                return Card::getLoyalty;

            case "multiverseid":
                return Card::getMultiverseid;

            case "imagename":
                return Card::getImageName;

            case "watermark":
                return Card::getWatermark;

            case "border":
                return Card::getBorder;

            case "timeshifted":
                return Card::isTimeshifted;

            case "hand":
                return Card::getHand;

            case "life":
                return Card::getLife;

            case "reserved":
                return Card::isReserved;

            case "releasedate":
                return Card::getReleaseDate;

            case "starter":
                return Card::isStarter;

            case "set":
                return Card::getSet;

            case "setname":
                return Card::getSetName;

            case "imageurl":
                return Card::getImageUrl;

            case "pricehigh":
                return Card::getPriceHigh;

            case "pricemid":
                return Card::getPriceMid;

            case "pricelow":
                return Card::getPriceLow;

            case "onlinepricehigh":
                return Card::getOnlinePriceHigh;

            case "onlinepricemid":
                return Card::getOnlinePriceMid;

            case "onlinepricelow":
                return Card::getOnlinePriceLow;

            case "colors":
            case "coloridentity":
            case "names":
            case "supertypes":
            case "types":
            case "subtypes":
            case "legalities":
            case "printings":
            case "rulings":
            case "foreignnames":
            case "variations":
                throw new IllegalArgumentException("Field '" + fieldName + "' not supported for grouping.");

            default:
                throw new IllegalArgumentException("Field '" + fieldName + "' does not exist for grouping.");
        }

    }
}
