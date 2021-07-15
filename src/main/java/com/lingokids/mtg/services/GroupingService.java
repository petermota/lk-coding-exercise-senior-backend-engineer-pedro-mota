package com.lingokids.mtg.services;

import com.lingokids.mtg.model.Card;

import java.util.List;

/**
 * This is a model domain class representing a Service to group a list of Cards.
 *
 * Actual implementation can do it in memory, Hadoop/Spark cluster, etc
 *
 */
public interface GroupingService {
    Object groupBy(List<Card> cards, List<String> groupBy);
}
