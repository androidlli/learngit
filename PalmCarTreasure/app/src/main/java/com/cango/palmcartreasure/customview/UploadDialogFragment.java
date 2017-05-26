package com.cango.palmcartreasure.customview;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioGroup;

import com.cango.palmcartreasure.R;
import com.cango.palmcartreasure.baseAdapter.BaseHolder;
import com.cango.palmcartreasure.baseAdapter.OnBaseItemClickListener;
import com.cango.palmcartreasure.model.SelectPhoto;
import com.cango.palmcartreasure.trailer.map.PhotoAdapter;
import com.cango.palmcartreasure.util.CommUtil;
import com.cango.palmcartreasure.util.FileUtils;
import com.cango.palmcartreasure.util.ToastUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by cango on 2017/4/27.
 */

public class UploadDialogFragment extends DialogFragment {
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    public void setmListener(UploadListener mListener) {
        this.mListener = mListener;
    }

    private UploadListener mListener;
    private String mCurrentPhotoPath;
    private SelectPhoto mCurrentData;
    private ArrayList<SelectPhoto> datas;
    private PhotoAdapter mAdapter;
    private List<String> stringList = new ArrayList<>();
    String firstValue;
    String secondValue;
    boolean isDoUp;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.upload_dialog, null);
        view.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        RadioGroup rgFirst = (RadioGroup) view.findViewById(R.id.rg_first_question);
        rgFirst.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rb_first_one:
                        firstValue = "1";
                        break;
                    case R.id.rb_first_two:
                        firstValue = "2";
                        break;
                    case R.id.rb_first_three:
                        firstValue = "3";
                        break;
                    case R.id.rb_first_four:
                        firstValue = "4";
                        break;
                }
            }
        });
        RadioGroup rgSecond = (RadioGroup) view.findViewById(R.id.rg_second_question);
        rgSecond.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rb_second_one:
                        secondValue = "1";
                        break;
                    case R.id.rb_second_two:
                        secondValue = "2";
                        break;
                    case R.id.rb_second_three:
                        secondValue = "3";
                        break;
                    case R.id.rb_second_four:
                        secondValue = "4";
                        break;
                }
            }
        });
        final CheckBox cbThirdOne = (CheckBox) view.findViewById(R.id.cb_third_one);
        final CheckBox cbThirdTwo = (CheckBox) view.findViewById(R.id.cb_third_two);
        final CheckBox cbThirdThree = (CheckBox) view.findViewById(R.id.cb_third_three);

        final CheckBox cbFourthOne = (CheckBox) view.findViewById(R.id.cb_fourth_one);
        final CheckBox cbFourthTwo = (CheckBox) view.findViewById(R.id.cb_fourth_two);
        final CheckBox cbFourthThree = (CheckBox) view.findViewById(R.id.cb_fourth_three);
        final CheckBox cbFourthFour = (CheckBox) view.findViewById(R.id.cb_fourth_four);
        view.findViewById(R.id.tv_trailer_complete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (datas.size() > 1) {
                    if (checkQuestonIsOver()) {
                        isDoUp=true;
                        mListener.upLoadClick(datas, stringList);
                        dismiss();
                    } else {
                        ToastUtils.showShort("请填写全部问卷！");
                    }
                } else {
                    ToastUtils.showShort("请拍摄照片！");
                }
            }

            private boolean checkQuestonIsOver() {
                boolean thirdOne = cbThirdOne.isChecked();
                boolean thirdTwo = cbThirdTwo.isChecked();
                boolean thirdThree = cbThirdThree.isChecked();

                boolean fourthOne = cbFourthOne.isChecked();
                boolean fourthTwo = cbFourthTwo.isChecked();
                boolean fourthThree = cbFourthThree.isChecked();
                boolean fourthFour = cbFourthFour.isChecked();
                if (!CommUtil.checkIsNull(firstValue) && !CommUtil.checkIsNull(secondValue)
                        && (thirdOne || thirdTwo || thirdThree) && (fourthOne || fourthTwo || fourthThree || fourthFour)) {
                    stringList.add("QN01,1;" + firstValue);
                    stringList.add("QN01,2;" + secondValue);
                    String strThird = "";
                    if (thirdOne)
                        strThird += "1,";
                    if (thirdTwo)
                        strThird += "2,";
                    if (thirdThree)
                        strThird += "3,";
                    String strFourth = "";
                    if (fourthOne)
                        strFourth += "1,";
                    if (fourthTwo)
                        strFourth += "2,";
                    if (fourthThree)
                        strFourth += "3,";
                    if (fourthFour)
                        strFourth += "4,";
                    stringList.add("QN01,3;" + strThird);
                    stringList.add("QN01,4;" + strFourth);
                    return true;
                }
                return false;
            }
        });
        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_upload);
        datas = new ArrayList<>();
        datas.add(new SelectPhoto("", true));
        mAdapter = new PhotoAdapter(getActivity(), datas, false);
        mAdapter.setOnItemClickListener(new OnBaseItemClickListener<SelectPhoto>() {
            @Override
            public void onItemClick(BaseHolder viewHolder, SelectPhoto data, int position) {
                mCurrentData = data;
                if (data.isShowSelect()) {
//                    Collections.reverse(datas);
//                    datas.add(new SelectPhoto("",false));
//                    Collections.reverse(datas);
//                    mAdapter.notifyDataSetChanged();
                    openCamera();
                }
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return alertDialog;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (!isDoUp){
            deleteImagePictures();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            if (mCurrentData != null) {
                if (mCurrentData.isShowSelect()) {
                    Collections.reverse(datas);
                    datas.add(new SelectPhoto(mCurrentPhotoPath, false));
                    Collections.reverse(datas);
                    mAdapter.notifyDataSetChanged();
                }
            }
        }
        if (resultCode == Activity.RESULT_CANCELED) {
            deleteImageFile();
        }
    }

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

    private void deleteImagePictures() {
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        boolean deleteDir = FileUtils.deleteDir(storageDir);
    }

    public interface UploadListener {
        void upLoadClick(List<SelectPhoto> selectPhotoList, List<String> strings);
    }
}
