package com.cango.palmcartreasure.customview;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cango.palmcartreasure.R;
import com.cango.palmcartreasure.baseAdapter.BaseHolder;
import com.cango.palmcartreasure.baseAdapter.MemberItemDecoration;
import com.cango.palmcartreasure.baseAdapter.OnBaseItemClickListener;
import com.cango.palmcartreasure.model.SelectPhoto;
import com.cango.palmcartreasure.trailer.map.PhotoAdapter;
import com.cango.palmcartreasure.trailer.message.MessageAdapter;
import com.cango.palmcartreasure.util.SizeUtil;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by cango on 2017/4/27.
 */

public class UploadDialogFragment extends DialogFragment {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private String mCurrentPhotoPath;
    private SelectPhoto mCurrentData;
    private ArrayList<SelectPhoto> datas;
    private PhotoAdapter mAdapter;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.upload_dialog, null);
        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_upload);
        datas=new ArrayList<>();
        datas.add(new SelectPhoto("",true));
        mAdapter=new PhotoAdapter(getActivity(),datas,false);
        mAdapter.setOnItemClickListener(new OnBaseItemClickListener<SelectPhoto>() {
            @Override
            public void onItemClick(BaseHolder viewHolder, SelectPhoto data, int position) {
                mCurrentData=data;
//                Logger.d(position);
                if (data.isShowSelect()){
//                    Collections.reverse(datas);
//                    datas.add(new SelectPhoto("",false));
//                    Collections.reverse(datas);
//                    mAdapter.notifyDataSetChanged();
                    openCamera();
                }
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return alertDialog;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Logger.d("onActivityResult: requestCode = " + requestCode + "  resultCode = " + resultCode);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            if (mCurrentData!=null){
                if (mCurrentData.isShowSelect()){
                    Collections.reverse(datas);
                    datas.add(new SelectPhoto(mCurrentPhotoPath,false));
                    Collections.reverse(datas);
                    mAdapter.notifyDataSetChanged();
                }
            }
        }
        if (resultCode == Activity.RESULT_CANCELED) {
            deleteImageFile();
        }
    }

//    private void setPic() {
//        int targetW = ivShow.getWidth();
//        int targetH = ivShow.getHeight();
//        Log.d(TAG, "setPic: targetW = " + targetW + "  targetH = " + targetH);
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        BitmapFactory.decodeFile(mCurrentPhotoPath, options);
//        int photoW = options.outWidth;
//        int photoH = options.outHeight;
//        Log.d(TAG, "photoW :" + photoW + " photoH :" + photoH);
//        int min = Math.min(photoW / targetW, photoH / targetH);
//        if (min <= 0)
//            min = 1;
//        options.inJustDecodeBounds = false;
//        options.inSampleSize = min;
//        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, options);
//        Log.d(TAG, "W " + bitmap.getWidth() + " H :" + bitmap.getHeight());
//        ivShow.setImageBitmap(bitmap);
//    }

    private void openCamera() {
        boolean hasSystemFeature = getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
        if (hasSystemFeature) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (photoFile != null) {
                    Uri photoURI;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        photoURI = FileProvider.getUriForFile(getActivity(), "com.cango.palmcartreasure.fileprovider", photoFile);

                    } else {
                        photoURI = Uri.fromFile(photoFile);
                    }
                    Logger.d("photoURI: " + photoURI.toString());
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private boolean deleteImageFile() {
        if (mCurrentPhotoPath != null) {
            File emptyFile = new File(mCurrentPhotoPath);
            if (emptyFile.exists())
                return emptyFile.delete();
        }
        return false;
    }
}
