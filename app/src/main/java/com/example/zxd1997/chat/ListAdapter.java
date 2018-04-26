package com.example.zxd1997.chat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<ListMessage> listMessages = new ArrayList<>();

    public ListAdapter(List<ListMessage> listMessage) {
        this.listMessages = listMessage;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final RecyclerView.ViewHolder viewHolder;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list, parent, false);
        viewHolder = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                ListMessage listMessage = listMessages.get(position);
                Intent intent = new Intent(MyApplication.getContext(), ChatActivity.class);
                intent.putExtra("from", listMessage.getFrom());
                MyApplication.getContext().startActivity(intent);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.from.setText(listMessages.get(position).getFrom());
        viewHolder.newest.setText(listMessages.get(position).getLast());
    }

    @Override
    public int getItemCount() {
        return listMessages.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView header;
        TextView from;
        TextView newest;

        public ViewHolder(View itemView) {
            super(itemView);
            header = itemView.findViewById(R.id.listhead);
            from = itemView.findViewById(R.id.from);
            newest = itemView.findViewById(R.id.newest);
        }
    }
}
