package vn.mrlongg71.ps09103_assignment.view.book;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import vn.mrlongg71.ps09103_assignment.R;
import vn.mrlongg71.ps09103_assignment.adapter.GirdViewChooseImageAdpater;
import vn.mrlongg71.ps09103_assignment.model.objectclass.ChooseImages;

public class ChooseImageActivity extends AppCompatActivity {

    private List<ChooseImages> chooseImagesList;
    List<String> imagesCheckedList = new ArrayList<>();
    private GirdViewChooseImageAdpater girdViewChooseImageAdpater;
    private GridView gvImagesChooseLocal;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_image);
        toolbar = findViewById(R.id.toolbarChooseImages);
        setSupportActionBar(toolbar);
        gvImagesChooseLocal = findViewById(R.id.gvImagesLocal);

        String[] projection = {MediaStore.Images.Media.DATA};
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        try (Cursor cursor = getContentResolver().query(uri, projection, null, null, null)) {
            cursor.moveToFirst();
            chooseImagesList = new ArrayList<>();
            while (!cursor.isAfterLast()) {
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));

                chooseImagesList.add(new ChooseImages(path,false));
                cursor.moveToNext();
            }
        }
        girdViewChooseImageAdpater = new GirdViewChooseImageAdpater(getApplicationContext(), R.layout.custom_gv_choose_images, chooseImagesList);
        gvImagesChooseLocal.setAdapter(girdViewChooseImageAdpater);
        girdViewChooseImageAdpater.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.custom_check_done, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        imagesCheckedList = new ArrayList<>();
        if (item.getItemId() == R.id.menu_checkDone) {
            checkedImages();
        }

        return super.onOptionsItemSelected(item);
    }

    private void checkedImages() {
        for (ChooseImages value : chooseImagesList) {
            if (value.isChecked()) {
              imagesCheckedList.add(value.getPathImages());
            }
        }
        Intent intent = new Intent();
        intent.putStringArrayListExtra("listpath" , (ArrayList<String>) imagesCheckedList);
        setResult(RESULT_OK,intent);
        finish();
    }
}
