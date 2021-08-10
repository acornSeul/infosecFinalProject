package com.example.team5_final;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.team5_final.dto.UserInvoice;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import lombok.AllArgsConstructor;

class ViewHolder extends RecyclerView.ViewHolder{
    TextView item_in_num;
    TextView item_name;
    TextView item_re_name;
    Button btn_invoice;

    public ViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);
        item_in_num = itemView.findViewById(R.id.item_in_num);
        item_name = itemView.findViewById(R.id.item_name);
        item_re_name = itemView.findViewById(R.id.item_re_name);
        btn_invoice = itemView.findViewById(R.id.btn_invoice);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ApplyInfoActivity.class);
                intent.putExtra("in_num", item_in_num.getText());
                v.getContext().startActivity(intent);
            }
        });
        btn_invoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent btn_intent = new Intent(v.getContext(), UserInvoiceActivity.class);
                v.getContext().startActivity(btn_intent);
            }
        });
    }
}
@AllArgsConstructor
public class RecyclerAdapter extends RecyclerView.Adapter<ViewHolder>{
    private Context context;
    private int resourceId;
    private List<UserInvoice> dataList;

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(resourceId,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        UserInvoice userInvoice = dataList.get(position);
        holder.item_in_num.setText(userInvoice.getIn_num());
        holder.item_name.setText(userInvoice.getName());
        holder.item_re_name.setText(userInvoice.getRe_name());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
