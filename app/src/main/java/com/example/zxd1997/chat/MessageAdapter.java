package com.example.zxd1997.chat;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<Message> messages=new ArrayList<Message>();
    static final int MESSAGE=0;
    static final int SELF_MESSAGE=1;
    public MessageAdapter(List<Message> messages){
        this.messages=messages;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        if (viewType==MESSAGE){
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.message,parent,false);
            viewHolder=new ViewHolder(view);
        }else{
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.message_self,parent,false);
            viewHolder=new SelfHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position)==MESSAGE){
            ViewHolder viewHolder=(ViewHolder)holder;
            viewHolder.name.setText(messages.get(position).getName());
            viewHolder.content.setText(messages.get(position).getContent());
        }else{
            SelfHolder viewHolder=(SelfHolder)holder;
            viewHolder.name.setText(messages.get(position).getName());
            viewHolder.content.setText(messages.get(position).getContent());
        }
    }

    @Override
    public int getItemViewType(int position) {
        return messages.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView content;
        SimpleDraweeView header;
        public ViewHolder(View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            content=itemView.findViewById(R.id.content);
            header=itemView.findViewById(R.id.header);
        }
    }
    class SelfHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView content;
        SimpleDraweeView header;
        public SelfHolder(View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.selfname);
            content=itemView.findViewById(R.id.selfcontent);
            header=itemView.findViewById(R.id.selfheader);
        }
    }
}
