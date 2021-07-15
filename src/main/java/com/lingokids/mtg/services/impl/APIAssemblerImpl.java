package com.lingokids.mtg.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lingokids.mtg.model.Card;
import com.lingokids.mtg.model.CardsResponse;
import com.lingokids.mtg.services.APIAssembler;
import com.lingokids.mtg.services.HTTPService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This is an orchestrator class. Makes multiple HTTP calls, parses and assembles
 * all the HTTP responses into one List of Card objects.
 *
 * It uses an HTTP service to make the real HTTP call and an Object Mapper to deserialize the
 * JSON response to a list of objects.
 *
 * If url parameter is null the standard url is used: https://api.magicthegathering.io/v1/cards
 *
 */
public class APIAssemblerImpl implements APIAssembler {

    /**
     * Base URL and page parameter
     *
     */
    private static final String BASE_URL = "https://api.magicthegathering.io/v1/cards";
    private static final String PAGE = "?page=%s";

    /**
     * Another dependecy that will make the real HTTP requests
     */
    private HTTPService httpService;

    /**
     * JSON serializer/deserializer
     */
    private ObjectMapper objectMapper;

    /**
     * Injection of dependencies through constructor
     *
     * @param httpService Dependency to make HTTP requests
     */
    public APIAssemblerImpl(HTTPService httpService) {
        this.httpService = httpService;
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public List<Card> getCards(String url) throws IOException {
        System.err.println("Retrieving cards from API " + getFinalURL(url));

        List<Card> completeList = new ArrayList<>();
        int page = 1;

        while (true) {
            List<Card> partialResult = makeRequest(url, page);

            if (partialResult == null || partialResult.isEmpty()) {
                break;
            } else {
                completeList.addAll(partialResult);
                if (page % 10 == 0) System.err.println(completeList.size() + " cards retrieved");
            }

            page++;
        }

        System.err.println(completeList.size() + " cards. Done!");
        return completeList;
    }

    private List<Card> makeRequest(String url, int page) throws IOException {
        String endpoint = getFinalURL(url);
        String completeURL = endpoint + String.format(PAGE, page);
        String result = httpService.doGet(completeURL);

        return parseResponse(result);
    }

    private String getFinalURL(String url) {
        return url == null ? BASE_URL : url;
    }

    private List<Card> parseResponse(String response) throws JsonProcessingException {
        CardsResponse cardsResponse = objectMapper.readValue(response, CardsResponse.class);
        return cardsResponse.getCards();
    }
}
