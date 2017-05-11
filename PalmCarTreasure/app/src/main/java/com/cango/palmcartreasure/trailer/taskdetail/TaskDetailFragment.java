package com.cango.palmcartreasure.trailer.taskdetail;


import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cango.palmcartreasure.R;
import com.cango.palmcartreasure.base.BaseFragment;
import com.cango.palmcartreasure.util.BarUtil;
import com.cango.palmcartreasure.util.ScreenUtil;
import com.cango.palmcartreasure.util.SizeUtil;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

public class TaskDetailFragment extends BaseFragment implements TaskDetailContract.View {
    public static final String ID = "taskId";
    @BindView(R.id.toolbar_detail)
    Toolbar mToolbar;
    @BindView(R.id.tv_download)
    TextView tvDownload;
    @BindView(R.id.recyclerView_detail)
    RecyclerView mRecyclerView;
    @BindView(R.id.rl_shadow)
    RelativeLayout rlShadow;

    @OnClick({R.id.tv_download})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_download:
                rlShadow.setVisibility(View.VISIBLE);
                downloadPW.update();
                downloadPW.showAsDropDown(mToolbar);
                break;
        }
    }
    private int mId;
    private TaskDetailActivity mActivity;
    private TaskDetailContract.Presenter mPresenter;
    private SectionedRecyclerViewAdapter mAdapter;
    private PopupWindow downloadPW;

    public TaskDetailFragment() {
    }

    public static TaskDetailFragment newInstance(int id) {
        TaskDetailFragment fragment = new TaskDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int initLayoutId() {
        return R.layout.fragment_task_detail;
    }

    @Override
    protected void initView() {
        int statusBarHeight = BarUtil.getStatusBarHeight(getActivity());
        int actionBarHeight = BarUtil.getActionBarHeight(getActivity());
        if (Build.VERSION.SDK_INT >= 21) {
            AppBarLayout.LayoutParams layoutParams = new AppBarLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight + actionBarHeight);
            mToolbar.setLayoutParams(layoutParams);
            mToolbar.setPadding(0, statusBarHeight, 0, 0);
        }
        mActivity = (TaskDetailActivity) getActivity();
        mActivity.setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.onBackPressed();
            }
        });
        mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mActivity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        mActivity.getSupportActionBar().setHomeButtonEnabled(true);
        mActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);

        // Set up the white button on the lower right corner
        // more or less with default parameter
        final ImageView fabIconNew = new ImageView(mActivity);
        fabIconNew.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_new_light));
        final FloatingActionButton rightLowerButton = new FloatingActionButton.Builder(mActivity)
                .setContentView(fabIconNew)
                .build();

        SubActionButton.Builder rLSubBuilder = new SubActionButton.Builder(mActivity);
        ImageView rlIcon1 = new ImageView(mActivity);
        ImageView rlIcon2 = new ImageView(mActivity);
        ImageView rlIcon3 = new ImageView(mActivity);
        ImageView rlIcon4 = new ImageView(mActivity);

        rlIcon1.setImageDrawable(getResources().getDrawable(R.drawable.start_task));
        rlIcon2.setImageDrawable(getResources().getDrawable(R.drawable.trailer_navigation));
        rlIcon3.setImageDrawable(getResources().getDrawable(R.drawable.dot_trailer));
        rlIcon4.setImageDrawable(getResources().getDrawable(R.drawable.send_car_library));

        FrameLayout.LayoutParams subParams=new FrameLayout.LayoutParams(SizeUtil.dp2px(mActivity,50),SizeUtil.dp2px(mActivity,50));
        // Build the menu with default options: light theme, 90 degrees, 72dp radius.
        // Set 4 default SubActionButtons
        final FloatingActionMenu rightLowerMenu = new FloatingActionMenu.Builder(mActivity)
                .addSubActionView(rLSubBuilder.setContentView(rlIcon1).setLayoutParams(subParams).build())
                .addSubActionView(rLSubBuilder.setContentView(rlIcon2).setLayoutParams(subParams).build())
                .addSubActionView(rLSubBuilder.setContentView(rlIcon3).setLayoutParams(subParams).build())
                .addSubActionView(rLSubBuilder.setContentView(rlIcon4).setLayoutParams(subParams).build())
                .setRadius(ScreenUtil.getScreenWidth(mActivity)/3)
                .attachTo(rightLowerButton)
                .build();

        // Listen menu open and close events to animate the button content view
        rightLowerMenu.setStateChangeListener(new FloatingActionMenu.MenuStateChangeListener() {
            @Override
            public void onMenuOpened(FloatingActionMenu menu) {
                // Rotate the icon of rightLowerButton 45 degrees clockwise
                fabIconNew.setRotation(0);
                PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, 45);
                ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(fabIconNew, pvhR);
                animation.start();
            }

            @Override
            public void onMenuClosed(FloatingActionMenu menu) {
                // Rotate the icon of rightLowerButton 45 degrees counter-clockwise
                fabIconNew.setRotation(45);
                PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, 0);
                ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(fabIconNew, pvhR);
                animation.start();
            }
        });

        downloadPW=getPopupWindow(getActivity(),R.layout.task_download);
    }

    @Override
    protected void initData() {
        if (getArguments() != null) {
            mId = getArguments().getInt(ID);
        }
        mAdapter = new SectionedRecyclerViewAdapter();
        for (char alphabet = 'A'; alphabet <= 'Z'; alphabet++) {
            List<String> contacts = getContactsWithLetter(alphabet);

            if (contacts.size() > 0) {
                mAdapter.addSection(new ExpandableContactsSection(String.valueOf(alphabet), contacts));
            }
        }
    }

    private List<String> getContactsWithLetter(char letter) {
        List<String> contacts = new ArrayList<>();

        for (String contact : getResources().getStringArray(R.array.names)) {
            if (contact.charAt(0) == letter) {
                contacts.add(contact);
            }
        }

        return contacts;
    }

    @Override
    public void setPresenter(TaskDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showTaskDetailIndicator(boolean active) {

    }

    @Override
    public void showTasksDetailError() {

    }

    @Override
    public void showTaskDetail(List<String> tasks) {

    }

    @Override
    public void showNoTaskDetail() {

    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    public PopupWindow getPopupWindow(Context context, final int layoutId) {
        View popupView = LayoutInflater.from(context).inflate(layoutId, null);
        PopupWindow popupWindow = new PopupWindow(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setContentView(popupView);
        // TODO: 2016/5/17 设置动画
        // TODO: 2016/5/17 设置背景颜色
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#36000000")));
        // TODO: 2016/5/17 设置可以获取焦点
        popupWindow.setFocusable(true);
        // TODO: 2016/5/17 设置可以触摸弹出框以外的区域
        popupWindow.setOutsideTouchable(true);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                rlShadow.setVisibility(View.GONE);
            }
        });
        // TODO：更新popupwindow的状态
        popupWindow.update();
        // TODO: 2016/5/17 以下拉的方式显示，并且可以设置显示的位置
        return popupWindow;
    }

    class ExpandableContactsSection extends StatelessSection {

        String title;
        List<String> list;
        boolean expanded = true;

        public ExpandableContactsSection(String title, List<String> list) {
            super(R.layout.section_ex4_header, R.layout.section_ex4_item);
            this.title = title;
            this.list = list;
        }

        @Override
        public int getContentItemsTotal() {
            return expanded ? list.size() : 0;
        }

        @Override
        public RecyclerView.ViewHolder getItemViewHolder(View view) {
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
            final ItemViewHolder itemHolder = (ItemViewHolder) holder;
            if (title.equals("A")){
                itemHolder.llTime.setVisibility(View.VISIBLE);
            }else {
                itemHolder.llTime.setVisibility(View.GONE);
            }
            itemHolder.rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), String.format("Clicked on position #%s of Section %s", mAdapter.getPositionInSection(itemHolder.getAdapterPosition()), title), Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
            return new HeaderViewHolder(view);
        }

        @Override
        public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
            final HeaderViewHolder headerHolder = (HeaderViewHolder) holder;

            headerHolder.tvTitle.setText(title);

            headerHolder.rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    expanded = !expanded;
                    mAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {

        private final View rootView;
        private final TextView tvTitle;

        public HeaderViewHolder(View view) {
            super(view);
            rootView = view;
            tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private final View rootView;
        private final TextView tvExplain;
        private final LinearLayout llTime;
        private final TextView tvTime;
        private final TextView tvContent;
        public ItemViewHolder(View view) {
            super(view);
            rootView = view;
            tvExplain= (TextView) rootView.findViewById(R.id.tv_task_detail_explain);
            llTime= (LinearLayout) rootView.findViewById(R.id.ll_task_detail_time);
            tvTime= (TextView) rootView.findViewById(R.id.tv_task_detail_time);
            tvContent= (TextView) rootView.findViewById(R.id.tv_task_detail_content);
        }
    }
}
