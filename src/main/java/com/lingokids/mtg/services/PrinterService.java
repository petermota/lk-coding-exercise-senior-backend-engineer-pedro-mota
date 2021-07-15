package com.lingokids.mtg.services;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.Set;

/**
 * Service to print final result.
 *
 * Current implementation outputs to stdout but it could do it to email, a real printer, etc.
 *
 */
public interface PrinterService {
    String print(Object input, Set<String> properties, boolean pretty) throws JsonProcessingException;
}
