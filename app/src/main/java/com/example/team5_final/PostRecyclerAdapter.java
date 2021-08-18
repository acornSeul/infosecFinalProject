package com.example.team5_final;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.team5_final.dto.PostList;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;


class PostViewHolder extends RecyclerView.ViewHolder{
    TextView txt_address;
    TextView txt_name;
    TextView txt_state;
    TextView txt_inNum;
    CheckBox checkBox;

    public PostViewHolder(Context context, View itemView){
        super(itemView);

        txt_address = itemView.findViewById(R.id.txt_address);
        txt_name = itemView.findViewById(R.id.txt_listName);
        txt_state = itemView.findViewById(R.id.txt_state);
        checkBox = itemView.findViewById(R.id.check_postList);
        txt_inNum = itemView.findViewById(R.id.txt_listInNum);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), PostListDetailActivity.class);
                intent.putExtra("in_num", txt_inNum.getText());
                v.getContext().startActivity(intent);
            }
        });
    }
}
@AllArgsConstructor
public class PostRecyclerAdapter extends RecyclerView.Adapter<PostViewHolder> {
    private ArrayList<PostList> postData = new ArrayList<>();
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
        PostList postList = postData.get(position);
        Log.d("postList seul result", postList.getAddress());
        holder.txt_address.setText(postList.getAddress());
        holder.txt_name.setText(postList.getName());
        holder.txt_inNum.setText("[ " + postList.getIn_num() + " ]");


        if (postList.getState().equals("none")){
            holder.txt_state.setText("대기");
        }
        else if (postList.getState().equals("leave")){
            holder.txt_state.setText("출발");
            holder.txt_state.setBackgroundColor(Color.parseColor("#FFFFFF"));
            holder.txt_state.setTextColor(Color.parseColor("#FF0000"));
        }
        else{
            holder.txt_state.setText("완료");
            holder.txt_state.setTextColor(Color.parseColor("#FFFFFF"));
            holder.txt_state.setBackgroundColor(Color.parseColor("#696969"));
        }

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("seul check click???", String.valueOf(holder.checkBox.isChecked()));
                postList.setSelected(holder.checkBox.isChecked());
            }
        });
    }
    @Override
    public int getItemCount() {
        return postData.size();
    }

    public List<PostList> getPostList() {
        return postData;
    }

}
