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

    //region REQUEST CODES for onActivityResult
    //----------------------------------------------------------------------------------------------

    public static final int RESULT_CATEGORY_CHANGES_MADE = 1;

    //endregion
    //region INTENT EXTRAS
    //----------------------------------------------------------------------------------------------

    public static final String EXTRA_CATEGORY_SELECTED_TO_EDIT = "Intent > Extra > Name of Category";
    public static final String EXTRA_NEW_WTX_ADDED = "Intent: Extra: A new Walletx has been added";

    //endregion

} // Constants
