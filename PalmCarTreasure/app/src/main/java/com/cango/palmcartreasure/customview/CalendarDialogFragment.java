package com.cango.palmcartreasure.customview;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.cango.palmcartreasure.R;
import com.cango.palmcartreasure.util.ToastUtils;
import com.orhanobut.logger.Logger;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by cango on 2017/4/17.
 */

public class CalendarDialogFragment extends DialogFragment implements OnDateSelectedListener {
    CalendarDay mSelectDay;
    Drawable selectDayEqualToday;
    OneDayDecorator mOneDayDecorator;

    public void setCalendarDilaogListener(CalendarDilaogListener calendarDilaogListener) {
        this.mListener = calendarDilaogListener;
    }

    private CalendarDilaogListener mListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = inflater.inflate(R.layout.calendar_dialog_fragment, container, false);
        Button btnStart = (Button) view.findViewById(R.id.btn_calendar_start);
        selectDayEqualToday = getActivity().getResources().getDrawable(R.drawable.calendar_selector);
        MaterialCalendarView calendarView = (MaterialCalendarView) view.findViewById(R.id.calendarView);

        Calendar instance = Calendar.getInstance();
        instance.set(instance.get(Calendar.YEAR), instance.get(Calendar.MONTH), 1);

        Calendar maxCalendar=Calendar.getInstance();
        maxCalendar.add(Calendar.DAY_OF_MONTH,10);

        calendarView.state().edit()
                .setMinimumDate(instance.getTime())
                .setMaximumDate(maxCalendar)
                .commit();
        mOneDayDecorator = new OneDayDecorator();
        calendarView.addDecorators(
                new TodayDecorator(),
                new MySelectorDecorator(getActivity()),
                new PrimeDayDisableDecorator(),
                mOneDayDecorator
        );
        calendarView.setOnDateChangedListener(this);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectDay!=null){
                    getDialog().dismiss();
                    mListener.onCalendarClick(mSelectDay.getDate());
                }else {
                    ToastUtils.showShort("请选择结束日期");
                }
            }
        });
        return view;
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        mSelectDay = date;
        mOneDayDecorator.setDate(date);
        widget.invalidateDecorators();
    }

    private class MySelectorDecorator implements DayViewDecorator {

        private final Drawable drawable;

        public MySelectorDecorator(Activity context) {
            drawable = context.getResources().getDrawable(R.drawable.calendar_selector);
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return true;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.setSelectionDrawable(drawable);
        }
    }

    private class TodayDecorator implements DayViewDecorator {

        private final CalendarDay today;
        private final Drawable backgroundDrawable;

        public TodayDecorator() {
            today = CalendarDay.today();
            backgroundDrawable = getResources().getDrawable(R.drawable.calendar_start_bg);
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return today.equals(day);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.setBackgroundDrawable(backgroundDrawable);
        }
    }

    private static class PrimeDayDisableDecorator implements DayViewDecorator {

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return day.isBefore(new CalendarDay());
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.setDaysDisabled(true);
        }
    }

    private class OneDayDecorator implements DayViewDecorator {

        private CalendarDay date;

        public OneDayDecorator() {
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return date != null && day.equals(date);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new StyleSpan(Typeface.BOLD));
            view.addSpan(new RelativeSizeSpan(1.2f));
            view.setBackgroundDrawable(selectDayEqualToday);
        }

        /**
         * We're changing the internals, so make sure to call {@linkplain MaterialCalendarView#invalidateDecorators()}
         */
        public void setDate(CalendarDay date) {
            this.date = date;
        }
    }
    public interface CalendarDilaogListener{
        void onCalendarClick(Date date);
    }
}
