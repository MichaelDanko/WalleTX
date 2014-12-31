package com.bitcoin.tracker.walletx.activity.walletGroups;

/**
 * Custom WalletGroup list item.
 */
public class WalletGroupListItem {

    private String name;
    private String defaultGroupLabel;

    public WalletGroupListItem(String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
