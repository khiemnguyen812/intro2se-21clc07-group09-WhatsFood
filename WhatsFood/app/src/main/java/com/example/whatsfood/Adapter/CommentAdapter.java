package com.example.whatsfood.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.whatsfood.Model.Comment;
import com.example.whatsfood.R;

import java.util.List;

public class CommentAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<String> commentList;

    public CommentAdapter(Context context, int layout, List<String> commentList) {
        this.context = context;
        this.layout = layout;
        this.commentList = commentList;
    }

    @Override
    public int getCount() {
        return commentList.size();
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
    public View getView(int position, View view, ViewGroup viewGroup) {
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view=inflater.inflate(layout,null);
        if(layout== R.layout.comment_line) {
            TextView username=(TextView) view.findViewById(R.id.username);
            TextView content=(TextView) view.findViewById(R.id.content);
            String list= commentList.get(position);

            content.setText(list);
        }
        return view;
    }
}
