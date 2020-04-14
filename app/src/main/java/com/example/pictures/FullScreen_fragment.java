package com.example.pictures;


import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

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
        String path = args.getString("uri");
        Uri uriData = Uri.parse(path);
        im.setImageURI(uriData);
        int height = im.getHeight();
        int width = im.getWidth();

        Bitmap byteImage = BitmapFactory.decodeFile(path);
        //Lire les 2 derniers pixels ?
        int lastPixel= byteImage.getPixel(width-1,height-1); //Un pixel = un octet
        int penultimatePixel = byteImage.getPixel(width-2,height-2);

        int Pixel = (penultimatePixel << 8) | lastPixel; //Décalage à gauche puis OU

        TextView text =  v.findViewById(R.id.lsb);

        text.setText(Pixel);

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

    /*Transformer un message en bits :
    String w = “William”;
    Byte[] b = w.getBytes();


    read (BitmapFactory.decode*(...) methods) and write (Bitmap.compress(...) method).
    //byteImage.setPixel();

    */

}
