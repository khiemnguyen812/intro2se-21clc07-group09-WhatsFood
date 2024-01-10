package com.example.whatsfood.Adapter;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.whatsfood.Model.Seller;
import com.example.whatsfood.Model.User;
import com.example.whatsfood.R;

import java.util.List;

public class ViewUserAdminAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<Pair<String, User>> users;

    public ViewUserAdminAdapter(Context context, int layout, List<Pair<String, User>> users) {
        this.context = context;
        this.layout = layout;
        this.users = users;
    }
    @Override
    public int getCount() {
        return users.size();
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
        User user = users.get(i).second;

        ((TextView)view.findViewById(R.id.username)).setText(user.username);

        return view;
    }
}
//Here