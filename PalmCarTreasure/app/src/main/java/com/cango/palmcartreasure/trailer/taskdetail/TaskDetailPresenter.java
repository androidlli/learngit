package com.cango.palmcartreasure.trailer.taskdetail;

import com.cango.palmcartreasure.MtApplication;
import com.cango.palmcartreasure.R;
import com.cango.palmcartreasure.api.Api;
import com.cango.palmcartreasure.api.TrailerTaskService;
import com.cango.palmcartreasure.model.CallRecord;
import com.cango.palmcartreasure.model.CaseInfo;
import com.cango.palmcartreasure.model.CustomerInfo;
import com.cango.palmcartreasure.model.HomeVisitRecord;
import com.cango.palmcartreasure.model.TaskDetailData;
import com.cango.palmcartreasure.net.NetManager;
import com.cango.palmcartreasure.net.RxSubscriber;
import com.cango.palmcartreasure.util.CommUtil;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by cango on 2017/4/17.
 */

public class TaskDetailPresenter implements TaskDetailContract.Presenter {
    private TaskDetailContract.View mView;
    TrailerTaskService mService;

    public TaskDetailPresenter(TaskDetailContract.View detailView) {
        mView = detailView;
        mView.setPresenter(this);
        mService = NetManager.getInstance().create(TrailerTaskService.class);
    }

    @Override
    public void start() {

    }

