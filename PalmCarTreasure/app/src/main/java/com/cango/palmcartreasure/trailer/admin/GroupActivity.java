package com.cango.palmcartreasure.trailer.admin;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentTransaction;

import com.cango.palmcartreasure.R;
import com.cango.palmcartreasure.base.BaseActivity;
import com.cango.palmcartreasure.model.Member;
import com.cango.palmcartreasure.util.CommUtil;

import java.util.ArrayList;

public class GroupActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
//        if (Build.VERSION.SDK_INT >= 21) {
//            View decorView = getWindow().getDecorView();
//            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
//            decorView.setSystemUiVisibility(option);
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
//        } else {
//        }
        String type = getIntent().getStringExtra("type");
        GroupFragment groupFragment = (GroupFragment) getSupportFragmentManager().findFragmentById(R.id.fl_group_contains);
        if (CommUtil.checkIsNull(groupFragment)) {
            if (GroupFragment.ADD.equals(type)){
                groupFragment = GroupFragment.newInstance(type);
            }else if (GroupFragment.UPDATE.equals(type)){
                int groupId = getIntent().getIntExtra("groupId",-1);
                String groupName = getIntent().getStringExtra("groupName");
                Member memberLeader = getIntent().getParcelableExtra("memberLeader");
                ArrayList<Member> currentMembers = getIntent().getParcelableArrayListExtra("currentMembers");
                groupFragment=groupFragment.newInstance(type,groupId,groupName, memberLeader,currentMembers);
            }else {

            }
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fl_group_contains, groupFragment);
            transaction.commit();
        }
        GroupPresenter presenter=new GroupPresenter(groupFragment);
    }

}
