package com.lingokids.mtg.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lingokids.mtg.model.Card;
import com.lingokids.mtg.services.HTTPService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class APIAssemblerImplTest {
    /**
     * Service to be tested
     */
    @InjectMocks
    private final APIAssemblerImpl apiAssembler;

    /**
     * We will mock the http service
     */
    @Mock
    private HTTPService httpService;

    /**
     * Fake URL and page parameter
     */
    private final String url = "http://localhost/api";

    /**
     * Mocked results
     */
    private final String jsonResult;
    private final String emptyResult;
    private final String brokenResult;

    public APIAssemblerImplTest() throws IOException {
        apiAssembler = new APIAssemblerImpl(httpService);

        jsonResult = readFile("test/3cards.json");
        emptyResult = readFile("test/empty.json");
        brokenResult = readFile("test/broken.json");
    }

    @Test
    public void shouldReadThreeCards() throws IOException {
        // Prepare answers from fake server
        String page = "?page=";
        when(httpService.doGet(url + page + "1")).thenReturn(jsonResult);
        when(httpService.doGet(url + page + "2")).thenReturn(emptyResult);

        // Call service
        List<Card> cards = apiAssembler.getCards(url);

        // Check some values of the responses
        assertNotNull(cards);
        assertEquals(3, cards.size());
        assertEquals("1", cards.get(0).getId());
        assertEquals("Master the Way", cards.get(0).getName());
        assertEquals(2, cards.get(2).getColors().size());
        assertEquals("Uncommon", cards.get(2).getRarity());
    }

    @Test(expected = JsonProcessingException.class)
    public void shouldThrowExceptionOnBrokenJson() throws IOException {
        when(httpService.doGet(anyString())).thenReturn(brokenResult);

        apiAssembler.getCards(url);
    }

    @Test(expected = IOException.class)
    public void shouldThrowExceptionOnCommunicationException() throws IOException {
        when(httpService.doGet(anyString())).thenThrow(new IOException());

        apiAssembler.getCards(url);
    }

    private String readFile(String path) throws IOException {
        Path uri = Paths.get(path);
        return Files.lines(uri).collect(Collectors.joining("\n"));
    }
}
