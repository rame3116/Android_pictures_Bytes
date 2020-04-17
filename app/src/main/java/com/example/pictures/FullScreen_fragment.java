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

import java.io.File;
import java.io.FileOutputStream;


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
    private int NB_CHAR_MAX = 64;
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

        //ATTENTION : Il faut récupérer les 2 bits les moins significatifs de CHAQUE composante de couleur

        Bitmap byteImage = BitmapFactory.decodeFile(path);
        byte[] result = new byte[64];
        byte waiting =0;

        for(int i = 0,j=0, x = 0, y =0 ; i < NB_CHAR_MAX; y++){ //64 caractères max pour l'instant

            if(y >= height){
                y=0;
                x++;
            }

            //Pixel actuel
            int pixel = byteImage.getPixel(x,y);
            int twoBits = pixel & 3; //Récupère les 2 derniers bits (3 = 00000011)

            if (j==0){
                waiting = (byte) (twoBits<<6);
                j++;
                continue;
            }
            if(j==1) {
                waiting = (byte) (waiting | (twoBits<<4));
                j++;
                continue;
            }
            if(j==2) {
                waiting = (byte) (waiting | (twoBits<<2));
                j++;
                continue;
            }
            if(j==3) {
                waiting = (byte) (waiting| twoBits);
                j=0;
                result[i]=waiting;
                waiting=0;
                i++;
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

      byte[] beforeTidy = text.getText().toString().getBytes();

      byte[] codeByte = new byte[beforeTidy.length*4];

      //Rangement 2 bits par case
      for(int x=0;x<beforeTidy.length;x++){
          codeByte[4*x]= (byte) ((beforeTidy[x]&192) >>6);
          codeByte[4*x+1]= (byte) ((beforeTidy[x]&48) >>4);
          codeByte[4*x+2]= (byte) ((beforeTidy[x]&12)>>2);
          codeByte[4*x+3]= (byte) (beforeTidy[x]&3);


      }
       byteImage = byteImage.copy( Bitmap.Config.ARGB_8888 , true);

        for(int i = 0, x = 0, y =0 ; i < NB_CHAR_MAX; i++, y++){
            if(y > height){
                y=0;
                x++;
            }

            int aux = (byteImage.getPixel(x,y) &252); //Met à 0 les 2 derniers bits
            int toSet =0;
            if(codeByte.length<=i){ //Si on a dépassé le nombre de caractères du message
                toSet = aux;
           }
            else {
                toSet = aux | codeByte[i];
            }
            byteImage.setPixel(x,y,toSet); //3eme argument : mon code
        }
        //FAIT LE LIEN ENTRE URI ET byteImage
        store(byteImage,path);
  }

  public void store(Bitmap bm, String filename){

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
