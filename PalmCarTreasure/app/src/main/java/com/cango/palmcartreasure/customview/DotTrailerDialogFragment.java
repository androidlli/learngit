package com.cango.palmcartreasure.customview;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.cango.palmcartreasure.R;

/**
 * Created by cango on 2017/4/27.
 */

public class DotTrailerDialogFragment extends DialogFragment {
    public void setmListener(OnStatusListener mListener) {
        this.mListener = mListener;
    }

    private OnStatusListener mListener;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dot_trailer_dialog, null);
        view.findViewById(R.id.tv_now).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                mListener.onStatusClick(0);
            }
        });
        view.findViewById(R.id.tv_delay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                mListener.onStatusClick(1);
            }
        });
        builder.setView(view);
        return builder.create();
    }

    public interface OnStatusListener{
        //type 0:立即 type 1:延迟
        void onStatusClick(int type);
    }
}
