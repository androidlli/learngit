package com.cango.eventselectcalendar;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

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
    CalendarDay mSelectDay, lastDay;
    //    Drawable selectDayEqualToday;
    SelectorDecorator selectorDecorator;
    StartDayDecorator startDayDecorator;
    CenterDayDecorator centerDayDecorator;
    EndDayDecorator endDayDecorator;
    private CalendarDay leftDay;
    private CalendarDay rightDay;
    private CalendarDay centerDay;
    /**
     * 0:选择一天；1：选择3天；
     */
    private int mType;

    public static CalendarDialogFragment newInstance(int type) {
        CalendarDialogFragment calendarDialogFragment = new CalendarDialogFragment();
        Bundle args = new Bundle();
        args.putInt("type", type);
        calendarDialogFragment.setArguments(args);
        return calendarDialogFragment;
    }

    public void setCalendarDilaogListener(CalendarDilaogListener calendarDilaogListener) {
        this.mListener = calendarDilaogListener;
    }

    private CalendarDilaogListener mListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mType = getArguments().getInt("type");
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = inflater.inflate(R.layout.fragment_calendar_dialog, container, false);
        TextView btnStart = (TextView) view.findViewById(R.id.btn_calendar_start);
//        selectDayEqualToday = getActivity().getResources().getDrawable(R.drawable.calendar_selector);
        MaterialCalendarView calendarView = (MaterialCalendarView) view.findViewById(R.id.calendarView);
        Calendar minCalendar = Calendar.getInstance();
        minCalendar.add(Calendar.DAY_OF_MONTH, -90);
//        minCalendar.set(minCalendar.get(Calendar.YEAR), minCalendar.get(Calendar.MONTH), 1);
        Calendar maxCalendar = Calendar.getInstance();
//        maxCalendar.add(Calendar.DAY_OF_MONTH,10);
        calendarView.state().edit()
//                .setMinimumDate(minCalendar)
                .setMaximumDate(maxCalendar)
                .commit();
        startDayDecorator = new StartDayDecorator();
        centerDayDecorator = new CenterDayDecorator();
        endDayDecorator = new EndDayDecorator();
        selectorDecorator = new SelectorDecorator();
        calendarView.addDecorators(
//                new TodayDecorator(),
//                selectorDecorator,
//                mOneDayDecorator
                startDayDecorator,
                centerDayDecorator,
                endDayDecorator,
                new PrimeDayDisableDecorator()
        );
        calendarView.setOnDateChangedListener(this);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectDay != null) {
                    getDialog().dismiss();
                    if (mType == 0) {
                        mListener.onCalendarClick(mSelectDay.getDate(), null);
                    } else {
                        if (!CommUtil.checkIsNull(mSelectDay)&&CommUtil.checkIsNull(leftDay)&&
                                CommUtil.checkIsNull(rightDay)){
                            mListener.onCalendarClick(mSelectDay.getDate(),null);
                        }else {
                            if (!CommUtil.checkIsNull(leftDay)&&!CommUtil.checkIsNull(rightDay))
                                mListener.onCalendarClick(leftDay.getDate(), rightDay.getDate());
                        }
                    }
                } else {
//                    ToastUtils.showShort("请选择结束日期");
                }
            }
        });
