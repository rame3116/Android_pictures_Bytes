package com.example.pictures;


import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;


/**
 * A simple {@link Fragment} subclass.
 */
public class Gallery_Fragment extends Fragment {

    View m_view = null;

    public Gallery_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        m_view = inflater.inflate(R.layout.fragment_gallery_, container, false);

        TextView title = m_view.findViewById(R.id.textView_grid);
        ImageView im = m_view.findViewById(R.id.image_grid);

        Bundle args = getArguments();
        String name = args.getString("path", "");
        Uri uriData = Uri.fromFile(new File(args.getString("uri")));



        title.setText(name);
       Picasso.get().load(uriData).into(im);

        return m_view;
    }

    public View getM_view(){
        return this.m_view;
    }

}
