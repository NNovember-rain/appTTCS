package vn.mrlongg71.ps09103_assignment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import vn.mrlongg71.ps09103_assignment.R;
import vn.mrlongg71.ps09103_assignment.model.objectclass.TypeBook;

public class SpinerAdapter extends BaseAdapter {
    private Context context;
    private int resource;
    private List<TypeBook> typeBookList;

    public SpinerAdapter(Context context, int resource, List<TypeBook> typeBookList) {
        this.context = context;
        this.resource = resource;
        this.typeBookList = typeBookList;
    }

    @Override
    public int getCount() {
        return typeBookList.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        convertView = layoutInflater.inflate(resource, parent, false);
        TextView txtSpTypeName  = convertView.findViewById(R.id.txtSpTypeName);
        txtSpTypeName.setText(typeBookList.get(position).getTypename());
        return convertView;
    }
}
