package com.groupmessage.Entity;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.groupmessage.R;

import java.util.List;

public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.ViewHolder>{

    private List<Msg> mMsgList;

    public MsgAdapter(List<Msg>msgList){
        this.mMsgList =msgList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.msg_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Msg msg = mMsgList.get(i);
        if (msg.getType() == Msg.TYPE_RECEIVED) {
            // 如果是收到的消息，则显示左边的消息布局，将右边的消息布局隐藏
            viewHolder.leftLayout.setVisibility(View.VISIBLE);
            viewHolder.rightLayout.setVisibility(View.GONE);
            viewHolder.left_imageLayout.setVisibility(View.GONE);
            viewHolder.right_imageLayout.setVisibility(View.GONE);

            viewHolder.leftMsg.setText(msg.getContent());
        } else if(msg.getType() == Msg.TYPE_SENT) {
            // 如果是发出的消息，则显示右边的消息布局，将左边的消息布局隐藏
            viewHolder.rightLayout.setVisibility(View.VISIBLE);
            viewHolder.leftLayout.setVisibility(View.GONE);
            viewHolder.left_imageLayout.setVisibility(View.GONE);
            viewHolder.right_imageLayout.setVisibility(View.GONE);

            viewHolder.rightMsg.setText(msg.getContent());
        }else if(msg.getType() == Msg.TYPE_IMAGE_RECEIVED){
            viewHolder.rightLayout.setVisibility(View.GONE);
            viewHolder.leftLayout.setVisibility(View.GONE);
            viewHolder.left_imageLayout.setVisibility(View.VISIBLE);
            viewHolder.right_imageLayout.setVisibility(View.GONE);

            Bitmap bitmap = BitmapFactory.decodeFile(msg.getContent());
            viewHolder.leftImg.setImageBitmap(bitmap);
        }else if(msg.getType() == Msg.TYPE_IMAGE_SENT){
            viewHolder.rightLayout.setVisibility(View.GONE);
            viewHolder.leftLayout.setVisibility(View.GONE);
            viewHolder.left_imageLayout.setVisibility(View.GONE);
            viewHolder.right_imageLayout.setVisibility(View.VISIBLE);

            Bitmap bitmap = BitmapFactory.decodeFile(msg.getContent());
            viewHolder.rightImg.setImageBitmap(bitmap);

        }

    }

    @Override
    public int getItemCount() {
        return this.mMsgList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout leftLayout;

        LinearLayout rightLayout;

        LinearLayout left_imageLayout;

        LinearLayout right_imageLayout;

        TextView leftMsg;

        TextView rightMsg;

        ImageView leftImg;

        ImageView rightImg;


        public ViewHolder(View view) {
            super(view);
            leftLayout = (LinearLayout) view.findViewById(R.id.left_layout);
            rightLayout = (LinearLayout) view.findViewById(R.id.right_layout);
            left_imageLayout =(LinearLayout)view.findViewById(R.id.left_image_layout);
            right_imageLayout =(LinearLayout)view.findViewById(R.id.right_image_layout);
            leftMsg = (TextView) view.findViewById(R.id.left_msg);
            rightMsg = (TextView) view.findViewById(R.id.right_msg);
            leftImg = view.findViewById(R.id.left_image);
            rightImg = view.findViewById(R.id.right_image);
        }

    }
}
