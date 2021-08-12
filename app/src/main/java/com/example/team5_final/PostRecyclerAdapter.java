package com.example.team5_final;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.team5_final.dto.List;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


class PostViewHolder extends RecyclerView.ViewHolder{
    public TextView textView;
    public CheckBox checkBox;

    PostViewHolder(Context context, View itemView){
        super(itemView);

        textView = itemView.findViewById(R.id.txt_listInNum);
        checkBox = itemView.findViewById(R.id.check_postList);
    }
}

public class PostRecyclerAdapter extends RecyclerView.Adapter<PostViewHolder> {
    private ArrayList<List> postData;
    @NonNull
    @NotNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_post_recycler, parent, false);

        PostViewHolder viewHolder = new PostViewHolder(context, view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PostViewHolder holder, int position) {
        String text = postData.get(position).toString();
        holder.textView.setText(text);
    }

    @Override
    public int getItemCount() {
        return postData.size();
    }
}
