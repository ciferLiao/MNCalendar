package com.example.calendarwidget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by lenovo on 2018-03-01.
 */

public class MNCalendarVertical extends LinearLayout {

    private Context context;
    private RecyclerView recyclerViewCalendar;
    private LinearLayout ll_week;
    private TextView tv_week_01;
    private TextView tv_week_02;
    private TextView tv_week_03;
    private TextView tv_week_04;
    private TextView tv_week_05;
    private TextView tv_week_06;
    private TextView tv_week_07;

    private Date startDate;
    private Date endDate;
    private boolean isStartTime;

    private Calendar currentCalendar;

    private MNCalendarVerticalConfig mnCalendarVerticalConfig = new MNCalendarVerticalConfig.Builder().build();
    private MNCalendarVerticalAdapter mnCalendarVerticalAdapter;
    private HashMap<String, ArrayList<MNCalendarItemModel>> dataMap;
    private OnCalendarRangeChooseListener onCalendarRangeChooseListener;
    private DateClickEventCallback mCallback;

    public MNCalendarVertical(Context context) {
        this(context, null);
    }

    public MNCalendarVertical(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MNCalendarVertical(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        setFocusableInTouchMode(true);
        setFocusable(true);
        init();
    }

    public void init() {
        startDate = LunarCalendarUtils.getDateWithNoHMM(Calendar.getInstance().getTimeInMillis());
//        endDate = FunctionUtil.getDateWithNoHMM(FunctionUtil.getOneDayLaterTime(Calendar.getInstance().getTimeInMillis() - 1000));
        initViews();
    }

    public boolean isStartTime() {
        return isStartTime;
    }

    public void setStartTime(boolean startTime) {
        isStartTime = startTime;
    }

    private void initViews() {
        //绑定View
        View.inflate(context, R.layout.mn_layout_calendar_vertical, this);
        recyclerViewCalendar = findViewById(R.id.recyclerViewCalendar);
        ll_week = findViewById(R.id.ll_week);
        tv_week_01 = findViewById(R.id.tv_week_01);
        tv_week_02 = findViewById(R.id.tv_week_02);
        tv_week_03 = findViewById(R.id.tv_week_03);
        tv_week_04 = findViewById(R.id.tv_week_04);
        tv_week_05 = findViewById(R.id.tv_week_05);
        tv_week_06 = findViewById(R.id.tv_week_06);
        tv_week_07 = findViewById(R.id.tv_week_07);

        //初始化RecycleerView
        recyclerViewCalendar.setLayoutManager(new LinearLayoutManager(context));
    }


    private void initCalendarDatas() {
        currentCalendar = Calendar.getInstance();
        currentCalendar.add(Calendar.MONTH, -1 * (mnCalendarVerticalConfig.getMnCalendar_countMonth_offset()));
        //星期栏的显示和隐藏
        boolean mnCalendar_showWeek = mnCalendarVerticalConfig.isMnCalendar_showWeek();
        if (mnCalendar_showWeek) {
            ll_week.setVisibility(View.VISIBLE);
            tv_week_01.setTextColor(mnCalendarVerticalConfig.getMnCalendar_colorWeek());
            tv_week_02.setTextColor(mnCalendarVerticalConfig.getMnCalendar_colorWeek());
            tv_week_03.setTextColor(mnCalendarVerticalConfig.getMnCalendar_colorWeek());
            tv_week_04.setTextColor(mnCalendarVerticalConfig.getMnCalendar_colorWeek());
            tv_week_05.setTextColor(mnCalendarVerticalConfig.getMnCalendar_colorWeek());
            tv_week_06.setTextColor(mnCalendarVerticalConfig.getMnCalendar_colorWeek());
            tv_week_07.setTextColor(mnCalendarVerticalConfig.getMnCalendar_colorWeek());
        } else {
            ll_week.setVisibility(View.GONE);
        }

        //日期集合
        dataMap = new HashMap<>();

        int mnCalendar_countMonth = mnCalendarVerticalConfig.getMnCalendar_countMonth();
        Calendar calendar;
//        int value = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);//获取这个月最后一天的值
//        Calendar instance = Calendar.getInstance();
//        int today = instance.get(Calendar.DAY_OF_MONTH);
//        currentCalendar.add(Calendar.MONTH, -mnCalendar_countMonth + 1);
        //计算日期
        for (int i = 0; i < mnCalendar_countMonth; i++) {
            int count7 = 0;
            ArrayList<MNCalendarItemModel> mDatas = new ArrayList<>();
            calendar = (Calendar) currentCalendar.clone();
            calendar.add(Calendar.MONTH, i);
            //置于当月的第一天
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            //获取当月第一天是星期几
            int day_of_week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
            //移动到需要绘制的第一天
            calendar.add(Calendar.DAY_OF_MONTH, -day_of_week);

            while (mDatas.size() < 6 * 7) {
                Date date = LunarCalendarUtils.getDateWithNoHMM(calendar.getTimeInMillis()) ;
                //阴历计算
                Lunar lunar = null;
                if(mnCalendarVerticalConfig.isMnCalendar_showLunar()){
                    lunar =  LunarCalendarUtils.solarToLunar(date);
                }
                mDatas.add(new MNCalendarItemModel(date, lunar));
                //包含两个7就多了一行

                if (String.valueOf(calendar.getTime().getDate()).equals("7")) {
                    count7++;
                }
                //向前移动一天
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }

            if (count7 >= 2) {
                ArrayList<MNCalendarItemModel> mDatas2 = new ArrayList<>();
                for (int j = 0; j < mDatas.size() - 7; j++) {
                    mDatas2.add(mDatas.get(j));
                }
                mDatas = new ArrayList<>(mDatas2);
            }

            dataMap.put(String.valueOf(i), mDatas);

        }
        //设置Adapter
        initAdapter();
    }

    public Date getStartDate() {
        return mnCalendarVerticalAdapter.PreSelectStartTime;
    }

    public Date getEndDate() {
        return mnCalendarVerticalAdapter.PreSelectEndTime;
    }


    private void initAdapter() {
        if (mnCalendarVerticalAdapter == null) {
            mnCalendarVerticalAdapter = new MNCalendarVerticalAdapter(context, dataMap, currentCalendar, mnCalendarVerticalConfig);
            mnCalendarVerticalAdapter.setNewTime(startDate, endDate);
            recyclerViewCalendar.setItemViewCacheSize(mnCalendarVerticalConfig.getMnCalendar_countMonth());
            recyclerViewCalendar.setAdapter(mnCalendarVerticalAdapter);
//            recyclerViewCalendar.setItemViewCacheSize(mnCalendarVerticalConfig.getMnCalendar_countMonth() + 2);
            if(mnCalendarVerticalConfig.isMoveToBottom()){
                recyclerViewCalendar.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recyclerViewCalendar.scrollToPosition(mnCalendarVerticalAdapter.getItemCount()-1);
//                        recyclerViewCalendar.scrollBy(0, 500);
                    }
                }, 100);
            }
        } else {
            mnCalendarVerticalAdapter.updateDates(dataMap, currentCalendar, mnCalendarVerticalConfig, startDate, endDate);
        }
        if (onCalendarRangeChooseListener != null) {
            mnCalendarVerticalAdapter.setOnCalendarRangeChooseListener(onCalendarRangeChooseListener);
        }
    }

    public void setOnDateClickEventListener(DateClickEventCallback callback){
        if(mnCalendarVerticalAdapter != null){
            mnCalendarVerticalAdapter.setOnDateChooseEventListener(callback);
        }
    }

    public void setStartDate(Date date) {
        startDate = date;
        initCalendarDatas();
    }

    public void setEndDate(Date date) {
        endDate = date;
        initCalendarDatas();
    }

    /**
     * 设置配置文件
     *
     * @param config
     */
    public void setConfig(MNCalendarVerticalConfig config) {
        this.mnCalendarVerticalConfig = config;
        initCalendarDatas();
    }

    public interface DateClickEventCallback{
        void click();
    }
}