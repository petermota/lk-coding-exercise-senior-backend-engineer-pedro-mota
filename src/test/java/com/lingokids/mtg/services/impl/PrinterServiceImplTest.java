package com.lingokids.mtg.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lingokids.mtg.model.Card;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class PrinterServiceImplTest {
    /**
     * Service to be tested
     */
    private final PrinterServiceImpl printerService;

    private final ObjectMapper objectMapper;

    public PrinterServiceImplTest() {
        printerService = new PrinterServiceImpl();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void shouldPrintListOfCards() throws IOException {
        List<Card> cards = createNewListOfCards();

        String result = printerService.print(cards, null, null,true);

        List<Card> printedCards = objectMapper.readValue(result, new TypeReference<List<Card>>() {
        });

        assertNotNull(printedCards);
        assertEquals(1, printedCards.size());
        assertEquals("1", printedCards.get(0).getId());
        assertEquals("KTK", printedCards.get(0).getSet());
        assertNull(printedCards.get(0).getRarity());
    }

    @Test
    public void shouldPrintListOfCardsOnlyWithSelectedFields() throws IOException {
        List<Card> cards = createNewListOfCards();
        Set<String> fieldsToBePrinted = new HashSet<>();
        fieldsToBePrinted.add("id");

        String result = printerService.print(cards, fieldsToBePrinted, null,true);

        List<Card> printedCards = objectMapper.readValue(result, new TypeReference<List<Card>>() {
        });

        assertNotNull(printedCards);
        assertEquals(1,printedCards.size());
        assertEquals("1", printedCards.get(0).getId());
        assertNull(printedCards.get(0).getSet());
        assertNull(printedCards.get(0).getRarity());
    }

    @Test
    public void shouldSaveToFile() throws IOException {
        List<Card> cards = createNewListOfCards();

        printerService.print(cards, null, "test.json",true);

        List<Card> cardsInFile = objectMapper.readValue(new File("test.json"), new TypeReference<List<Card>>() {
        });

        assertNotNull(cardsInFile);
        assertEquals(1,cardsInFile.size());
        assertEquals("1", cardsInFile.get(0).getId());
        assertEquals("KTK", cardsInFile.get(0).getSet());
        assertNull(cardsInFile.get(0).getRarity());
    }

    private List<Card> createNewListOfCards() {
        List<Card> cards = new ArrayList<>();
        Card c1 = new Card();
        c1.setId("1");
        c1.setSet("KTK");
        cards.add(c1);

        return cards;
    }
}
