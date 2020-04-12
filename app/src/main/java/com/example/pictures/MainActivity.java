package com.example.pictures;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSION_REQUEST = 1;

    ArrayList<Uri> arrayList;
    GridView gridView;
    CustomAdapter adapter;
    //MediaPlayer mediaPlayer;
    //Uri uriPicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
            }
        } else {
            displayPictures();
        }
    }

    public void displayPictures(){
        gridView = (GridView) findViewById(R.id.Hagrid);
        arrayList = new ArrayList<>();
        getPictures();
        adapter = new CustomAdapter(arrayList,this);
        gridView.setAdapter(adapter);

//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            /*public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
//                /*Intent intent = new Intent(this, HelloService.class);
//                startService(intent);
//                if(mediaPlayer != null) {
//
//                    mediaPlayer.release();
//                }*/
//
//                String[] lines = arrayList.get(position).split(System.getProperty("line.separator"));
//                //uriPicked = Uri.parse(lines[2]);
//
//
//                Intent intent = new Intent(view.getContext(),Listening.class);
//                intent.putExtra("uriPicked",lines[2]);
//                startActivity(intent);
//                /*
//                mediaPlayer = MediaPlayer.create(MainActivity.this,Uri.parse(lines[2]));
//                mediaPlayer.start();
//
//                 */
//
//
//
//        });

    }

    public void getPictures() {
        ContentResolver contentResolver = getContentResolver();
        Uri picturesUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        Cursor picCursor = contentResolver.query(picturesUri, null, null, null, null);

        if (picCursor != null && picCursor.moveToFirst()) {
            int picLocation = picCursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            do {

                String s = picCursor.getString(picCursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                Uri uri = Uri.fromFile(new File(s));
                //Toast.makeText(this,s,Toast.LENGTH_SHORT).show();

           

//                ImageView im = findViewById(R.id.imageView);
//                Picasso.get().load(uri).into(im);
                arrayList.add(uri);

            } while (picCursor.moveToNext());

        }
        else{
            Log.d("MainActivity", "Envie de rejoindre papa Johnny");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions,  int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();

                        displayPictures();
                    }
                } else {
                    Toast.makeText(this, "No permission granted !", Toast.LENGTH_SHORT).show();
                    finish();
                }
                return;
            }
        }
    }




    private class CustomAdapter extends BaseAdapter{

        ArrayList<Uri> images = null;
        Context context = null;

        public CustomAdapter(ArrayList<Uri> images, Context context){
            this.images = images;
            this.context = context;
        }


        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View gridView;

            if(convertView==null){

                gridView = new View(context);
                // get layout from mobile.xml
                gridView = inflater.inflate(R.layout.grid_element, null);

                // set value into textview
//                TextView textView = (TextView) gridView
//                        .findViewById(R.id.grid_item_label);
//                textView.setText(mobileValues[position]);

                // set image based on selected text
                ImageView imageView = (ImageView) gridView.findViewById(R.id.image_grid);

                Uri uri = images.get(position);

                Picasso.get().load(uri).into(imageView);
               // imageView.setImageResource(R.drawable.ic_launcher_background);
                //imageView.setImageURI(uri);
//                if (mobile.equals("Windows")) {
//                    imageView.setImageResource(R.drawable.windows_logo);
//                } else if (mobile.equals("iOS")) {
//                    imageView.setImageResource(R.drawable.ios_logo);
//                } else if (mobile.equals("Blackberry")) {
//                    imageView.setImageResource(R.drawable.blackberry_logo);
//                } else {
//                    imageView.setImageResource(R.drawable.android_logo);
//                }

            } else {
                gridView = (View) convertView;
            }

            return gridView;
        }
    }
}
