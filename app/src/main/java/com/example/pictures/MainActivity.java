package com.example.pictures;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.gridlayout.widget.GridLayout;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSION_REQUEST = 1;

    ArrayList<String> arrayList;
   // GridLayout gridView;
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
       // gridView =  findViewById(R.id.grid);
        arrayList = new ArrayList<>();
        getPictures();
    }

    public void getPictures() {
        ContentResolver contentResolver = getContentResolver();
        Uri picturesUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor picCursor = contentResolver.query(picturesUri, null, null, null, null);
        GridLayout gv = findViewById(R.id.grid);

        gv.setColumnCount(3);
        gv.setRowCount(500);

        if (picCursor != null && picCursor.moveToFirst()) {
            int picLocation = picCursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            do {

                String s = picCursor.getString(picLocation);


                FragmentManager fm = ((MainActivity) this).getSupportFragmentManager();
                Gallery_Fragment frag = new Gallery_Fragment();

                Bundle args = new Bundle();
                args.putString("path", s);
                args.putString("uri", s);
                frag.setArguments(args);

                fm.beginTransaction().add(gv.getId(), frag,"someTag1").commit();

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



    private class CustomAdapter extends BaseAdapter {

        ArrayList<String> images = null;
        Context context = null;

        public CustomAdapter(ArrayList<String> images, Context context){
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
//            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            View gridView;
//
//         //   if(convertView==null){
//
//                //gridView = new View(context);
//                // get layout from mobile.xml
//                gridView = inflater.inflate(R.layout.grid_element, null);
//
//                String s = images.get(position);
//
//                ImageView imageView =  gridView.findViewById(R.id.image_grid);
//                TextView text =  gridView.findViewById(R.id.textView);
//
//                text.setText(s);
//                imageView.setImageURI(Uri.fromFile(new File(s)));
//
//
////            } else {
////                gridView = (View) convertView;
////            }
//
//            return gridView;

//            ImageView iview;
//            if (convertView == null) {
//                iview = new ImageView(context);
//                iview.setLayoutParams(new GridView.LayoutParams(150,200));
//                iview.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                iview.setPadding(5, 5, 5, 5);
//            } else {
//                iview = (ImageView) convertView;
//            }
//            iview.setImageURI(Uri.fromFile(new File(images.get(position))));
//            return iview;
            return null;
        }
    }

}
