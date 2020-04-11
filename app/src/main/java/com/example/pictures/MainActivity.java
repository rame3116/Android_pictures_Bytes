package com.example.pictures;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSION_REQUEST = 1;

    ArrayList<String> arrayList;
    GridView gridView;
    ArrayAdapter<String> adapter;
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
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, arrayList);
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

        Log.d("MainActivity", "WEEEESH ??? ");
        if (picCursor != null && picCursor.moveToFirst()) {
            int picLocation = picCursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            do {
                Log.d("MainActivity", "abusé");
                arrayList.add(picCursor.getString(picCursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)));
/*
                arrayList.add(picCursor.getString(picCursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)));

                arrayList.add(picCursor.getString(picCursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)));*/

                //arrayList.add(currentLocation);
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
}
