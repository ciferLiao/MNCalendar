package com.example.calendarwidget;

import java.util.Date;

/**
 * Created by lenovo on 2018-03-01.
 */

public interface OnCalendarItemClickListener {

    void onClick(Date date);

    void onLongClick(Date date);

}
