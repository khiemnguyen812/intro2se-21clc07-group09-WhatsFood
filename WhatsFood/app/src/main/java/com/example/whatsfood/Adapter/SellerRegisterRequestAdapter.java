package com.example.whatsfood.Adapter;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.whatsfood.Model.Seller;
import com.example.whatsfood.R;

import java.util.List;

public class SellerRegisterRequestAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<Pair<String, Seller>> sellers;

    public SellerRegisterRequestAdapter(Context context, int layout, List<Pair<String, Seller>> sellers) {
        this.context = context;
        this.layout = layout;
        this.sellers = sellers;
    }
    @Override
    public int getCount() {
        return sellers.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(layout,null);
        Seller seller = sellers.get(i).second;

        ((TextView)view.findViewById(R.id.store_name)).setText(seller.storeName);
        ((TextView)view.findViewById(R.id.username)).setText(seller.username);

        return view;
    }
}