    @Override
    public void loadTaskDetail(int type, boolean showLoadingUI, int agencyID, int caseID) {
        if (mView.isActive()) {
            mView.showTaskDetailIndicator(showLoadingUI);
        }
        switch (type) {
            case 0:
                mService.callRecord(MtApplication.mSPUtils.getInt(Api.USERID), agencyID, caseID)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new RxSubscriber<CallRecord>() {
                            @Override
                            protected void _onNext(CallRecord o) {
                                if (mView.isActive()) {
                                    mView.showTaskDetailIndicator(false);
                                    int code = o.getCode();
                                    if (code == 0) {
                                        if (o.getData() == null) {
                                            mView.showTasksDetailError();
                                        } else {
                                            if (CommUtil.checkIsNull(o.getData().getCallRecordData()) && CommUtil.checkIsNull(o.getData().getOverdueData())) {
                                                mView.showNoTaskDetail();
                                            } else {
                                                TaskDetailData data = setCallInfo(o);
                                                mView.showTaskDetail(data);
                                            }
                                        }
                                    } else {
                                        mView.showTasksDetailError();
                                    }
                                }
                            }

                            @Override
                            protected void _onError() {
                                if (mView.isActive()) {
                                    mView.showTaskDetailIndicator(false);
                                    mView.showTasksDetailError();
                                }
                            }
                        });
                break;
            case 1:
                mService.homeVisitRecord(MtApplication.mSPUtils.getInt(Api.USERID), agencyID, caseID)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new RxSubscriber<HomeVisitRecord>() {
                            @Override
                            protected void _onNext(HomeVisitRecord o) {
                                if (mView.isActive()) {
                                    mView.showTaskDetailIndicator(false);
                                    int code = o.getCode();
                                    if (code == 0) {
                                        if (o.getData() == null) {
                                            mView.showTasksDetailError();
                                        } else {
                                            if (o.getData().size() > 0) {
                                                TaskDetailData data = setHomeInfo(o);
                                                mView.showTaskDetail(data);
                                            } else {
                                                mView.showNoTaskDetail();
                                            }
                                        }
                                    } else {
                                        mView.showTasksDetailError();
                                    }
                                }
                            }

                            @Override
                            protected void _onError() {
                                if (mView.isActive()) {
                                    mView.showTaskDetailIndicator(false);
                                    mView.showTasksDetailError();
                                }
                            }
                        });
                break;
            case 2:
                mService.caseInfo(MtApplication.mSPUtils.getInt(Api.USERID), agencyID, caseID)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new RxSubscriber<CaseInfo>() {
                            @Override
                            protected void _onNext(CaseInfo o) {
                                if (mView.isActive()) {
                                    mView.showTaskDetailIndicator(false);
                                    int code = o.getCode();
                                    if (code == 0) {
                                        if (o.getData() == null) {
                                            mView.showTasksDetailError();
                                        } else {
                                            TaskDetailData data = setCaseInfo(o);
                                            mView.showTaskDetail(data);
                                        }
                                    } else {
                                        mView.showTasksDetailError();
                                    }
                                }
                            }

                            @Override
                            protected void _onError() {
                                if (mView.isActive()) {
                                    mView.showTaskDetailIndicator(false);
                                    mView.showTasksDetailError();
                                }
                            }
                        });
                break;
            case 3:
                mService.customerinfo(MtApplication.mSPUtils.getInt(Api.USERID), agencyID, caseID)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new RxSubscriber<CustomerInfo>() {
                            @Override
                            protected void _onNext(CustomerInfo o) {
                                if (mView.isActive()) {
                                    mView.showTaskDetailIndicator(false);
                                    int code = o.getCode();
                                    if (code == 0) {
                                        if (CommUtil.checkIsNull(o.getData())) {
                                            mView.showTasksDetailError();
                                        } else {
                                            if (CommUtil.checkIsNull(o.getData().getCustInfo()) && CommUtil.checkIsNull(o.getData().getMateInfo())) {
                                                mView.showTasksDetailError();
                                            } else {
                                                TaskDetailData data = setCustomInfo(o);
                                                mView.showTaskDetail(data);
                                            }
                                        }
                                    } else {
                                        mView.showTasksDetailError();
                                    }
                                }
                            }

                            @Override
                            protected void _onError() {
                                if (mView.isActive()) {
                                    mView.showTaskDetailIndicator(false);
                                    mView.showTasksDetailError();
                                }
                            }
                        });
                break;
        }

    }

    private TaskDetailData setCallInfo(CallRecord o) {
        TaskDetailData data = new TaskDetailData();
        List<TaskDetailData.TaskSection> taskSectionList = new ArrayList<>();
        TaskDetailData.TaskSection section;
        if (o.getData().getCallRecordData() != null) {
            section = new TaskDetailData.TaskSection();
            List<TaskDetailData.TaskSection.TaskInfo> taskInfoList = new ArrayList<>();
            TaskDetailData.TaskSection.TaskInfo taskInfo;
            List<CallRecord.DataBean.CallRecordDataBean> callRecordDataBeanList = o.getData().getCallRecordData();

            //手动增加催记员 发生时间 备注
            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("催记员");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setCenter("发生日期");
            taskInfo.setCenterColor(R.color.mt9c9c9c);
            taskInfo.setRight("备注");
            taskInfo.setRightColor(R.color.mt9c9c9c);
            taskInfoList.add(taskInfo);

            for (CallRecord.DataBean.CallRecordDataBean bean : callRecordDataBeanList) {
                taskInfo = new TaskDetailData.TaskSection.TaskInfo();
                taskInfo.setLeft(bean.getCallUser());
                taskInfo.setLeftColor(R.color.colorPrimary);
                taskInfo.setCenter(bean.getCallTime());
                taskInfo.setCenterColor(R.color.colorPrimary);
                taskInfo.setRight(bean.getComment());
                taskInfo.setRightColor(R.color.colorPrimary);
                taskInfoList.add(taskInfo);
            }
            section.setIvId(R.drawable.cuijixinxi);
            section.setTitle("催记信息");
            section.setTaskInfoList(taskInfoList);
            taskSectionList.add(section);
        }

        if (o.getData().getOverdueData() != null) {
            section = new TaskDetailData.TaskSection();
            List<TaskDetailData.TaskSection.TaskInfo> taskInfoList = new ArrayList<>();
            TaskDetailData.TaskSection.TaskInfo taskInfo;
            CallRecord.DataBean.OverdueDataBean overdueDataBean = o.getData().getOverdueData();

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("逾期原因");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight(overdueDataBean.getOverdueReason());
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("逾期原因备注");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight(overdueDataBean.getOverdueComment());
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

            section.setIvId(R.drawable.overdue);
            section.setTitle("逾期原因");
            section.setTaskInfoList(taskInfoList);
            taskSectionList.add(section);
        }


        data.setTaskSectionList(taskSectionList);
        return data;
    }

    private TaskDetailData setHomeInfo(HomeVisitRecord o) {
        TaskDetailData data = new TaskDetailData();
        List<TaskDetailData.TaskSection> taskSectionList = new ArrayList<>();
        TaskDetailData.TaskSection section;

        List<HomeVisitRecord.DataBean> dataBeanList = o.getData();
//        for (HomeVisitRecord.DataBean bean : dataBeanList) {
        for (int i=0;i<dataBeanList.size();i++){
            HomeVisitRecord.DataBean bean = dataBeanList.get(i);

            section = new TaskDetailData.TaskSection();
            TaskDetailData.TaskSection.TaskInfo taskInfo;
            List<TaskDetailData.TaskSection.TaskInfo> taskInfoList = new ArrayList<>();

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("家访状态");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight(bean.getVisitStatus());
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("家访日期");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight(bean.getVisitTime());
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("家访员姓名");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight(bean.getVisitUser());
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("家访动作");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight(bean.getVisitAciton());
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("后续建议");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight(bean.getRecommendActions());
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("逾期真实原因");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight(bean.getRedueTrueReason());
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("申请人");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight(bean.getApplyUser());
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

            section.setIvId(R.drawable.home_record);
            section.setTitle("家访记录"+i+1);
            section.setTaskInfoList(taskInfoList);
            taskSectionList.add(section);
        }

        data.setTaskSectionList(taskSectionList);
        return data;
    }

    private TaskDetailData setCaseInfo(CaseInfo o) {
        TaskDetailData data = new TaskDetailData();
        List<TaskDetailData.TaskSection> taskSectionList = new ArrayList<>();
        TaskDetailData.TaskSection section;
        if (o.getData() != null) {
            section = new TaskDetailData.TaskSection();
            CaseInfo.DataBean dataBean = o.getData();
            TaskDetailData.TaskSection.TaskInfo taskInfo;
            List<TaskDetailData.TaskSection.TaskInfo> taskInfoList = new ArrayList<>();

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("申请编号");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight(dataBean.getApplycd());
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("创建日期");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight(dataBean.getApplytime());
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("品牌");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight(dataBean.getCarbrandcg());
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("车型");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight(dataBean.getCarmodelidcg());
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("GPS安装情况");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight(dataBean.getHasgpsflg());
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("GPS是否在线");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight(dataBean.getIsgpsonline());
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("IMEI1");
            taskInfo.setRight(dataBean.getIMEI1());
            taskInfoList.add(taskInfo);

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("IMEI2");
            taskInfo.setRight(dataBean.getIMEI2());
            taskInfoList.add(taskInfo);

            section.setIvId(R.drawable.case_information);
            section.setTitle("案件信息");
            section.setTaskInfoList(taskInfoList);
            taskSectionList.add(section);
        }

        data.setTaskSectionList(taskSectionList);
        return data;
    }

    private TaskDetailData setCustomInfo(CustomerInfo o) {
        TaskDetailData data = new TaskDetailData();
        List<TaskDetailData.TaskSection> taskSectionList = new ArrayList<>();
        TaskDetailData.TaskSection section;
        if (o.getData().getCustInfo() != null) {
            section = new TaskDetailData.TaskSection();
            CustomerInfo.DataBean.CustInfoBean custInfo = o.getData().getCustInfo();
            TaskDetailData.TaskSection.TaskInfo taskInfo;
            List<TaskDetailData.TaskSection.TaskInfo> taskInfoList = new ArrayList<>();

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("姓名");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight(custInfo.getCustomername());
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("性别");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight(custInfo.getSex());
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("证件号码");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight(custInfo.getPaperno());
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("手机");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight(custInfo.getMobile() + "");
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("婚姻状况");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight(custInfo.getMaritalstatus());
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("现居住省");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight(custInfo.getProvince());
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("现居住市");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight(custInfo.getCity());
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("现居住地地址");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight(custInfo.getAddress());
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("现居住地电话区号");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight(custInfo.getTelarea());
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("现居住地电话号码");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight(custInfo.getTel() + "");
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("户籍地址省");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight(custInfo.getResidenceprovince());
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("户籍地址市");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight(custInfo.getResidencecity());
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("户籍地址地址");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight(custInfo.getResidenceaddress());
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("户籍地址电话区号");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight(custInfo.getResidencetelarea());
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("户籍地址电话号码");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight(custInfo.getResidencetel());
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("现工作单位省");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight(custInfo.getWorkprovince());
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("现工作单位市");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight(custInfo.getWorkcity());
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("现工作单位地址");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight(custInfo.getWorkaddress());
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("现工作单位名称");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight(custInfo.getWorkunitname());
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("现工作单位电话区号");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight(custInfo.getResidencetelarea());
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("现工作单位电话号码");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight(custInfo.getWorktel());
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("现工作单位电话分机");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight(custInfo.getWorktelext());
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

            section.setIvId(R.drawable.customer_information_small);
            section.setTitle("客户信息");
            section.setTaskInfoList(taskInfoList);
            taskSectionList.add(section);
        }

        if (o.getData().getMateInfo() != null) {
            section = new TaskDetailData.TaskSection();
            CustomerInfo.DataBean.MateInfoBean mateInfo = o.getData().getMateInfo();
            TaskDetailData.TaskSection.TaskInfo taskInfo;
            List<TaskDetailData.TaskSection.TaskInfo> taskInfoList = new ArrayList<>();

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("配偶姓名");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight(mateInfo.getPartnername());
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("配偶证件号码");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight(mateInfo.getPartnerpaperno());
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

            section.setIvId(R.drawable.spouse_information);
            section.setTitle("配偶信息");
            section.setTaskInfoList(taskInfoList);
            taskSectionList.add(section);
        }
        data.setTaskSectionList(taskSectionList);
        return data;
    }
}
