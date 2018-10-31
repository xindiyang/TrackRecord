package com.example.codoon.myapplication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.codoon.myapplication.DbFlow.MyDataBase;
import com.example.codoon.myapplication.R;
import com.example.codoon.myapplication.Record.RecordActivity;
import com.example.codoon.myapplication.Record.RecordShowActivity;

import java.util.List;

/**
 * 创建者：Sunday
 * 项目名：coddonGMap
 * 包名：com.example.codoon.myapplication.Adapter
 * 文件名：RecordAdapter
 * 创建时间：2018/10/22 下午4:16
 * 描述：TODO
 */
public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.MyViewHolder> {
    private Context context;
    private RecordActivity recordActivity;
    private List<MyDataBase> myDataBases;

    public RecordAdapter(RecordActivity recordActivity1, List<MyDataBase> myDataBases1) {
        this.recordActivity = recordActivity1;
        this.myDataBases = myDataBases1;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        View item;
        ImageView imageView;
        TextView time, distance, sportTime;

        public MyViewHolder(View itemView) {
            super(itemView);
            item = itemView;
            imageView = itemView.findViewById(R.id.imageView2);
            time = itemView.findViewById(R.id.textView);
            distance = itemView.findViewById(R.id.km);
            sportTime = itemView.findViewById(R.id.textView4);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recorditem_activity, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        myViewHolder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), RecordShowActivity.class);
                recordActivity.startActivity(intent);
            }
        });
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.time.setText(myDataBases.get(position).time);
        holder.sportTime.setText(String.valueOf(myDataBases.get(position).duration));
        holder.distance.setText(String.valueOf(myDataBases.get(position).distance));
    }

    @Override
    public int getItemCount() {
        return myDataBases.size();
    }
}
