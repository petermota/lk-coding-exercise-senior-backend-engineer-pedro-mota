package com.lingokids.mtg.services.impl;

import com.lingokids.mtg.services.HTTPService;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Just an easy implementation to make HTTP requests.
 *
 * No optimizations, no connection pooling, no connection reuse. Just simple timeouts so
 * the connection will not hang forever if there is no response from the server.
 *
 * It has a potential to make a lot of optimizations and make the reading of the cards
 * from the external API faster.
 *
 */
public class HTTPServiceImpl implements HTTPService {

    /**
     * Timeouts when making HTTP requests
     */
    private static long connectTimeout = 2;
    private static long writeTimeout = 2;
    private static long readTimeout = 5;

    /**
     * HTTP client
     */
    private OkHttpClient client;

    /**
     * Create a new HTTP Client to make requests with provided timeouts
     * so the connection will not hang forever if the server doesn't answer
     */
    public HTTPServiceImpl() {
        this.client = new OkHttpClient.Builder()
                .connectTimeout(connectTimeout, TimeUnit.SECONDS)
                .writeTimeout(writeTimeout, TimeUnit.SECONDS)
                .readTimeout(readTimeout, TimeUnit.SECONDS)
                .build();
    }

    @Override
    public String doGet(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return response.body().string();
            } else {
                throw new IOException("Error reading cards from server. Status code " + response.code());
            }
        }
    }
}
