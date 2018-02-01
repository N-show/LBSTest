package com.sony.www.demo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by nsh on 2017/10/24.
 */

public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.ViewHolder> {


    private List<Msg> msgs;

    public MsgAdapter(List<Msg> msgs) {
        this.msgs = msgs;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Msg msg = msgs.get(position);
        int type = msg.getType();

        if (type == Msg.TYPE_RECEIVED) {
            //received left
            holder.rightLayout.setVisibility(View.GONE);
            holder.leftLayout.setVisibility(View.VISIBLE);
            holder.leftMsg.setText(msg.getCotent());

        } else if (type == Msg.TYPE_SEND) {
            //send right
            holder.leftLayout.setVisibility(View.GONE);
            holder.rightLayout.setVisibility(View.VISIBLE);
            holder.rightMsg.setText(msg.getCotent());
        }
    }

    @Override
    public int getItemCount() {
        return msgs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout leftLayout;
        TextView leftMsg;
        LinearLayout rightLayout;
        TextView rightMsg;

        public ViewHolder(View itemView) {
            super(itemView);

            leftLayout = (LinearLayout) itemView.findViewById(R.id.left_layout);
            leftMsg = (TextView) itemView.findViewById(R.id.left_msg);
            rightLayout = (LinearLayout) itemView.findViewById(R.id.right_layout);
            rightMsg = (TextView) itemView.findViewById(R.id.right_msg);


        }
    }
}
