package com.bitcoin.tracker.walletx.model.wallet;

/**
 * WalletxBlockchainInterface
 *
 * Each supported wallet type will share various operations.
 * For example, fetching all transactions associated with a specific Walletx.
 * Each supported wallet type should implement WalletxBlockchainInterface in order to
 * ensure uniformity.
 *
 */
public interface WalletxBlockchainInterface {

    // TODO Define what functions are required regardless of wallet type.

} // WalletxBlockchainInterface
