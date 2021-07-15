package com.lingokids.mtg.services;

import com.lingokids.mtg.model.Card;

import java.util.List;
import java.util.Map;

/**
 * This is a model domain class representing a Service to filter List of Cards
 *
 * Actual implementation can do it in memory or use an Apache Spark cluster, or any
 * other suitable technology.
 *
 */
public interface FilterService {
    List<Card> filter(List<Card> cards, Map<String, String> filters);
}
