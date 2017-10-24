package com.example.the.youtubetest;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by the on 2017-10-24.
 */

public class YoutubeAdapter extends ArrayAdapter<SearchData> {

    private Context mContext;
    List<SearchData> mList;
    LayoutInflater inf;

    public YoutubeAdapter (Context context, List<SearchData> list){
        super(context, R.layout.listview_start,list);
        mContext = context;
        mList = list;
        inf = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        if(row==null){
            row = inf.inflate(R.layout.listview_start,null);
        }

        return row;
    }
}
