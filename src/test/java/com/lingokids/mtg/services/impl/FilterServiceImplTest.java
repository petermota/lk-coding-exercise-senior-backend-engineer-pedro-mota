package com.lingokids.mtg.services.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lingokids.mtg.model.Card;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class FilterServiceImplTest {
    /**
     * Service to be tested
     */
    private final FilterServiceImpl filterService;

    /**
     * Prepared list of cards
     */
    private final List<Card> cards;

    public FilterServiceImplTest() throws IOException {
        filterService = new FilterServiceImpl();
        cards = readListOfCards();
    }

    @Test
    public void shouldFilterBySet() {
        Map<String, String> filter = new HashMap<>();
        filter.put("set", "ptc");

        List<Card> filteredCards = filterService.filter(cards, filter);

        assertNotNull(filteredCards);
        assertEquals(2, filteredCards.size());
        assertEquals("1", filteredCards.get(0).getId());
        assertEquals("2", filteredCards.get(1).getId());

        filter = new HashMap<>();
        filter.put("set", "ktk");

        filteredCards = filterService.filter(cards, filter);

        assertNotNull(filteredCards);
        assertEquals(2, filteredCards.size());
        assertEquals("3", filteredCards.get(0).getId());
        assertEquals("4", filteredCards.get(1).getId());

        filter = new HashMap<>();
        filter.put("set", "znr");

        filteredCards = filterService.filter(cards, filter);

        assertNotNull(filteredCards);
        assertEquals(3, filteredCards.size());
        assertEquals("5", filteredCards.get(0).getId());
        assertEquals("6", filteredCards.get(1).getId());
        assertEquals("7", filteredCards.get(2).getId());
    }

    @Test
    public void shouldFilterByTwoSets() {
        Map<String, String> filter = new HashMap<>();
        filter.put("set", "ptc,ktk");

        List<Card> filteredCards = filterService.filter(cards, filter);

        assertNotNull(filteredCards);
        assertEquals(4, filteredCards.size());
        assertEquals("1", filteredCards.get(0).getId());
        assertEquals("2", filteredCards.get(1).getId());
        assertEquals("3", filteredCards.get(2).getId());
        assertEquals("4", filteredCards.get(3).getId());

        filter = new HashMap<>();
        filter.put("set", "ptc,znr");

        filteredCards = filterService.filter(cards, filter);

        assertNotNull(filteredCards);
        assertEquals(5, filteredCards.size());
        assertEquals("1", filteredCards.get(0).getId());
        assertEquals("2", filteredCards.get(1).getId());
        assertEquals("5", filteredCards.get(2).getId());
        assertEquals("6", filteredCards.get(3).getId());
        assertEquals("7", filteredCards.get(4).getId());

        filter = new HashMap<>();
        filter.put("set", "ktk,znr");

        filteredCards = filterService.filter(cards, filter);

        assertNotNull(filteredCards);
        assertEquals(5, filteredCards.size());
        assertEquals("3", filteredCards.get(0).getId());
        assertEquals("4", filteredCards.get(1).getId());
        assertEquals("5", filteredCards.get(2).getId());
        assertEquals("6", filteredCards.get(3).getId());
        assertEquals("7", filteredCards.get(4).getId());
    }

    @Test
    public void shouldFilterByThreeSets() {
        Map<String, String> filter = new HashMap<>();
        filter.put("set", "ptc,ktk,znr");

        List<Card> filteredCards = filterService.filter(cards, filter);

        assertNotNull(filteredCards);
        assertEquals(7, filteredCards.size());
        assertEquals("1", filteredCards.get(0).getId());
        assertEquals("2", filteredCards.get(1).getId());
        assertEquals("3", filteredCards.get(2).getId());
        assertEquals("4", filteredCards.get(3).getId());
        assertEquals("5", filteredCards.get(4).getId());
        assertEquals("6", filteredCards.get(5).getId());
        assertEquals("7", filteredCards.get(6).getId());
    }

    @Test
    public void shouldFilterNonExistentSet() {
        Map<String, String> filter = new HashMap<>();
        filter.put("set", "xyz");

        List<Card> filteredCards = filterService.filter(cards, filter);

        assertNotNull(filteredCards);
        assertEquals(0, filteredCards.size());
    }

    @Test
    public void shouldFilterByExistentAndNonExistentSet() {
        Map<String, String> filter = new HashMap<>();
        filter.put("set", "ktk,xyz");

        List<Card> filteredCards = filterService.filter(cards, filter);

        assertNotNull(filteredCards);
        assertEquals(2, filteredCards.size());
        assertEquals("3", filteredCards.get(0).getId());
        assertEquals("4", filteredCards.get(1).getId());
    }

    @Test
    public void shouldFilterByOneColor() {
        Map<String, String> filter = new HashMap<>();
        filter.put("colors", "red");

        List<Card> filteredCards = filterService.filter(cards, filter);

        assertNotNull(filteredCards);
        assertEquals(1, filteredCards.size());
        assertEquals("1", filteredCards.get(0).getId());

        filter = new HashMap<>();
        filter.put("colors", "white");

        filteredCards = filterService.filter(cards, filter);

        assertNotNull(filteredCards);
        assertEquals(1, filteredCards.size());
        assertEquals("5", filteredCards.get(0).getId());
    }

    @Test
    public void shouldFilterByTwoColors() {
        Map<String, String> filter = new HashMap<>();
        filter.put("colors", "red,blue");

        List<Card> filteredCards = filterService.filter(cards, filter);

        assertNotNull(filteredCards);
        assertEquals(3, filteredCards.size());
        assertEquals("2", filteredCards.get(0).getId());
        assertEquals("4", filteredCards.get(1).getId());
        assertEquals("6", filteredCards.get(2).getId());
    }

    @Test
    public void shouldFilterByThreeColors() {
        Map<String, String> filter = new HashMap<>();
        filter.put("colors", "red,blue,white");

        List<Card> filteredCards = filterService.filter(cards, filter);

        assertNotNull(filteredCards);
        assertEquals(1, filteredCards.size());
        assertEquals("7", filteredCards.get(0).getId());
    }

    @Test
    public void shouldFilterByNonExistentColors() {
        Map<String, String> filter = new HashMap<>();
        filter.put("colors", "silver");

        List<Card> filteredCards = filterService.filter(cards, filter);

        assertNotNull(filteredCards);
        assertEquals(0, filteredCards.size());

        filter = new HashMap<>();
        filter.put("colors", "blue,silver");

        filteredCards = filterService.filter(cards, filter);

        assertNotNull(filteredCards);
        assertEquals(0, filteredCards.size());
    }

    @Test
    public void shouldWorkWithMultipleFiltersFirst() {
        Map<String, String> filter = new HashMap<>();
        filter.put("set", "ptc");
        filter.put("colors", "red,blue");

        List<Card> filteredCards = filterService.filter(cards, filter);

        assertNotNull(filteredCards);
        assertEquals(1, filteredCards.size());
        assertEquals("2", filteredCards.get(0).getId());
    }

    @Test
    public void shouldWorkWithMultipleFiltersSecond() {
        Map<String, String> filter = new HashMap<>();
        filter.put("set", "znr");
        filter.put("colors", "red,blue,white");

        List<Card> filteredCards = filterService.filter(cards, filter);

        assertNotNull(filteredCards);
        assertEquals(1, filteredCards.size());
        assertEquals("7", filteredCards.get(0).getId());
    }

    @Test
    public void shouldFilterById() {
        Map<String, String> filter = new HashMap<>();
        filter.put("id", "5");

        List<Card> filteredCards = filterService.filter(cards, filter);

        assertNotNull(filteredCards);
        assertEquals(1, filteredCards.size());
        assertEquals("5", filteredCards.get(0).getId());

        filter = new HashMap<>();
        filter.put("id", "1,3,5,7");

        filteredCards = filterService.filter(cards, filter);

        assertNotNull(filteredCards);
        assertEquals(4, filteredCards.size());
        assertEquals("1", filteredCards.get(0).getId());
        assertEquals("3", filteredCards.get(1).getId());
        assertEquals("5", filteredCards.get(2).getId());
        assertEquals("7", filteredCards.get(3).getId());

        filter = new HashMap<>();
        filter.put("id", "25");

        filteredCards = filterService.filter(cards, filter);

        assertNotNull(filteredCards);
        assertEquals(0, filteredCards.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfFilterIsAComplexObject() {
        Map<String, String> filter = new HashMap<>();
        filter.put("legalities", "1");

        filterService.filter(cards, filter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfFilterIsANonExistentField() {
        Map<String, String> filter = new HashMap<>();
        filter.put("gender", "female");

        filterService.filter(cards, filter);
    }

    private List<Card> readListOfCards() throws IOException {
        String jsonContent = readFile();

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(jsonContent, new TypeReference<List<Card>>() {
        });
    }

    private String readFile() throws IOException {
        Path uri = Paths.get("test/filter-group.json");
        return Files.lines(uri).collect(Collectors.joining("\n"));
    }
}
