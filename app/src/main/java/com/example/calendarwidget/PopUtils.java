package com.example.calendarwidget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PopUtils {

    /**
     * 初始化时间选择pop
     *
     * @param context {@link Context} 上下文，使用activity
     * @param dismissListener 弹窗消失监听器
     * @param moveBottom 是否默认移动到最底部
     * @return {@link CustomPopuWindow}
     */
    public static CustomPopuWindow initTimePickPop(Context context,
                                            String title, final Date startDate, final Date endDate,
                                            PopupWindow.OnDismissListener dismissListener,
                                            boolean outsideTouchable, MNCalendarVerticalConfig mnCalendarVerticalConfig3,
                                            TimePickCallBack timePickCallBack, boolean moveBottom, int monthCount, boolean timeChooseLimit,
                                            int periodDays, int month_offset) {

        View view = View.inflate(context, R.layout.popu_scheme_starttime, null);
        TextView popTitle = view.findViewById(R.id.popu_title);
        ImageView popCancel = view.findViewById(R.id.pop_cancel);
        TextView popSure = view.findViewById(R.id.tv_sure);
        TextView popStart = view.findViewById(R.id.start);
        TextView popEnd = view.findViewById(R.id.end);
        MNCalendarVertical mnCalendarVertical = view.findViewById(R.id.mnCalendarVertical);

        if(mnCalendarVerticalConfig3 == null){
            mnCalendarVerticalConfig3 = new MNCalendarVerticalConfig.Builder()
                    .setMnCalendar_showLunar(false)
                    .setMnCalendar_colorWeek("#333333")             //星期栏的颜色
                    .setMnCalendar_titleFormat("yyyy年MM月")           //每个月的标题样式
                    .setMnCalendar_colorTitle("#333333")            //每个月标题的颜色
                    .setMnCalendar_colorSolar("#333333")            //阳历的颜色
                    .setMnCalendar_colorStartAndEndBg("#fc7f1a")    //开始结束的背景颜色
                    .setMnCalendar_colorBeforeToday("#BBBBBB")      //今天之前的日期的颜色
                    .setMnCalendar_issingine(false)                  //是否是单选
                    .setMnCalendar_countMonth(monthCount)
                    .setMnCalendar_moveToBottom(moveBottom)
                    .setTimeChoseLimit(timeChooseLimit)//设置是否有重现在起往前推的天数限制
                    .setMonthOffset(month_offset)
                    .setPeriodTime(periodDays)//限制多少天(只有上一个条件是true时生效)
                    .build();
        }

        popStart.setText(dateFormat.format(startDate));

        popSure.setEnabled(false);
        popSure.setClickable(false);
        popSure.setBackgroundResource(R.drawable.time_pick_sure_btn_bg_enabled);
        mnCalendarVertical.setConfig(mnCalendarVerticalConfig3);
//        mnCalendarVertical.setStartDate(startDate);
//        mnCalendarVertical.setEndDate(endDate);

        CustomPopuWindow customPopupWindow =
                new CustomPopuWindow(
                        view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        customPopupWindow.setOutsideTouchable(outsideTouchable);
        customPopupWindow.setAnimationStyle(R.style.pop_bottom_anim);
        customPopupWindow.setBackgroundDrawable(
                ContextCompat.getDrawable(context, android.R.drawable.screen_background_dark_transparent));
        if (dismissListener != null) {
            customPopupWindow.setOnDismissListener(dismissListener);
        }

        popTitle.setText(title);
        popCancel.setOnClickListener(view13 -> {
            customPopupWindow.dismiss();
        });
        LinearLayout layout = view.findViewById(R.id.layout);
        layout.setOnClickListener(view1 -> customPopupWindow.dismiss());

        popSure.setOnClickListener(view12 -> {
            timePickCallBack.onTimeSelectFinish(mnCalendarVertical.getStartDate(), mnCalendarVertical.getEndDate());
            customPopupWindow.dismiss();
        });

        mnCalendarVertical.setOnDateClickEventListener(() -> {
            Date startTime = mnCalendarVertical.getStartDate();
            Date endTime = mnCalendarVertical.getEndDate();

            if(startTime != null && endTime != null){
                popEnd.setText(dateFormat.format(endTime));
                popSure.setEnabled(true);
                popSure.setClickable(true);
                popSure.setBackgroundResource(R.drawable.time_pick_sure_btn_bg);
            }else {
                if(endTime == null){
                    popEnd.setText("");
                }
                popSure.setEnabled(false);
                popSure.setClickable(false);
                popSure.setBackgroundResource(R.drawable.time_pick_sure_btn_bg_enabled);
            }
            popStart.setText(dateFormat.format(startTime));
        });

        return customPopupWindow;
    }



    /**
     * 初始化时间选择pop
     *
     * @param context {@link Context} 上下文，使用activity
     * @param dismissListener 弹窗消失监听器
     * @param moveBottom 是否默认移动到最底部
     * @return {@link CustomPopuWindow}
     */
    public static CustomPopuWindow initTimePickPopSingle(Context context,
                                                   String title, final Date startDate, final Date endDate,
                                                   PopupWindow.OnDismissListener dismissListener,
                                                   boolean outsideTouchable, MNCalendarVerticalConfig mnCalendarVerticalConfig3,
                                                   TimePickCallBack timePickCallBack, boolean moveBottom, int monthCount, boolean timeChooseLimit,
                                                   int periodDays, int month_offset) {

        View view = View.inflate(context, R.layout.popu_scheme_starttime, null);
        TextView popTitle = view.findViewById(R.id.popu_title);
        ImageView popCancel = view.findViewById(R.id.pop_cancel);
        TextView popSure = view.findViewById(R.id.tv_sure);
        view.findViewById(R.id.time_multiple_layout).setVisibility(View.GONE);
        MNCalendarVertical mnCalendarVertical = view.findViewById(R.id.mnCalendarVertical);

        if(mnCalendarVerticalConfig3 == null){
            mnCalendarVerticalConfig3 = new MNCalendarVerticalConfig.Builder()
                    .setMnCalendar_showLunar(false)
                    .setMnCalendar_colorWeek("#333333")             //星期栏的颜色
                    .setMnCalendar_titleFormat("yyyy年MM月")           //每个月的标题样式
                    .setMnCalendar_colorTitle("#333333")            //每个月标题的颜色
                    .setMnCalendar_colorSolar("#333333")            //阳历的颜色
                    .setMnCalendar_colorStartAndEndBg("#fc7f1a")    //开始结束的背景颜色
                    .setMnCalendar_colorBeforeToday("#BBBBBB")      //今天之前的日期的颜色
                    .setMnCalendar_issingine(true)                  //是否是单选
                    .setMnCalendar_countMonth(monthCount)
                    .setMnCalendar_moveToBottom(moveBottom)
                    .setTimeChoseLimit(timeChooseLimit)//设置是否有重现在起往前推的天数限制
                    .setMonthOffset(month_offset)
                    .setPeriodTime(periodDays)//限制多少天(只有上一个条件是true时生效)
                    .build();
        }

        mnCalendarVertical.setConfig(mnCalendarVerticalConfig3);
//        mnCalendarVertical.setStartDate(startDate);
//        mnCalendarVertical.setEndDate(endDate);

        CustomPopuWindow customPopupWindow =
                new CustomPopuWindow(
                        view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        customPopupWindow.setOutsideTouchable(outsideTouchable);
        customPopupWindow.setAnimationStyle(R.style.pop_bottom_anim);
        customPopupWindow.setBackgroundDrawable(
                ContextCompat.getDrawable(context, android.R.drawable.screen_background_dark_transparent));
        if (dismissListener != null) {
            customPopupWindow.setOnDismissListener(dismissListener);
        }

        popTitle.setText(title);
        popCancel.setOnClickListener(view13 -> {
            customPopupWindow.dismiss();
        });
        LinearLayout layout = view.findViewById(R.id.layout);
        layout.setOnClickListener(view1 -> customPopupWindow.dismiss());

        popSure.setOnClickListener(view12 -> {
            timePickCallBack.onTimeSelectFinish(mnCalendarVertical.getStartDate(), mnCalendarVertical.getEndDate());
            customPopupWindow.dismiss();
        });

        return customPopupWindow;
    }

    public static final String DATE_FORMAT = "yyyy-MM-dd";

    private static final SimpleDateFormat dateFormat =
            new SimpleDateFormat(DATE_FORMAT, Locale.CHINA);


    public interface TimePickCallBack{

        void onTimeSelectFinish(Date StartTime, Date EndTime);
    }

    public interface BottomPopCallBack{
        void onBottomItemClick();
    }
}
