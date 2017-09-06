package com.example.bluey.digitalsextant;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by robinluna RobinLuna on 8/26/17.
 */

public class PreviousPositionPageFragment extends Fragment{


    /**
     * Default Constructor PreviousPositionPageFragment
     */
    public PreviousPositionPageFragment() {}

    /**
     * Inflate the layout for the PreviousPositionPageFragment and gets the view of the fragment
     * @param inflater Inflater
     * @param container ViewGroup
     * @param savedInstanceState Bundle
     * @return View
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.page_previous_position_list, container, false);
    }
}