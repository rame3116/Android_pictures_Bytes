package com.example.pictures;


import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class FullScreen_fragment extends DialogFragment {

    public static String TAG = "FullScreenDialog";

    public FullScreen_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.xml_full_screen_fragment, container, false);

        ImageView im = v.findViewById(R.id.imageView_dialog);
        Bundle args = getArguments();
        Uri uriData = Uri.parse(args.getString("uri"));
        im.setImageURI(uriData);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

}
