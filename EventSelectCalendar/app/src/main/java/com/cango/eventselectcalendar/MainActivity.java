package com.cango.eventselectcalendar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    CalendarDialogFragment mCalendarDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void showCalendar(View view){
        showCalendarDialog();
    }

    private void showCalendarDialog() {
        if (CommUtil.checkIsNull(mCalendarDialog)) {
            //TODO test
            int type = 1;
            mCalendarDialog = CalendarDialogFragment.newInstance(type);
            mCalendarDialog.setCalendarDilaogListener(new CalendarDialogFragment.CalendarDilaogListener() {
                @Override
                public void onCalendarClick(Date date, Date date1) {

                }
            });
        }
        if (mCalendarDialog.isVisible()) {

        } else {
            mCalendarDialog.show(getSupportFragmentManager(), "CalendarDialog");
        }
    }

    private void closeCalendarDialog() {
        if (CommUtil.checkIsNull(mCalendarDialog)) {

        } else {
            if (mCalendarDialog.isVisible()) {
                mCalendarDialog.dismiss();
            }
        }
    }
}
