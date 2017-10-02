package com.example.bluey.digitalsextant;


import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by robinluna Robin Luna on 8/06/17.
 */
@SuppressLint("ValidFragment")
public class ManualPageFragment extends Fragment {

    private View view;
    boolean iscalculateButtonPushed = false;

    /**
     * Default Constructor ManualPageFragment
     */
    public ManualPageFragment() {}


    /**
     * Inflate the layout for the ManualPageFragment and gets the view of the fragment
     *
     * @param inflater           Inflater
     * @param container          ViewGroup
     * @param savedInstanceState Bundle
     * @return View
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.page_manual, container, false);

        String fileLines = "";
        String fileLine = "";

        TextView instructionsTextView = view.findViewById(R.id.manualTextView);
        InputStream inputStream = this.getResources().openRawResource(R.raw.instructions);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        // 3.)
        try {
            while ((fileLine = bufferedReader.readLine()) != null) {
                fileLines = fileLines + fileLine + "\n";
            }

            instructionsTextView.setText(fileLines);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return view;
    }

}
