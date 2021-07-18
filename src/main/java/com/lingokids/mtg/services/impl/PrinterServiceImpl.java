package com.lingokids.mtg.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.lingokids.mtg.services.PrinterService;

import java.io.File;
import java.io.IOException;
import java.util.Set;

/**
 * Service to print JSON and the fields that we want to see.
 *
 * Also a boolean parameter to pretty print the JSON so it will be easier to read by humans
 * If false compact form will be used. It will take less space on disk. Print will be made to stdout
 * so we can use Unix redirections, pipes, etc.
 *
 */
public class PrinterServiceImpl implements PrinterService {

    private ObjectMapper objectMapper;

    public PrinterServiceImpl() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public String print(Object input, Set<String> properties, String filename, boolean pretty) throws IOException {
        SimpleBeanPropertyFilter filter;
        if (properties != null && !properties.isEmpty()) {
            filter = SimpleBeanPropertyFilter.filterOutAllExcept(properties);
        } else {
            filter = SimpleBeanPropertyFilter.serializeAll();
        }

        FilterProvider filters = new SimpleFilterProvider().addFilter("customFilter", filter);
        ObjectWriter writer = objectMapper.writer(filters);

        if (pretty) {
            writer = writer.withDefaultPrettyPrinter();
        }

        String jsonString = writer.writeValueAsString(input);

        if (filename != null && !filename.trim().isEmpty()) {
            writer.writeValue(new File(filename), input);
        } else {
            System.out.println(jsonString);
        }

        return jsonString;
    }
}
