package com.bitcoin.tracker.walletx.model;

/**
 * Supported summary types
 *
 * Defines the type of information that Bitcoin Walletx will
 * summarize and aggregate for wallets and wallet groups.
 * Use this class to determine what sort of information needs to be
 * aggregated when communicating between fragments/activites.
 *
 */
public enum SupportedSummaryType {

    TRANSACTION_SUMMARY, SPENDING_BY_CATEGORY;

    public String toString() {
        switch(this) {
            case TRANSACTION_SUMMARY: return "Transaction summary";
            case SPENDING_BY_CATEGORY: return "Spending by category";
            default: throw new IllegalArgumentException();
        }
    }

}