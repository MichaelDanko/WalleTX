package com.bitcoin.tracker.walletx.activity.testing;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bitcoin.tracker.walletx.R;
import com.bitcoin.tracker.walletx.activity.MainActivity;

/**
 * Temporary testing class
 */
public class TestingDankoFragment extends Fragment {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static TestingDankoFragment newInstance(int sectionNumber) {
        TestingDankoFragment fragment = new TestingDankoFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public TestingDankoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_testing_danko, container, false);

        /*
        // Crashes app
        try {
            Wallet = new BlockchainInfo();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Toast.makeText(this, (int) Wallet.jsonFinalBalance, Toast.LENGTH_LONG).show();
        */

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

}