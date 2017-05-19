package com.cango.palmcartreasure.trailer.message;

import com.cango.palmcartreasure.MtApplication;
import com.cango.palmcartreasure.api.Api;
import com.cango.palmcartreasure.api.LoginService;
import com.cango.palmcartreasure.model.MessageList;
import com.cango.palmcartreasure.model.TaskAbandon;
import com.cango.palmcartreasure.net.NetManager;
import com.cango.palmcartreasure.net.RxSubscriber;

import java.util.HashMap;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by cango on 2017/5/8.
 */

public class MessagePresenter implements MessageContract.Presenter{
    private MessageContract.View mView;
    private LoginService mService;
    public MessagePresenter(MessageContract.View messageView) {
        mView=messageView;
        mView.setPresenter(this);
        mService= NetManager.getInstance().create(LoginService.class);
    }

    @Override
    public void start() {

    }

    @Override
    public void openDetailTask(int id) {

    }

    @Override
    public void loadMessages(boolean showRefreshLoadingUI, int status,int pageCount, int pageSize) {
        //(-1-全部（已发送和已读）/0-未发送 / 1-已发送 / 2-已读 /9-发送失败)
        if (mView.isActive()){
            mView.showMessagesIndicator(showRefreshLoadingUI);
        }
        mService.getMessageList(MtApplication.mSPUtils.getInt(Api.USERID),status,pageCount,pageSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<MessageList>() {
                    @Override
                    protected void _onNext(MessageList o) {
                        if (mView.isActive()){
                            mView.showMessagesIndicator(false);
                            int code = o.getCode();
                            if (code==0){
                                mView.showMessages(o.getData().getMessageList());
                            }else {
                                mView.showMessagesError();
                            }
                        }
                    }

                    @Override
                    protected void _onError() {
                        if (mView.isActive()){
                            mView.showMessagesIndicator(false);
                            mView.showMessagesError();
                        }
                    }
                });
    }

    @Override
    public void postMessageRead(int messageID) {
        Map<String,Object> objectMap=new HashMap<>();
        objectMap.put("userid",MtApplication.mSPUtils.getInt(Api.USERID));
        objectMap.put("messageID",messageID);
        mService.postMessageRead(objectMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<TaskAbandon>() {
                    @Override
                    protected void _onNext(TaskAbandon o) {
                        if (mView.isActive()){
                            int code = o.getCode();
                            boolean isSuccess=code==0;
                            mView.showMessageSuccess(isSuccess,o.getMsg());
                        }
                    }

                    @Override
                    protected void _onError() {

                    }
                });
    }
}
