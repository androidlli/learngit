package com.cango.palmcartreasure.customview;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.cango.palmcartreasure.R;
import com.cango.palmcartreasure.util.FileUtils;

import java.io.File;

/**
 * Created by cango on 2017/4/27.
 */

public class DotTrailerDialogFragment extends DialogFragment {
    public void setmListener(OnStatusListener mListener) {
        this.mListener = mListener;
    }

    private OnStatusListener mListener;
    boolean isDoUp;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dot_trailer_dialog, null);
        view.findViewById(R.id.tv_now).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDoUp=true;
                dismiss();
                mListener.onStatusClick(0);
            }
        });
        view.findViewById(R.id.tv_delay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDoUp=true;
                dismiss();
                mListener.onStatusClick(1);
            }
        });
        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (!isDoUp){
            deleteImagePictures();
        }
    }

    public interface OnStatusListener{
        //type 0:立即 type 1:延迟
        void onStatusClick(int type);
    }
    private void deleteImagePictures() {
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        boolean deleteDir = FileUtils.deleteDir(storageDir);
    }
}
