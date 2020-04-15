package com.example.pictures;


import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FullScreen_fragment extends DialogFragment {

    public static String TAG = "FullScreenDialog";
    public View v;
    public TextView text;
    public Bitmap byteImage;
    public int width;
    public int height;
    public String path;
    private Button bouton;

    public FullScreen_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v =  inflater.inflate(R.layout.xml_full_screen_fragment, container, false);

        ImageView im = v.findViewById(R.id.imageView_dialog);
        bouton = v.findViewById(R.id.button);
        bouton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                coder();
            }
        });



        Bundle args = getArguments();
        path = args.getString("uri");
        Uri uriData = Uri.parse(path);
        im.setImageURI(uriData);

        byteImage = BitmapFactory.decodeFile(path);

        width = byteImage.getWidth();
        height = byteImage.getHeight();

        //int height = im.getHeight();
        Log.d(TAG, "onCreateView: height : " + height);

        //int width = im.getWidth();
        Log.d(TAG, "onCreateView: width : " + width);

        //ATTENTION : Il faut récupérer les 2 bits les moins significatifs de CHAQUE composante de couleur
        //=> lire

        Bitmap byteImage = BitmapFactory.decodeFile(path);
        byte[] result = new byte[(int) (8*8 +1)];
        result[0] =(byte) (byteImage.getPixel(0,0) & 3);
        int i = 1;
        int j=1;
       // for (int y = 1 ; y< height-1; y++){
         //   for (int x = 1; x < width-1; x++){
        for (int y = 1 ; y< 8; y++){
            for (int x = 1; x < 8; x++){
                if ((j % 4) ==0){ // à la 4eme itération
                    //je change de place le tableau
                    i++;
                    j=1;
                }
                else{
                    // décalage à gauche
                    result[i] = (byte) (result[i]<<2) ;
                    j++;
                }
                //Pixel actuel
                int pixel = byteImage.getPixel(x,y);
                int twoBits = pixel & 3; //Récupère les 2 derniers bits (3 = 00000011)

                result[i] = (byte) (result[i] | twoBits); //Mets les 2 bits à la fin
            }
        }


        String message = new String (result) ;

        text =  v.findViewById(R.id.lsb);
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

  public void coder() {
     // Toast.makeText(getContext(),"BEGIN !!!", Toast.LENGTH_SHORT).show();
        byte[] codeByteBack = text.getText().toString().getBytes();
        byte[] codeByte = wrap(codeByteBack); //message à stocker
        byteImage = byteImage.copy( Bitmap.Config.ARGB_8888 , true);

      //à chaque itération, récupérer les deux bits
        for(int i = 0, x = 1, y =1 ; i < codeByte.length; i++, x++){
            if(x > width){
                x=1;
                y++;
            }
            Log.d(TAG, "coder: x= "+x);
            Log.d(TAG, "coder: y= "+y);
            Log.d(TAG, "coder: byteImage width= "+width);
            Log.d(TAG, "coder: byteImage height= "+height);
            byteImage.setPixel(x-1,y-1,77); //3eme argument : mon code
        }
     // Toast.makeText(getContext(),"END !!!", Toast.LENGTH_SHORT).show();
        //FAIRE LE LIEN ENTRE URI ET byteImage
        store(byteImage,path);
//      File file = new File(path);
//      FileOutputStream fOut = null;
//      try {
//          fOut = new FileOutputStream(file);
//      } catch (FileNotFoundException e) {
//          e.printStackTrace();
//      }
//
//      byteImage.compress(Bitmap.CompressFormat.PNG, 100, fOut);
//      Log.d(TAG, "coder: Je suis là frère");
//      try {
//          fOut.flush();
//      } catch (IOException e) {
//          e.printStackTrace();
//      }
//      try {
//          fOut.close();
//      } catch (IOException e) {
//          e.printStackTrace();
//      }

  }
  public byte[] wrap(byte[] list){
        byte[] result = new byte[list.length];
        for (int i = 0; i<list.length;i++){
            result[i]= list[list.length - 1 -i];
        }
        return result;
  }

  public void store(Bitmap bm, String filename){

      String path=":/storage/sdcard0/DCIM/Camera/1414240995236.jpg";
      String dirpath = "";
      int i =0;
        for(String s : filename.split("/")){
            if(i==filename.split("/").length-1){
                break;
            }
            i++;
            dirpath += s+"/";
        }
        dirpath+="/";

        String name = filename.substring(filename.lastIndexOf("/")+1);
     // Toast.makeText(getContext(),dirpath, Toast.LENGTH_SHORT).show();
        File dir = new File(dirpath);
        if(!dir.exists()){
            dir.mkdirs();
        }
        File file = new File(dirpath,name);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG,100,fos);
            fos.flush();
            fos.close();
            Toast.makeText(getContext(),"SAVED !!!", Toast.LENGTH_LONG).show();
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getContext(),e.getMessage(), Toast.LENGTH_LONG).show();
        }
  }
}
