package com.bitcoin.tracker.walletx.activity;

public class Constants {

    /**
     * Fragment argument representing the section number for a fragment.
     * Used by the navigation drawer.
     */
    public static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Satoshis to BTC
     */
    public static final int SATOSHIS = 100000000;

    //region SHARED PREFERENCES
    //----------------------------------------------------------------------------------------------

    public static final String SHARED_PREFERENCES = "com.bitcoin.tracker.walletx";
    public static final String SP_EXCHANGE_RATE = "Shared Preferences > Exchange Rate";


    //endregion
    //region INTENT
    //----------------------------------------------------------------------------------------------

    public static final String SYNC_MANAGER_STATUS = "com.bitcoin.tracker.walletx.SYNC_MGR_STATUS";

    //endregion
    //region REQUEST CODES for onActivityResult
    //----------------------------------------------------------------------------------------------

    public static final int RESULT_WALLETX_FRAGMENT_NEW_WTX_ADDED = 1;
    public static final int RESULT_WALLETX_FRAGMENT_UPDATES_MADE = 2;

    public static final int RESULT_CATEGORY_CHANGES_MADE = 1;

    public static final int RESULT_GROUP_ADDED = 1;
    public static final int RESULT_GROUP_UPDATED = 2;

    //endregion
    //region INTENT EXTRAS
    //----------------------------------------------------------------------------------------------

    public static final String EXTRA_WTX_ADDED = "Intent > Extra > New Walletx added";
    public static final String EXTRA_WTX_SELECTED_TO_EDIT = "Intent > Extra > Name of Walletx";
    public static final String EXTRA_WTX_TO_SUMMARIZE = "Intent > Extra > Walletx to summarize";
    public static final String EXTRA_GROUP_TO_SUMMARIZE = "Intent > Extra > Group to summarize";
    public static final String EXTRA_CATEGORY_SELECTED_TO_EDIT = "Intent > Extra > Name of Category";
    public static final String EXTRA_GROUP_SELECTED_TO_EDIT = "Intent > Extra > Name of Group";
    public static final String EXTRA_SYNC_MGR_COMPLETE = "Intent > Extra > SyncManager task complete";

    //endregion

} // Constants
