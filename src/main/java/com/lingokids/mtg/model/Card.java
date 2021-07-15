package com.lingokids.mtg.model;

import com.fasterxml.jackson.annotation.JsonFilter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

/**
 * This is a model domain class representing a Card.
 *
 */
@Getter
@Setter
@EqualsAndHashCode
@JsonFilter("customFilter")
public class Card {
    private String id;
    private String layout;
    private String name;
    private List<String> names;
    private String manaCost;
    private double cmc;
    private List<String> colors;
    private List<String> colorIdentity;
    private String type;
    private List<String> supertypes;
    private List<String> types;
    private List<String> subtypes;
    private String rarity;
    private String text;
    private String originalText;
    private String originalType;
    private String flavor;
    private String artist;
    private String number;
    private String power;
    private String toughness;
    private String loyalty;
    private int multiverseid = -1;
    private List<String> variations;
    private String imageName;
    private String watermark;
    private String border;
    private boolean timeshifted;
    private int hand;
    private int life;
    private boolean reserved;
    private String releaseDate;
    private boolean starter;
    private String set;
    private String setName;
    private List<String> printings;
    private String imageUrl;
    private List<Legal> legalities;
    private BigDecimal priceHigh;
    private BigDecimal priceMid;
    private BigDecimal priceLow;
    private BigDecimal onlinePriceHigh;
    private BigDecimal onlinePriceMid;
    private BigDecimal onlinePriceLow;
    private List<Ruling> rulings;
    private List<ForeignName> foreignNames;
}
