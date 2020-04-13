package com.example.pictures;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSION_REQUEST = 1;

    ArrayList<String> arrayList;
    CustomAdapter adapter;
    GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
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
        gridView =  findViewById(R.id.gridv);

        arrayList = new ArrayList<>();
        getPictures();
        adapter = new CustomAdapter(arrayList,this);
        gridView.setAdapter(adapter);

    }

    public void getPictures() {
        ContentResolver contentResolver = getContentResolver();
        Uri picturesUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor picCursor = contentResolver.query(picturesUri, null, null, null, null);
       // GridLayout gv = findViewById(R.id.grid);
//
//        gv.setColumnCount(3);
//        gv.setRowCount(500);

        if (picCursor != null && picCursor.moveToFirst()) {
            int picLocation = picCursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            do {

                String s = picCursor.getString(picLocation);

                arrayList.add(s);

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
           // Toast.makeText(context, String.valueOf(images.size()),Toast.LENGTH_SHORT).show();

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
                gridView = inflater.inflate(R.layout.grid_element, null);

            } else {
                gridView = (View) convertView;
           }
            final String s = images.get(position);

            ImageView imageView =  gridView.findViewById(R.id.imageView);
            //TextView text =  gridView.findViewById(R.id.textView);

            //text.setText(s);
            Picasso.get().load(Uri.fromFile(new File(s))).resize(600,600).into(imageView);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fm = getSupportFragmentManager();
                    FullScreen_fragment editNameDialogFragment = new FullScreen_fragment();
                    editNameDialogFragment.show(fm, FullScreen_fragment.TAG);
                    Bundle b = new Bundle();
                    b.putString("uri", s);
                    editNameDialogFragment.setArguments(b);
                }
            });
           // imageView.setImageURI(Uri.fromFile(new File(s)));

            return gridView;

        }
    }

}
