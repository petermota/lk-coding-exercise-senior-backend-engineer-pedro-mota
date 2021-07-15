package com.lingokids.mtg.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * This is a model domain class representing a list of Cards.
 *
 * It is exactly the response from the API
 */
@Getter
@Setter
@EqualsAndHashCode
public class CardsResponse {
    List<Card> cards;
}
