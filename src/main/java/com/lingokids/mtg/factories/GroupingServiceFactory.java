package com.lingokids.mtg.factories;

import com.lingokids.mtg.services.GroupingService;
import com.lingokids.mtg.services.impl.GroupingServiceImpl;

/**
 * Dependency injection through a Factory Pattern.
 * It creates a new instance every time is called.
 *
 */
public class GroupingServiceFactory {

    /**
     * Nobody should create an instance of the factory, so the constructor private.
     */
    private GroupingServiceFactory() {}

    public static GroupingService getGroupingServiceInstance() {
        return new GroupingServiceImpl();
    }
}
