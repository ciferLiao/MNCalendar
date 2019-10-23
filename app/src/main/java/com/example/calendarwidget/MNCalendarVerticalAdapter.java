package com.example.calendarwidget;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by lenovo on 2018-03-01.
 * 日历大Item 中的 某一个月RcyView
 */

public class MNCalendarVerticalAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private HashMap<String, ArrayList<MNCalendarItemModel>> mDatas;

    private LayoutInflater layoutInflater;

    private Context context;

    private Calendar currentCalendar;

    private MNCalendarVerticalConfig mnCalendarVerticalConfig;

    private MNCalendarVertical.DateClickEventCallback mCallback;

    static int mPrimaryColor, mColor999, mWhite;
    private SparseArray<MNCalendarVerticalItemAdapter> mChildAdapterMap;
    private static final int NORMAL_TYPE = 0;
    private static final int EMPTY_TYPE = 1;

    Date PreSelectStartTime = null;
    Date PreSelectEndTime = null;
    SimpleDateFormat sdf;

    MNCalendarVerticalAdapter(Context context, HashMap<String, ArrayList<MNCalendarItemModel>> mDates,
                              Calendar currentCalendar, MNCalendarVerticalConfig mnCalendarVerticalConfig
    ) {
        this.context = context;
        this.mDatas = mDates;
        this.currentCalendar = currentCalendar;
        this.mnCalendarVerticalConfig = mnCalendarVerticalConfig;
        layoutInflater = LayoutInflater.from(this.context);
        sdf = new SimpleDateFormat(mnCalendarVerticalConfig.getMnCalendar_titleFormat(), Locale.CHINA);
        mChildAdapterMap = new SparseArray<>();
        mPrimaryColor = context.getResources().getColor(R.color.colorPrimary);
        mColor999 = context.getResources().getColor(R.color.color_999);
        mWhite = context.getResources().getColor(R.color.white);
    }

    public void setNewTime(Date startDate, Date endDate) {
        this.PreSelectStartTime = startDate;
        this.PreSelectEndTime = endDate;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == NORMAL_TYPE){
            View inflate = layoutInflater.inflate(R.layout.mn_item_calendar_vertical, parent, false);
            return new MyViewHolder(inflate);
        }else {
            View inflate = layoutInflater.inflate(R.layout.mn_item_calendar_empty, parent, false);
            return new EmptyHolder(inflate);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MyViewHolder) {
            MyViewHolder myViewHolder = (MyViewHolder) holder;
            //标题
            Calendar calendarTitle = (Calendar) currentCalendar.clone();
            calendarTitle.add(Calendar.MONTH, position);
            Date titleDate = calendarTitle.getTime();

            myViewHolder.tv_item_title.setText(sdf.format(titleDate));

            //设置标题的颜色
            myViewHolder.tv_item_title.setTextColor(mnCalendarVerticalConfig.getMnCalendar_colorTitle());

            //日期数据
            ArrayList<MNCalendarItemModel> dates = mDatas.get(String.valueOf(position));
            //初始化RecycleerView
//            GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 7);
//            myViewHolder.recyclerViewItem.setLayoutManager(gridLayoutManager);
            MNCalendarVerticalItemAdapter adapter = mChildAdapterMap.get(position);
            if(adapter != null){
                adapter.setDatas(dates);
                myViewHolder.recyclerViewItem.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                adapter.notifyDataSetChanged();
            }else {
                MNCalendarVerticalItemAdapter mnCalendarVerticalItemAdapter = new MNCalendarVerticalItemAdapter(context, dates, calendarTitle, this, mnCalendarVerticalConfig);
                myViewHolder.recyclerViewItem.setAdapter(mnCalendarVerticalItemAdapter);
                mChildAdapterMap.put(position, mnCalendarVerticalItemAdapter);

            }
        }else if(holder instanceof EmptyHolder){

        }
    }

    public void setOnDateChooseEventListener(MNCalendarVertical.DateClickEventCallback callback){
        mCallback = callback;
    }

    public SparseArray<MNCalendarVerticalItemAdapter> getChildAdapterArray() {
        return mChildAdapterMap;
    }

    @Override
    public int getItemCount() {
        return mDatas.size() + 1;
    }


    private static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_item_title;
        private RecyclerView recyclerViewItem;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_item_title = (TextView) itemView.findViewById(R.id.tv_item_title);
            recyclerViewItem = (RecyclerView) itemView.findViewById(R.id.recyclerViewItem);
        }
    }
    private static class EmptyHolder extends RecyclerView.ViewHolder {

        public EmptyHolder(View itemView) {
            super(itemView);
        }
    }

    public OnCalendarRangeChooseListener onCalendarRangeChooseListener;

    public void setOnCalendarRangeChooseListener(OnCalendarRangeChooseListener onCalendarRangeChooseListener) {
        this.onCalendarRangeChooseListener = onCalendarRangeChooseListener;
        notifyDataSetChanged();
    }

    public void notifyChoose() {
       if(mCallback != null){
           mCallback.click();
       }
    }

    public void updateDates(HashMap<String, ArrayList<MNCalendarItemModel>> mDates, Calendar currentCalendar, MNCalendarVerticalConfig mnCalendarVerticalConfig, Date startDate, Date endDate) {
        this.mDatas = mDates;
        this.currentCalendar = currentCalendar;
        this.mnCalendarVerticalConfig = mnCalendarVerticalConfig;
        this.PreSelectEndTime = endDate;
        this.PreSelectStartTime = startDate;
        notifyDataSetChanged();
    }

    public Date getPreSelectEndTime() {
        return PreSelectEndTime;
    }

    public Date getPreSelectStartTime() {
        return PreSelectStartTime;
    }

    @Override
    public int getItemViewType(int position) {
        if(position < mDatas.size()){
            return NORMAL_TYPE;
        }else {
            return EMPTY_TYPE;
        }
    }
}
