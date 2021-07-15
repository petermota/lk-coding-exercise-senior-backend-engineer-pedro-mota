package com.lingokids.mtg.services.impl;

import com.lingokids.mtg.model.Card;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class StorageServiceImplTest {
    /**
     * Service to be tested
     */
    private final StorageServiceImpl storageService;

    public StorageServiceImplTest() {
        storageService = new StorageServiceImpl();
    }

    /**
     * Steps
     *
     * (1) Delete file, now dataexists should be false
     * (2) Create file, now dataexists should be true
     * (3) Read file, should have
     *
     * @throws IOException Because filesystem is accessed
     */
    @Test
    public void shouldWorkThisFlow() throws IOException {
        storageService.delete();
        assertFalse(storageService.dataExists());

        List<Card> cards = new ArrayList<>();
        Card c1 = new Card();
        c1.setId("1");
        c1.setSet("KTK");
        cards.add(c1);
        storageService.write(cards);
        assertTrue(storageService.dataExists());

        List<Card> cardsRead = storageService.read();
        assertNotNull(cardsRead);
        assertEquals(1, cardsRead.size());
        assertEquals("1", cardsRead.get(0).getId());
        assertEquals("KTK", cardsRead.get(0).getSet());

        storageService.delete();
        assertFalse(storageService.dataExists());
    }
}
