package com.example.whatsfood.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.whatsfood.Model.CartDetail;
import com.example.whatsfood.Model.Order;
import com.example.whatsfood.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class OrderAdapterForSeller extends BaseAdapter {

    private final Context context;
    private final int layout;
    private final List<Order> orderList;

    public OrderAdapterForSeller(Context context, int layout, List<Order> orderList) {
        this.context = context;
        this.layout = layout;
        this.orderList = orderList;
    }

    @Override
    public int getCount() {
        return orderList.size();
    }

    @Override
    public Object getItem(int i) {
        return orderList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    private class ViewHolder {
        TextView orderId, buyerName, address, price, status, foodNameList;
    }
    public void updateOrderList(List<Order> newOrderList) {
        orderList.clear();
        orderList.addAll(newOrderList);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout, null);

            viewHolder.orderId = (TextView) view.findViewById(R.id.result);
            viewHolder.address = (TextView) view.findViewById(R.id.address);
            viewHolder.buyerName = (TextView) view.findViewById(R.id.buyersName);
            viewHolder.price = (TextView) view.findViewById(R.id.priceAmount);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        Order order = orderList.get(i);
        viewHolder.address.setText(order.getShip_to());
        viewHolder.buyerName.setText(order.getBuyerName());
        viewHolder.orderId.setText(order.getOrderId());
        viewHolder.price.setText(order.getTotalMoney() + "VNĐ");

        return view;
    }
}
