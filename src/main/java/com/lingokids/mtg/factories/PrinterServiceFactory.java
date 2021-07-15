package com.lingokids.mtg.factories;

import com.lingokids.mtg.services.PrinterService;
import com.lingokids.mtg.services.impl.PrinterServiceImpl;

/**
 * Dependency injection through a Factory Pattern.
 * It creates a new instance every time is called.
 *
 */
public class PrinterServiceFactory {
    /**
     * Nobody should create an instance of the factory, so the constructor private.
     */
    private PrinterServiceFactory() {}

    public static PrinterService getPrinterServiceInstance() {
        return new PrinterServiceImpl();
    }
}
