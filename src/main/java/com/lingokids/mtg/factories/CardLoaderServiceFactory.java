package com.lingokids.mtg.factories;

import com.lingokids.mtg.services.APIAssembler;
import com.lingokids.mtg.services.CardLoaderService;
import com.lingokids.mtg.services.HTTPService;
import com.lingokids.mtg.services.StorageService;
import com.lingokids.mtg.services.impl.APIAssemblerImpl;
import com.lingokids.mtg.services.impl.CardLoaderServiceImpl;
import com.lingokids.mtg.services.impl.HTTPServiceImpl;
import com.lingokids.mtg.services.impl.StorageServiceImpl;

/**
 * Dependency injection through a Factory Pattern.
 * It creates a new instance every time is called.
 *
 */
public class CardLoaderServiceFactory {
    /**
     * Nobody should create an instance of the factory, so the constructor private.
     */
    private CardLoaderServiceFactory() {}

    public static CardLoaderService getCardLoaderServiceInstance() {
        HTTPService httpService = new HTTPServiceImpl();
        APIAssembler apiAssembler = new APIAssemblerImpl(httpService);
        StorageService storageService = new StorageServiceImpl();
        return new CardLoaderServiceImpl(storageService, apiAssembler);
    }
}
