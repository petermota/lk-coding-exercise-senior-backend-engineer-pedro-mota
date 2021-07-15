package com.lingokids.mtg.services.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.lingokids.mtg.model.Card;
import com.lingokids.mtg.services.StorageService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * This implementation of the Storage service check/read/write/delete on the local filesystem
 *
 * It uses a file named "mtg_cards.json" located in $HOME directory.
 *
 * This improves querying a lot: from several minutes downloading all the cards from the API to a few
 * seconds reading the list of cards from the local file.
 *
 */
public class StorageServiceImpl implements StorageService {

    /**
     * Constant with the name of file containing 58169 cards
     */
    private static final String FILENAME = "mtg_cards.json";

    /**
     * JSON serializer/deserializer
     */
    private final ObjectMapper objectMapper;
    private final ObjectWriter objectWriter;

    public StorageServiceImpl() {
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.serializeAll();
        FilterProvider filters = new SimpleFilterProvider().addFilter("customFilter", filter);
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectWriter = objectMapper.writer(filters);
    }

    /**
     * Method to check if file exists in $HOME directory
     *
     * @return True if file exists
     */
    @Override
    public boolean dataExists() {
        String absolutePath = getAbsolutePath();
        return new File(absolutePath).exists();
    }

    /**
     * Reads the data from File
     *
     * @return The list of cards read from external storage
     * @throws IOException
     */
    @Override
    public List<Card> read() throws IOException {
        String path = getAbsolutePath();
        System.err.println("Reading cards from file " + path);
        return objectMapper.readValue(new File(path), new TypeReference<List<Card>>() {
        });
    }

    /**
     * Method to persist the list of cards on the filesystem.
     *
     * @param cardList
     * @throws IOException
     */
    @Override
    public void write(List<Card> cardList) throws IOException {
        String path = getAbsolutePath();
        objectWriter.writeValue(new File(path), cardList);
    }

    /**
     * Deletes cache file
     *
     * @throws IOException
     */
    @Override
    public void delete() throws IOException {
        Path path = Paths.get(getAbsolutePath());
        Files.deleteIfExists(path);
    }

    private String getAbsolutePath() {
        return System.getProperty("user.home") + File.separator + FILENAME;
    }
}
