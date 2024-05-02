package vn.mrlongg71.ps09103_assignment.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import vn.mrlongg71.ps09103_assignment.R;
import vn.mrlongg71.ps09103_assignment.model.objectclass.ChooseImages;

public class GirdViewChooseImageAdpater extends BaseAdapter {

    private Context context;
    private int resource;
    private List<ChooseImages> arrImagesLocal;
    private ChooseImages chooseImages;


    public GirdViewChooseImageAdpater(Context context, int resource, List<ChooseImages> arrImagesLocal) {
        this.context = context;
        this.resource = resource;
        this.arrImagesLocal = arrImagesLocal;
        chooseImages = new ChooseImages();
    }

    @Override
    public int getCount() {
        return arrImagesLocal.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(resource,parent, false);
        final ChooseImages chooseImages = arrImagesLocal.get(position);
        ImageView imageView = convertView.findViewById(R.id.imgGVLocal);
        TextView txtAmountImagesChooose = convertView.findViewById(R.id.txtAmountImagesChooose);
        CheckBox checkBoxImages  = convertView.findViewById(R.id.checkboxImages);
        Uri uri = Uri.parse(chooseImages.getPathImages());
        imageView.setImageURI(uri);
        checkBoxImages.setChecked(chooseImages.isChecked());

        checkBoxImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox checkBox = (CheckBox) v;
                chooseImages.setChecked(v.isSelected());
                arrImagesLocal.get(position).setChecked(checkBox.isChecked());

            }
        });

        return convertView;
    }
}
