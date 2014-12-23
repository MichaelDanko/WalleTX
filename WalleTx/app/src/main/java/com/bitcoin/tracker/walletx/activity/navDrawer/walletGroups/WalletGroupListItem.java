package com.bitcoin.tracker.walletx.activity.navDrawer.walletGroups;

/**
 * Custom WalletGroup list item.
 */
public class WalletGroupListItem {

    private String name;
    private String defaultGroupLabel;

    public WalletGroupListItem(String name, String defaultGroupLabel) {
        super();
        this.name = name;
        this.defaultGroupLabel = defaultGroupLabel;
    }

    public String getName() {
        return this.name;
    }

    public String getIsDefault() {
        return this.defaultGroupLabel;
    }
}