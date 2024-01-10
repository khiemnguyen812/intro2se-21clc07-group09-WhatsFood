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

public class OrderAdapterForBuyer extends BaseAdapter {

    private final Context context;
    private final int layout;
    private final List<Order> orderList;

    public OrderAdapterForBuyer(Context context, int layout, List<Order> orderList) {
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
            if (layout == R.layout.order_placeholder_seller) {
                viewHolder.orderId = (TextView) view.findViewById(R.id.result);
                viewHolder.address = (TextView) view.findViewById(R.id.address);
                viewHolder.buyerName = (TextView) view.findViewById(R.id.buyersName);
                viewHolder.price = (TextView) view.findViewById(R.id.priceAmount);
            } else if (layout == R.layout.order_placeholder_buyer) {
                viewHolder.status = (TextView) view.findViewById(R.id.status_text_view_order_placeholder_buyer);
                viewHolder.price = (TextView) view.findViewById(R.id.total_price_order_placeholder_buyer);
                viewHolder.orderId = (TextView) view.findViewById(R.id.order_id_text_view_order_placeholder_buyer);
                viewHolder.foodNameList = (TextView) view.findViewById(R.id.food_name_list_order_placeholder_buyer);
            }

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        Order order = orderList.get(i);
        if (layout == R.layout.order_placeholder_seller) {
            viewHolder.address.setText(order.getShip_to());
            viewHolder.buyerName.setText(order.getBuyerName());
            viewHolder.orderId.setText(order.getOrderId());
            viewHolder.price.setText("" + order.getTotalMoney() + "VNĐ");
        } else if (layout == R.layout.order_placeholder_buyer) {
            viewHolder.orderId.setText(order.getOrderId());
            viewHolder.price.setText("" + order.getTotalMoney() + "VNĐ");
            viewHolder.status.setText(order.getStatus());
            ArrayList<CartDetail> foodList = order.getFoodList();
            String foodNameList = "Food List: ";
            for (int j = 0; j < foodList.size() - 1; j++) {
                foodNameList += foodList.get(j).getName() + ", ";
            }
            if (foodList.size() >= 1) {
                foodNameList += foodList.get(foodList.size() - 1).getName() + ". ";
            }
            viewHolder.foodNameList.setText(foodNameList);
        }
        if (layout == R.layout.order_placeholder_seller) {
            return view;
        }
        viewHolder.status.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                if(order.getStatus().equals("Denied")) {
                    showPopupDenied();
                }
                if(order.getStatus().equals("Waiting")){
                    showPopupWaiting();
                }
            }
            private void showPopupWaiting() {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View popupView = LayoutInflater.from(context).inflate(R.layout.popup_cancel_order, null);
                builder.setView(popupView);
                AlertDialog alertDialog = builder.create();

                Button yes= (Button) popupView.findViewById(R.id.yes);
                Button no = (Button) popupView.findViewById(R.id.no);

                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Order");
                        mDatabase.child(order.getOrderId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // Xóa thành công
                                        Toast.makeText(context, "Đơn hàng đã được xóa thành công", Toast.LENGTH_SHORT).show();
                                        List<Order> updatedOrderList = new ArrayList<>(orderList);
                                        updatedOrderList.remove(order);
                                        updateOrderList(updatedOrderList);
                                        alertDialog.dismiss();
                                        // Cập nhật giao diện hoặc thông báo tùy theo yêu cầu của bạn
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Xóa thất bại
                                        // Cập nhật giao diện hoặc thông báo tùy theo yêu cầu của bạn
                                    }
                                });
                    }
                });
                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Đóng popup khi nút "Cancel" được nhấn
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();

            }
            private void showPopupDenied() {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View popupView = LayoutInflater.from(context).inflate(R.layout.activity_buyer_reason_denied_order, null);
                builder.setView(popupView);
                AlertDialog alertDialog = builder.create();
                Button cancel = (Button) popupView.findViewById(R.id.cancel);
                Button remove = (Button) popupView.findViewById(R.id.remove);

                TextView reasonDenied=(TextView) popupView.findViewById(R.id.reason);
                reasonDenied.setText(order.getDenialReason());
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Đóng popup khi nút "Cancel" được nhấn
                        alertDialog.dismiss();
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Đóng popup khi nút "Cancel" được nhấn
                        alertDialog.dismiss();
                    }
                });

                remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Order");
                        mDatabase.child(order.getOrderId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // Xóa thành công
                                        Toast.makeText(context, "Đơn hàng đã được xóa thành công", Toast.LENGTH_SHORT).show();
                                        List<Order> updatedOrderList = new ArrayList<>(orderList);
                                        updatedOrderList.remove(order);
                                        updateOrderList(updatedOrderList);
                                        alertDialog.dismiss();
                                        // Cập nhật giao diện hoặc thông báo tùy theo yêu cầu của bạn
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Xóa thất bại
                                        // Cập nhật giao diện hoặc thông báo tùy theo yêu cầu của bạn
                                    }
                                });
                         }
                });
                alertDialog.show();

            }

        });

        return view;
    }
}
