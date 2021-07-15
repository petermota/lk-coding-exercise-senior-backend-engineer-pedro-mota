package com.lingokids.mtg.services;

import java.io.IOException;

/**
 * Service to make HTTP requests
 *
 * Implementation can optimize and reuse connections, use HTTP/2, etc
 */
public interface HTTPService {
    String doGet(String url) throws IOException;
}
