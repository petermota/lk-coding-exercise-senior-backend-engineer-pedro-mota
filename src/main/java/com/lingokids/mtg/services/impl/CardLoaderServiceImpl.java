package com.lingokids.mtg.services.impl;

import com.lingokids.mtg.model.Card;
import com.lingokids.mtg.services.APIAssembler;
import com.lingokids.mtg.services.CardLoaderService;
import com.lingokids.mtg.services.StorageService;

import java.io.IOException;
import java.util.List;

/**
 *
 * The idea behind this service is to load all the cards from local filesystem to improve
 * performance. Right now the API returns a maximum of 100 cards per page, and there are
 * 582 pages (58169 cards). So it takes a few minutes to load all the cards from the server.
 *
 * So if the service doesn't find in $HOME the file mtg_cards.json or the refresh parameter
 * is true it will force a complete download of all the cards (582 http requests). Then it
 * will save the response to the file and future queries will be solved faster.
 *
 * A url parameter is provided if the client wants to download the cards from a different url
 * but the response structure should be the same.
 *
 */
public class CardLoaderServiceImpl implements CardLoaderService {

    /**
     * External service to chech/read/write/delete the list of cards.
     *
     */
    private StorageService storageService;

    /**
     * This is an external helper service to assemble all the HTTP API calls into one.
     * It's an interface so a Factory class will inject this dependency.
     *
     */
    private APIAssembler apiAssembler;

    /**
     * Injection of dependencies through constructor
     *
     * @param apiAssembler Dependency to make all the http request and assemble a response
     */
    public CardLoaderServiceImpl(StorageService storageService, APIAssembler apiAssembler) {
        this.storageService = storageService;
        this.apiAssembler = apiAssembler;
    }

    @Override
    public List<Card> getCards(String url, boolean refresh) throws IOException {
        List<Card> cards;

        if (refresh || !storageService.dataExists()) {
            cards = apiAssembler.getCards(url);
            storageService.write(cards);
        } else {
            cards = storageService.read();
        }

        return cards;
    }
}
