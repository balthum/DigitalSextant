package com.example.bluey.digitalsextant;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by robinluna Robin Luna on 8/06/17.
 */
public class ManualPageFragment extends Fragment
{

    /**
     * Default Constructor ManualPageFragment
     */
    public ManualPageFragment() {}

    /**
     * Inflate the layout for the ManualPageFragment and gets the view of the fragment
     * @param inflater Inflater
     * @param container ViewGroup
     * @param savedInstanceState Bundle
     * @return View
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container,
                             Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.page_manual, container, false);
    }
}
