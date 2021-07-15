package com.lingokids.mtg.services;

import com.lingokids.mtg.model.Card;

import java.io.IOException;
import java.util.List;

/**
 * Service to check, save, read and delete the list of Cards. It's an optimization
 * so the list of cards doesn't have to be retrieved from the API server every
 * time (it takes several minutes).
 *
 * Current implementation does it on local filesystem but it could do it to a DB,
 * AWS S3, etc
 */
public interface StorageService {
    boolean dataExists();
    List<Card> read() throws IOException;
    void write(List<Card> cardList) throws IOException;
    void delete() throws IOException;
}
