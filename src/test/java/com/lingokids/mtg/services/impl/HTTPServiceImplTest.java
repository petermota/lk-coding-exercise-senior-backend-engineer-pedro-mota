package com.lingokids.mtg.services.impl;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class HTTPServiceImplTest {

    /**
     * Service to be tested
     */
    private final HTTPServiceImpl httpService;

    private final String emptyResult;

    /**
     * Web server for testing and url
     */
    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8089);

    private static final String url = "http://localhost:8089/api";

    public HTTPServiceImplTest() throws IOException {
        httpService = new HTTPServiceImpl();
        emptyResult = readFile();
    }

    @Test
    public void shouldAnswerToHttpRequests() throws IOException {
        stubFor(get("/api")
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(emptyResult)));

        String response = httpService.doGet(url);
        assertEquals(response, emptyResult);
    }

    @Test(expected = IOException.class)
    public void shouldThrowExceptionOnNotFound() throws IOException {
        stubFor(get("/api")
                .willReturn(aResponse()
                        .withStatus(404)));
        httpService.doGet(url);
    }

    @Test(expected = IOException.class)
    public void shouldThrowExceptionOnInternalError() throws IOException {
        stubFor(get("/api")
                .willReturn(aResponse()
                        .withStatus(500)));
        httpService.doGet(url);
    }

    @Test(expected = SocketTimeoutException.class)
    public void shouldThrowExceptionOnReadTimeout() throws IOException {
        stubFor(get("/api")
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(emptyResult)
                        .withFixedDelay(6000)));
        httpService.doGet(url);
    }

    private String readFile() throws IOException {
        Path uri = Paths.get("test/empty.json");
        return Files.lines(uri).collect(Collectors.joining("\n"));
    }
}
