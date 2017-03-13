package com.example.yodgor777.themoviedb;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by yodgor777 on 2017-02-07.
 */

public class ReviewAdapter extends BaseAdapter {
    Context context;
    ArrayList<reviewDataHolder> lists;
    LayoutInflater inflator;

    public ReviewAdapter(Context context, ArrayList<reviewDataHolder> lists) {
        this.context = context;
        this.lists = lists;
        inflator = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int i) {
        return lists.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            view = inflator.inflate(R.layout.review_layout, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) view.findViewById(R.id.userName);
            viewHolder.comment = (TextView) view.findViewById(R.id.comment);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.name.setText(lists.get(i).userName);
        viewHolder.comment.setText(lists.get(i).Comment);
        return view;
    }

    private class ViewHolder {
        TextView name,comment;
    }

}
