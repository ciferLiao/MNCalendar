package com.example.calendarwidget;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.example.calendarwidget.MNCalendarVerticalAdapter.mPrimaryColor;
import static com.example.calendarwidget.MNCalendarVerticalAdapter.mColor999;
import static com.example.calendarwidget.MNCalendarVerticalAdapter.mWhite;


/**
 * @author lenovo
 * @date 2018-03-01
 */
public class MNCalendarVerticalItemAdapter
        extends RecyclerView.Adapter<MNCalendarVerticalItemAdapter.MyViewHolder> {

    private ArrayList<MNCalendarItemModel> mDatas;

    private LayoutInflater layoutInflater;

    private Context context;

    private Calendar currentCalendar;
    private MNCalendarVerticalAdapter adapter;

    /**
     * 配置信息
     */
    private MNCalendarVerticalConfig mnCalendarVerticalConfig;

    private boolean isSingle = false;

    /**
     * 日历 二级小Item
     *
     */
    MNCalendarVerticalItemAdapter(
            Context context,
            ArrayList<MNCalendarItemModel> mDatas,
            Calendar currentCalendar,
            MNCalendarVerticalAdapter adapter,
            MNCalendarVerticalConfig mnCalendarVerticalConfig) {
        this.context = context;
        this.mDatas = mDatas;
        this.currentCalendar = currentCalendar;
        this.adapter = adapter;
        this.mnCalendarVerticalConfig = mnCalendarVerticalConfig;
        layoutInflater = LayoutInflater.from(this.context);

        isSingle = this.mnCalendarVerticalConfig.isIssingine();
    }

    public void setDatas(ArrayList<MNCalendarItemModel> datas){
        this.mDatas = datas;
    }


    @Override
    public MNCalendarVerticalItemAdapter.MyViewHolder onCreateViewHolder(
            ViewGroup parent, int viewType) {
        View inflate = layoutInflater.inflate(R.layout.mn_item_calendar_vertical_item, parent, false);
        return new MyViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(
            MNCalendarVerticalItemAdapter.MyViewHolder myViewHolder, int position) {
        MNCalendarItemModel mnCalendarItemModel = mDatas.get(position);
        final Date datePosition = mnCalendarItemModel.getDate();
        Lunar lunar = mnCalendarItemModel.getLunar();
        myViewHolder.itemView.setVisibility(View.VISIBLE);
        myViewHolder.tv_small.setVisibility(View.GONE);
        myViewHolder.iv_bg.setVisibility(View.GONE);
        myViewHolder.tv_small.setText("");
        myViewHolder.tvDay.setTextColor(mnCalendarVerticalConfig.getMnCalendar_colorSolar());
        myViewHolder.tvDay.setText(String.valueOf(datePosition.getDate()));
        // 不是本月的隐藏
        Date currentDate = currentCalendar.getTime();
        if (datePosition.getMonth() != currentDate.getMonth() || mDatas.get(position).isHide()) {
            myViewHolder.itemView.setVisibility(View.INVISIBLE);
        }

        // 阴历的显示
        if (mnCalendarVerticalConfig.isMnCalendar_showLunar()) {
            myViewHolder.tv_small.setVisibility(View.VISIBLE);
            String lunarDayString = LunarCalendarUtils.getLunarDayString(lunar.lunarDay);
            myViewHolder.tv_small.setText(lunarDayString);
        } else {
            myViewHolder.tv_small.setVisibility(View.GONE);
        }
        if(datePosition.getTime() > System.currentTimeMillis()){
            mnCalendarItemModel.setClick(false);
            myViewHolder.tvDay.setTextColor(mColor999);
        }

        //时间选择限制
        if(mnCalendarVerticalConfig.isTimeChoseLimit()){
            if(!isInTimePeriodLimit(mnCalendarItemModel)){
                mnCalendarItemModel.setClick(false);
                myViewHolder.tvDay.setTextColor(mColor999);
            }
        }
        //选中范围内的区间的颜色
        if (adapter.PreSelectStartTime != null && adapter.PreSelectEndTime != null) {

            if (datePosition.getTime() > adapter.PreSelectStartTime.getTime() && datePosition.getTime() < adapter.PreSelectEndTime.getTime()) {
                myViewHolder.iv_bg.setVisibility(View.VISIBLE);
                myViewHolder.iv_bg.setBackgroundColor(mPrimaryColor);
                myViewHolder.tv_small.setVisibility(View.GONE);
                myViewHolder.tvDay.setTextColor(mnCalendarVerticalConfig.getMnCalendar_colorRangeText());
                //开始的这一天
                if (isSameDay(adapter.PreSelectStartTime, datePosition)) {
                    myViewHolder.iv_bg.setBackgroundColor(mPrimaryColor);
                    myViewHolder.tvDay.setTextColor(mWhite);
                } else {
                    myViewHolder.tv_small.setVisibility(View.GONE);
                    myViewHolder.tvDay.setTextColor(mWhite);
                }
            }
            //开始当天
            if (isSameDay(adapter.PreSelectStartTime, datePosition)) {
                myViewHolder.iv_bg.setVisibility(View.VISIBLE);
                myViewHolder.tv_small.setVisibility(View.VISIBLE);
                myViewHolder.iv_bg.setBackground(isSameDay(adapter.PreSelectEndTime, adapter.PreSelectStartTime) ?
                        context.getDrawable(R.drawable.app_time_select_item_single_bg) :context.getDrawable(R.drawable.app_time_select_item_left_bg));
                myViewHolder.tvDay.setTextColor(mWhite);
                myViewHolder.tv_small.setText("开始");
            } else if (isSameDay(adapter.PreSelectEndTime, datePosition)) {//结束当天
                myViewHolder.iv_bg.setVisibility(View.VISIBLE);
                myViewHolder.tv_small.setVisibility(View.VISIBLE);
                myViewHolder.iv_bg.setBackground(context.getDrawable(R.drawable.app_time_select_item_right_bg));
//                    myViewHolder.iv_bg.setBackgroundColor(Color.parseColor("#3498db"));
                myViewHolder.tvDay.setTextColor(mWhite);
                myViewHolder.tv_small.setText("结束");
            }
        }
        //从开始选择
        if (adapter.PreSelectStartTime != null && adapter.PreSelectEndTime == null) {
            //开始时间存在，结束时间不存在
            if (adapter.PreSelectStartTime.getTime() == datePosition.getTime()) {
                myViewHolder.iv_bg.setVisibility(View.VISIBLE);
                myViewHolder.iv_bg.setBackground(context.getDrawable(R.drawable.app_time_select_item_single_bg));
                myViewHolder.tvDay.setTextColor(mWhite);
            } else {
                myViewHolder.tv_small.setVisibility(View.GONE);
                myViewHolder.iv_bg.setVisibility(View.INVISIBLE);
            }
        }
        //重新选择成功

        myViewHolder.itemView.setTag(myViewHolder.getLayoutPosition());
        /**
         * 点击时间
         */
        myViewHolder.itemView.setOnClickListener(view -> {
            MNCalendarItemModel mnCalendarItemModel1 = mDatas.get((int) view.getTag());
            if (!mnCalendarItemModel1.isClick()) {
                return;
            }
            Date dateClick = mnCalendarItemModel1.getDate();

            if(adapter.PreSelectStartTime != null && adapter.PreSelectEndTime != null){
                adapter.PreSelectStartTime = null;
                adapter.PreSelectEndTime = null;
            }

            if(adapter.PreSelectStartTime == null  || isSingle){
                adapter.PreSelectStartTime = dateClick;
            }else if(isSingle){

            }else if(adapter.PreSelectStartTime.getTime() < dateClick.getTime()){
                adapter.PreSelectEndTime = dateClick;
            }else{
                adapter.PreSelectEndTime = adapter.PreSelectStartTime;
                adapter.PreSelectStartTime = dateClick;
            }
            adapter.notifyChoose();
            SparseArray<MNCalendarVerticalItemAdapter> childAdapterArray = adapter.getChildAdapterArray();
            if(childAdapterArray != null)
            for (int i = 0; i < childAdapterArray.size(); i++) {
                RecyclerView.Adapter adapter = childAdapterArray.valueAt(i);
                if(adapter != null){
                    adapter.notifyDataSetChanged();
                }
            }
//            adapter.notifyDataSetChanged();
//            notifyDataSetChanged();
        });
    }

    /**
     * 获取给定日期是星期几
     */
    private int getDaysInWeek(Date date) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        int dayOfWeek = instance.get(Calendar.DAY_OF_WEEK);
        return dayOfWeek;
    }

    /**
     * 判断是否是处于限制范围的
     * @return
     */
    private boolean isInTimePeriodLimit(MNCalendarItemModel model){
        boolean result = false;
        if(model != null){
            Date date = model.getDate();
            if(date != null){
                long l = System.currentTimeMillis();
                Date today = new Date(l);
                int i = LunarCalendarUtils.calculateDays(date, today);
                if(date.getTime() < l && i < mnCalendarVerticalConfig.getPeriodDays()){
                    result = true;
                    model.setInTimeLimit(true);
                }
            }
        }
        return result;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvDay;
        private TextView tv_small;
        private ImageView iv_bg;

        MyViewHolder(View itemView) {
            super(itemView);
            tvDay = itemView.findViewById(R.id.tvDay);
            tv_small = itemView.findViewById(R.id.tv_small);
            iv_bg =  itemView.findViewById(R.id.iv_bg);
        }
    }

    public boolean isSameDay(Date date1, Date date2) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date1);
        int dayof1 = instance.get(Calendar.DAY_OF_MONTH);
        instance.setTime(date2);
        int dayof2 = instance.get(Calendar.DAY_OF_MONTH);
        return dayof1 == dayof2 && date1.getMonth() == date2.getMonth();
    }
}
