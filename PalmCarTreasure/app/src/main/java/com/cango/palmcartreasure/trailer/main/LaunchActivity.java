package com.cango.palmcartreasure.trailer.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cango.palmcartreasure.R;
import com.cango.palmcartreasure.base.BaseActivity;
import com.cango.palmcartreasure.login.LoginActivity;
import com.rd.Orientation;
import com.rd.PageIndicatorView;

import java.util.ArrayList;
import java.util.List;

public class LaunchActivity extends BaseActivity {

    ViewPager mViewPager;
    PageIndicatorView pageIndicatorView;
    private boolean isLeft = false; // 判断是否滑向左边
    private boolean isRight = false; // 判断是否滑向右边
    private boolean isScrolling = false; // 判断是否滑动中
    private int lastValue = -1; // 最后的位置

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        pageIndicatorView = (PageIndicatorView) findViewById(R.id.pageindicatorview);
        LaunchAdapter adapter = new LaunchAdapter();
        adapter.setData(createPageList());
        mViewPager.setAdapter(adapter);

        pageIndicatorView.setViewPager(mViewPager);
        pageIndicatorView.setOrientation(Orientation.HORIZONTAL);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //pos :当前页面，及你点击滑动的页面
                //arg1:当前页面偏移的百分比
                //arg2:当前页面偏移的像素位置

                if (isScrolling) {
                    if (lastValue > positionOffsetPixels) {
                        // 递减，向右侧滑动
                        isRight = true;
                        isLeft = false;
                    } else if (lastValue < positionOffsetPixels) {
                        // 递减，向右侧滑动
                        isRight = false;
                        isLeft = true;
                    } else if (lastValue == positionOffsetPixels) {
                        isRight = isLeft = false;
                    }
                }


                lastValue = positionOffsetPixels;
            }

            @Override
            public void onPageSelected(int position) {
                if (position==2){
                    pageIndicatorView.setVisibility(View.GONE);
                }else
                    pageIndicatorView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // 有三种状态（0，1，2）。
                // state == 0 表示什么都没做。
                // state == 1 表示正在滑动
                // state == 2 表示滑动完毕了
                if (state == 1) {
                    isScrolling = true;
                } else {
                    isScrolling = false;
                }

                if (state == 2) {
                    isRight = isLeft = false;
                }
            }
        });
    }

    @NonNull
    private List<View> createPageList() {
        List<View> pageList = new ArrayList<>();
        View view_one = LayoutInflater.from(this).inflate(R.layout.start_one, null, false);
        View view_two = LayoutInflater.from(this).inflate(R.layout.start_two, null, false);
        View view_three = LayoutInflater.from(this).inflate(R.layout.start_three, null, false);
        view_three.findViewById(R.id.tv_now).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeBackHelper.forwardAndFinish(LoginActivity.class);
            }
        });
        pageList.add(view_one);
        pageList.add(view_two);
        pageList.add(view_three);
        return pageList;
    }

    class LaunchAdapter extends PagerAdapter {

        private List<View> viewList;

        LaunchAdapter() {
            this.viewList = new ArrayList<>();
        }

        @Override
        public Object instantiateItem(ViewGroup collection, int position) {
            View view = viewList.get(position);
            collection.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup collection, int position, Object view) {
            collection.removeView((View) view);
        }

        @Override
        public int getCount() {
            return viewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        void setData(@Nullable List<View> list) {
            this.viewList.clear();
            if (list != null && !list.isEmpty()) {
                this.viewList.addAll(list);
            }

            notifyDataSetChanged();
        }

        @NonNull
        List<View> getData() {
            if (viewList == null) {
                viewList = new ArrayList<>();
            }

            return viewList;
        }
    }
}
