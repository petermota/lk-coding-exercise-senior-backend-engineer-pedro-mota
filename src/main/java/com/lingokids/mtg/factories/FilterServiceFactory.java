package com.lingokids.mtg.factories;

import com.lingokids.mtg.services.FilterService;
import com.lingokids.mtg.services.impl.FilterServiceImpl;

/**
 * Dependency injection through a Factory Pattern.
 * It creates a new instance every time is called.
 *
 */
public class FilterServiceFactory {
    /**
     * Nobody should create an instance of the factory, so the constructor private.
     */
    private FilterServiceFactory() {}

    public static FilterService getFilterServiceInstance() {
        return new FilterServiceImpl();
    }
}
