package com.cango.palmcartreasure;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.content.FileProvider;
import android.widget.TextView;

import com.cango.palmcartreasure.api.Api;
import com.cango.palmcartreasure.api.TrailerTaskService;
import com.cango.palmcartreasure.base.BaseActivity;
import com.cango.palmcartreasure.login.LoginActivity;
import com.cango.palmcartreasure.net.NetManager;
import com.cango.palmcartreasure.trailer.admin.AdminActivity;
import com.cango.palmcartreasure.trailer.main.LaunchActivity;
import com.cango.palmcartreasure.trailer.main.TrailerActivity;
import com.cango.palmcartreasure.util.AppUtils;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends BaseActivity {

    private String mCurrentPDFPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

//        downLoadFile(new OnDownloadListener() {
//            @Override
//            public void onDownloadSuccess(File file) {
//                Logger.d(file.getAbsolutePath());
//                Intent pdfFileIntent = getPdfFileIntent(file);
//                try {
//                    startActivity(pdfFileIntent);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onDownloading(int progress) {
////                Logger.d("onDownloading");
//            }
//
//            @Override
//            public void onDownloadFailed(String error) {
//                Logger.d(error);
//            }
//        });

        TextView tvVersion = (TextView) findViewById(R.id.tv_version);
        tvVersion.setText("v" + AppUtils.getVersionName(this));
        boolean isShow = MtApplication.mSPUtils.getBoolean(Api.ISSHOWSTARTOVER);
        if (!isShow) {
            MtApplication.mSPUtils.put(Api.ISSHOWSTARTOVER, true);
            startDelay(LaunchActivity.class);
        } else {
            int anInt = MtApplication.mSPUtils.getInt(Api.USERROLEID);
            if (anInt != -1) {
                if (anInt == Api.ADMIN_CODE) {
                    startDelay(AdminActivity.class);
                } else {
                    startDelay(TrailerActivity.class);
                }
            } else {
                startDelay(LoginActivity.class);
            }
        }
    }

    private void startDelay(final Class cls) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, cls);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }

    private void downLoadFile(final OnDownloadListener listener) {
        NetManager.getInstance().create(TrailerTaskService.class)
                .docDownLoad(1, 1, 1, "1")
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        InputStream is = null;
                        byte[] buf = new byte[2048];
                        int len = 0;
                        FileOutputStream fos = null;
//                        // 储存下载文件的目录
//                        String savePath = isExistDir(saveDir);
                        try {
                            is = response.body().byteStream();
                            long total = response.body().contentLength();
                            File file = createDownFile();
                            fos = new FileOutputStream(file);
                            long sum = 0;
                            while ((len = is.read(buf)) != -1) {
                                fos.write(buf, 0, len);
                                sum += len;
                                int progress = (int) (sum * 1.0f / total * 100);
                                // 下载中
                                listener.onDownloading(progress);
                            }
                            fos.flush();
                            // 下载完成
                            listener.onDownloadSuccess(file);
                        } catch (Exception e) {
                            e.printStackTrace();
                            listener.onDownloadFailed(e.getMessage());
                        } finally {
                            try {
                                if (is != null)
                                    is.close();
                            } catch (IOException e) {
                            }
                            try {
                                if (fos != null)
                                    fos.close();
                            } catch (IOException e) {
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Logger.d(t.getMessage());
                    }
                });
    }

    private File createDownFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "PDF_" + timeStamp + "_";
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".pdf",         /* suffix */
                storageDir      /* directory */
        );
        mCurrentPDFPath = image.getAbsolutePath();
        Logger.d(mCurrentPDFPath);
        return image;
    }

    public Intent getPdfFileIntent(File pdfFile) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Uri pdfUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            pdfUri = FileProvider.getUriForFile(this, "com.cango.palmcartreasure.fileprovider", pdfFile);

        } else {
            pdfUri = Uri.fromFile(pdfFile);
        }
        intent.setDataAndType(pdfUri, "application/pdf");
        return intent;
    }

    public interface OnDownloadListener {
        /**
         * 下载成功
         */
        void onDownloadSuccess(File file);

        /**
         * @param progress 下载进度
         */
        void onDownloading(int progress);

        /**
         * 下载失败
         */
        void onDownloadFailed(String error);
    }
}
