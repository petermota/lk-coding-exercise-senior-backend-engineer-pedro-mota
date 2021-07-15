package com.lingokids.mtg.services;

import com.lingokids.mtg.model.Card;

import java.io.IOException;
import java.util.List;

/**
 * This interface represents a way to load the cards.
 *
 * Actual implementation can read the cards from file, database, memory, external API, etc
 *
 */
public interface CardLoaderService {
    List<Card> getCards(String url, boolean refresh) throws IOException;
}
