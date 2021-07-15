package com.lingokids.mtg.services;

import com.lingokids.mtg.model.Card;

import java.io.IOException;
import java.util.List;

/**
 * This interface represents a way to get the cards.
 *
 * Actual implementation can get the cards from File, Database, etc.
 *
 */
public interface APIAssembler {
    List<Card> getCards(String url) throws IOException;
}
