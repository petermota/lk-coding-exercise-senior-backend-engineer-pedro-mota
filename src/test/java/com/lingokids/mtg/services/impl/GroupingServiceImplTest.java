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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class GroupingServiceImplTest {
    /**
     * Service to be tested
     */
    private GroupingServiceImpl groupingService;

    /**
     * Prepared list of cards
     */
    private final List<Card> cards;

    public GroupingServiceImplTest() throws IOException {
        groupingService = new GroupingServiceImpl();
        cards = readListOfCards();
    }

    @Test
    public void shouldReturnOriginalListIfNoGroupIsSpecified() {
        Object obj = groupingService.groupBy(cards, null);

        assertNotNull(obj);
        assertTrue(obj instanceof ArrayList);
        assertEquals(cards, (List<Card>) obj);
    }

    @Test
    public void shouldReturnMapWithOneGroup() {
        List<String> groupBy = new ArrayList<>();
        groupBy.add("set");

        Object obj = groupingService.groupBy(cards, groupBy);

        assertNotNull(obj);
        assertTrue(obj instanceof Map);

        Map<String, List<Card>> groupedCards = (Map<String, List<Card>>) obj;
        assertEquals(3, groupedCards.size());
        assertEquals(2, groupedCards.get("PTC").size());
        assertEquals(2, groupedCards.get("KTK").size());
        assertEquals(3, groupedCards.get("ZNR").size());
        assertEquals("1", groupedCards.get("PTC").get(0).getId());
        assertEquals("2", groupedCards.get("PTC").get(1).getId());
        assertEquals("3", groupedCards.get("KTK").get(0).getId());
        assertEquals("4", groupedCards.get("KTK").get(1).getId());
        assertEquals("5", groupedCards.get("ZNR").get(0).getId());
        assertEquals("6", groupedCards.get("ZNR").get(1).getId());
        assertEquals("7", groupedCards.get("ZNR").get(2).getId());
    }

    @Test
    public void shouldReturnMapOfMapWithTwoGroups() {
        List<String> groupBy = new ArrayList<>();
        groupBy.add("set");
        groupBy.add("rarity");

        Object obj = groupingService.groupBy(cards, groupBy);

        assertNotNull(obj);
        assertTrue(obj instanceof Map);

        Map<String, Map<String, List<Card>>> groupedCards = (Map<String, Map<String, List<Card>>>) obj;
        assertEquals(3, groupedCards.size());

        Map<String, List<Card>> ptcCards = groupedCards.get("PTC");
        assertNotNull(ptcCards);
        assertEquals(2, ptcCards.size());
        assertEquals(1, ptcCards.get("Common").size());
        assertEquals(1, ptcCards.get("Rare").size());

        Map<String, List<Card>> ktkCards = groupedCards.get("KTK");
        assertNotNull(ktkCards);
        assertEquals(2, ktkCards.size());
        assertEquals(1, ktkCards.get("Rare").size());
        assertEquals(1, ktkCards.get("Mythic").size());

        Map<String, List<Card>> znrCards = groupedCards.get("ZNR");
        assertNotNull(znrCards);
        assertEquals(2, znrCards.size());
        assertEquals(1, znrCards.get("Common").size());
        assertEquals(2, znrCards.get("Rare").size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfThreeGroups() {
        List<String> groupBy = new ArrayList<>();
        groupBy.add("set");
        groupBy.add("rarity");
        groupBy.add("name");

        groupingService.groupBy(cards, groupBy);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfGroupIsListOfString() {
        List<String> groupBy = new ArrayList<>();
        groupBy.add("colors");

        groupingService.groupBy(cards, groupBy);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfGroupIsANonExistentField() {
        List<String> groupBy = new ArrayList<>();
        groupBy.add("gender");

        groupingService.groupBy(cards, groupBy);
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
