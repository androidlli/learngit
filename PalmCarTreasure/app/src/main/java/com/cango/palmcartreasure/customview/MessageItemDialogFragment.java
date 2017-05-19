package com.cango.palmcartreasure.customview;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.cango.palmcartreasure.R;
import com.cango.palmcartreasure.model.MessageList;

/**
 * Created by cango on 2017/5/15.
 */

public class MessageItemDialogFragment extends DialogFragment {
    private MessageList.DataBean.MessageListBean messageListBean;
    private AlertDialog dialog;
    public static MessageItemDialogFragment getInstance(MessageList.DataBean.MessageListBean messageListBean){
        MessageItemDialogFragment fragment=new MessageItemDialogFragment();
        Bundle args=new Bundle();
        args.putParcelable("messageListBean",messageListBean);
        fragment.setArguments(args);
        return fragment;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        messageListBean = getArguments().getParcelable("messageListBean");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.message_item_dialog,null);
        TextView tvDate= (TextView) view.findViewById(R.id.tv_date);
        TextView tvContent= (TextView) view.findViewById(R.id.tv_content);
        tvDate.setText(messageListBean.getSendTime());
        tvContent.setText(messageListBean.getMessageContent());
        builder.setView(view);
        dialog = builder.create();
        return dialog;
    }

}
