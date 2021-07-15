package com.lingokids.mtg.services.impl;

import com.lingokids.mtg.services.APIAssembler;
import com.lingokids.mtg.services.StorageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CardLoaderServiceImplTest {

    /**
     * Service to be tested
     */
    @InjectMocks
    private final CardLoaderServiceImpl cardLoaderService;

    /**
     * We will mock Storage Service and API assembler
     */
    @Mock
    private StorageService storageService;

    @Mock
    private APIAssembler apiAssembler;

    public CardLoaderServiceImplTest() {
        cardLoaderService = new CardLoaderServiceImpl(storageService, apiAssembler);
    }

    @Test
    public void shouldReloadCardsIfNoFile() throws IOException {
        when(storageService.dataExists()).thenReturn(false);

        cardLoaderService.getCards("", true);
        cardLoaderService.getCards("", false);

        verify(apiAssembler, times(2)).getCards(anyString());
        verify(storageService, times(2)).write(anyList());
        verify(storageService, times(0)).read();
    }

    @Test
    public void shouldUseCachedFileIfPresentAndRefreshIsFalse() throws IOException {
        when(storageService.dataExists()).thenReturn(true);

        cardLoaderService.getCards("", false);

        verify(apiAssembler, times(0)).getCards(anyString());
        verify(storageService, times(0)).write(anyList());
        verify(storageService, times(1)).read();
    }

    @Test
    public void shouldReloadIfCachedFileExistsButRefreshIsTrue() throws IOException {
        cardLoaderService.getCards("", true);

        verify(apiAssembler, times(1)).getCards(anyString());
        verify(storageService, times(1)).write(anyList());
        verify(storageService, times(0)).read();
    }
}