//        calendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_MULTIPLE);
        return view;
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        mSelectDay = date;
        if (mType == 0) {

        } else {
//            if (lastDay != null && lastDay.getDate() != date.getDate()) {
            if (lastDay != null) {
                //加入先选择两个，那么点击两个旁边的也要三个选中
                if (leftDay != null && rightDay != null && centerDay == null) {
                    int left = differentDaysByMillisecond(date.getDate(), leftDay.getDate());
                    int right = differentDaysByMillisecond(date.getDate(), rightDay.getDate());
                    if (date.getDate().getTime() < leftDay.getDate().getTime()) {
                        if (Math.abs(left) == 1) {
                            Calendar leftCalendar = Calendar.getInstance();
                            leftCalendar.setTime(date.getDate());
                            leftDay = new CalendarDay(leftCalendar);
                            Calendar centerCalendar = Calendar.getInstance();
                            centerCalendar.setTime(date.getDate());
                            centerCalendar.add(Calendar.DAY_OF_MONTH, 1);
                            centerDay = new CalendarDay(centerCalendar);
                            startDayDecorator.setDate(leftDay);
                            centerDayDecorator.setDate(centerDay);
                            endDayDecorator.setDate(rightDay);
                            widget.clearAnimation();
                            widget.selectRange(leftDay, rightDay);
                        } else {
                            leftDay = null;
                            centerDay = null;
                            rightDay = null;
                            widget.clearAnimation();
                            widget.setDateSelected(date, true);
                        }
                    } else if (date.getDate().getTime() == leftDay.getDate().getTime()) {
                        leftDay = null;
                        centerDay = null;
                        rightDay = null;
                        widget.clearAnimation();
                        widget.setDateSelected(date, true);
                    } else if (date.getDate().getTime() == rightDay.getDate().getTime()) {
                        leftDay = null;
                        centerDay = null;
                        rightDay = null;
                        widget.clearAnimation();
                        widget.setDateSelected(date, true);
                    } else {
                        if (Math.abs(right) == 1) {
                            Calendar centerCalendar = Calendar.getInstance();
                            centerCalendar.setTime(rightDay.getDate());
                            centerDay = new CalendarDay(centerCalendar);
                            Calendar rightCalendar = Calendar.getInstance();
                            rightCalendar.setTime(date.getDate());
                            rightDay = new CalendarDay(rightCalendar);
                            startDayDecorator.setDate(leftDay);
                            centerDayDecorator.setDate(centerDay);
                            endDayDecorator.setDate(rightDay);
                            widget.clearAnimation();
                            widget.selectRange(leftDay, rightDay);
                        } else {
                            leftDay = null;
                            centerDay = null;
                            rightDay = null;
                            widget.clearAnimation();
                            widget.setDateSelected(date, true);
                        }
                    }

                } else if (leftDay != null && centerDay != null && rightDay != null) {
                    //当三天已经选择出来的话点击任何天数都应该选中当前，也就只有一天显示
                    leftDay = null;
                    centerDay = null;
                    rightDay = null;
                    widget.clearAnimation();
                    widget.setDateSelected(date, true);
                } else {
                    int count = differentDaysByMillisecond(date.getDate(), lastDay.getDate());
                    if (Math.abs(count) > 2) {
                        leftDay = null;
                        centerDay = null;
                        rightDay = null;
                    } else {
                        if (Math.abs(count) == 0) {
                            leftDay = null;
                            centerDay = null;
                            rightDay = null;
                        } else if (Math.abs(count) == 1) {
                            //两个天数
                            if (count > 0) {
                                //后选择的天数大
                                Calendar leftCalendar = Calendar.getInstance();
                                leftCalendar.setTime(lastDay.getDate());
//                            leftCalendar.add(Calendar.DAY_OF_MONTH, -1);
                                leftDay = new CalendarDay(leftCalendar);
                                centerDay = null;
                                Calendar rightCalendar = Calendar.getInstance();
                                rightCalendar.setTime(date.getDate());
//                            rightCalendar.add(Calendar.DAY_OF_MONTH, 1);
                                rightDay = new CalendarDay(rightCalendar);
                                startDayDecorator.setDate(leftDay);
                                endDayDecorator.setDate(rightDay);
                            } else {
                                //后选择的天数小
                                Calendar leftCalendar = Calendar.getInstance();
                                leftCalendar.setTime(date.getDate());
//                            leftCalendar.add(Calendar.DAY_OF_MONTH, -1);
                                leftDay = new CalendarDay(leftCalendar);
                                centerDay = null;
                                Calendar rightCalendar = Calendar.getInstance();
                                rightCalendar.setTime(lastDay.getDate());
//                            rightCalendar.add(Calendar.DAY_OF_MONTH, 1);
                                rightDay = new CalendarDay(rightCalendar);
                                startDayDecorator.setDate(leftDay);
                                endDayDecorator.setDate(rightDay);
                            }
                            widget.clearAnimation();
                            widget.selectRange(leftDay, rightDay);
                        } else {
                            //三个天数
                            if (count > 0) {
                                //后选择的天数大
                                Calendar leftCalendar = Calendar.getInstance();
                                leftCalendar.setTime(lastDay.getDate());
                                leftDay = new CalendarDay(leftCalendar);
                                Calendar centerCalendar = Calendar.getInstance();
                                centerCalendar.setTime(lastDay.getDate());
                                centerCalendar.add(Calendar.DAY_OF_MONTH, 1);
                                centerDay = new CalendarDay(centerCalendar);
                                Calendar rightCalendar = Calendar.getInstance();
                                rightCalendar.setTime(date.getDate());
                                rightDay = new CalendarDay(rightCalendar);
                                startDayDecorator.setDate(leftDay);
                                centerDayDecorator.setDate(centerDay);
                                endDayDecorator.setDate(rightDay);
                            } else {
                                //后选择的天数小
                                Calendar leftCalendar = Calendar.getInstance();
                                leftCalendar.setTime(date.getDate());
                                leftDay = new CalendarDay(leftCalendar);
                                Calendar centerCalendar = Calendar.getInstance();
                                centerCalendar.setTime(date.getDate());
                                centerCalendar.add(Calendar.DAY_OF_MONTH, 1);
                                centerDay = new CalendarDay(centerCalendar);
                                Calendar rightCalendar = Calendar.getInstance();
                                rightCalendar.setTime(lastDay.getDate());
                                rightDay = new CalendarDay(rightCalendar);
                                startDayDecorator.setDate(leftDay);
                                centerDayDecorator.setDate(centerDay);
                                endDayDecorator.setDate(rightDay);
                            }
                            widget.clearAnimation();
                            widget.selectRange(leftDay, rightDay);
                        }
                    }
                }
            }
        }
        widget.invalidateDecorators();
        lastDay = date;
    }

    private class SelectorDecorator implements DayViewDecorator {

        private final Drawable drawable;

        public SelectorDecorator() {
            drawable = getResources().getDrawable(R.drawable.calendar_selector);
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return true;
        }

        @Override
        public void decorate(DayViewFacade view) {
//            view.addSpan(new StyleSpan(Typeface.BOLD));
//            view.addSpan(new RelativeSizeSpan(1.2f));
            view.setSelectionDrawable(drawable);
        }
    }

    private class TodayDecorator implements DayViewDecorator {

        private final CalendarDay today;
        private final Drawable backgroundDrawable;

        public TodayDecorator() {
            today = CalendarDay.today();
//            backgroundDrawable = getResources().getDrawable(R.drawable.calendar_start_bg);
            backgroundDrawable = getResources().getDrawable(R.drawable.circular_arc);
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return today.equals(day);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.setSelectionDrawable(backgroundDrawable);
        }
    }

    private static class PrimeDayDisableDecorator implements DayViewDecorator {

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return day.isAfter(new CalendarDay());
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.setDaysDisabled(true);
        }
    }

    private class StartDayDecorator implements DayViewDecorator {

        private CalendarDay date;

        public StartDayDecorator() {
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            if (mSelectDay != null && leftDay != null && rightDay != null) {
                if (day.getDate().getTime() == leftDay.getDate().getTime()) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public void decorate(DayViewFacade view) {
//            view.addSpan(new StyleSpan(Typeface.BOLD));
//            view.addSpan(new RelativeSizeSpan(1.2f));
            view.setSelectionDrawable(getResources().getDrawable(R.drawable.left));
        }

        /**
         * We're changing the internals, so make sure to call {@linkplain MaterialCalendarView#invalidateDecorators()}
         */
        public void setDate(CalendarDay date) {
            this.date = date;
        }
    }

    private class CenterDayDecorator implements DayViewDecorator {

        private CalendarDay date;

        public CenterDayDecorator() {
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            if (mSelectDay != null && leftDay != null && rightDay != null && centerDay != null) {
                if (day.isInRange(leftDay, rightDay)) {
                    if (day.getDate().getTime() == centerDay.getDate().getTime()) {
                        return true;
                    }
                }
            }
            return false;
        }

        @Override
        public void decorate(DayViewFacade view) {
//            view.addSpan(new StyleSpan(Typeface.BOLD));
//            view.addSpan(new RelativeSizeSpan(1.2f));
            view.setSelectionDrawable(getResources().getDrawable(R.drawable.center));
        }

        /**
         * We're changing the internals, so make sure to call {@linkplain MaterialCalendarView#invalidateDecorators()}
         */
        public void setDate(CalendarDay date) {
            this.date = date;
        }
    }

    private class EndDayDecorator implements DayViewDecorator {

        private CalendarDay date;

        public EndDayDecorator() {
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            if (mSelectDay != null && leftDay != null && rightDay != null) {
                if (day.getDate().getTime() == rightDay.getDate().getTime()) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public void decorate(DayViewFacade view) {
//            view.addSpan(new StyleSpan(Typeface.BOLD));
//            view.addSpan(new RelativeSizeSpan(1.2f));
            view.setSelectionDrawable(getResources().getDrawable(R.drawable.right));
        }

        /**
         * We're changing the internals, so make sure to call {@linkplain MaterialCalendarView#invalidateDecorators()}
         */
        public void setDate(CalendarDay date) {
            this.date = date;
        }
    }

    public interface CalendarDilaogListener {
        void onCalendarClick(Date date, Date date1);
    }

    /**
     * 通过时间秒毫秒数判断两个时间的间隔
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int differentDaysByMillisecond(Date date1, Date date2) {
        int days = (int) ((date1.getTime() - date2.getTime()) / (1000 * 3600 * 24));
        return days;
    }
}
