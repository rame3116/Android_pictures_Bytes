package com.example.pictures;


import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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


        Bitmap myImage = BitmapFactory.decodeFile(path);
        int width = 55;
        int height = 55;

        width = myImage.getWidth();
        height = myImage.getHeight();

        //int height = im.getHeight();
        Log.d(TAG, "onCreateView: height : " + height);

        //int width = im.getWidth();
        Log.d(TAG, "onCreateView: width : " + width);

        //ATTENTION : Il faut récupérer les 2 bits les moins significatifs de CHAQUE composante de couleur
        //=> lire

        Bitmap byteImage = BitmapFactory.decodeFile(path);
        byte[] result = new byte[(int) (0.5*width*height +1)];
        result[0] =(byte) (byteImage.getPixel(0,0) & 3);
        int i = 1;
        int j=1;
        for (int x = 1; x < width-1; x++){
            for (int y = 1 ; y< height-1; y++){
                if ((j % 4) ==0){ // à la 4eme itération
                    //je change de place le tableau
                    i++;
                    j=1;
                }
                else{
                    // décalage à gauche
                    result[i] = (byte) (result[i]<<2) ;
                }
                //Pixel actuel
                int pixel = byteImage.getPixel(x,y);
                int twoBits = pixel & 3; //Récupère les 2 derniers bits (3 = 00000011)

                result[i] = (byte) (result[i] | twoBits); //Mets les 2 bits à la fin
            }
        }



        String message = new String (result) ;

        TextView text =  v.findViewById(R.id.lsb);
        text.setText(message);

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
