package com.example.calendarwidget;

import android.graphics.Rect;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;

/** Created by FangGang on 2017/9/30. descprition:解决android 7.0以上系统pop显示位置不对的bug */
public class CustomPopuWindow extends PopupWindow {

    /**
     * @param view pop的布局view
     * @param width pop的宽(px)
     * @param height pop的高(px)
     */
    public CustomPopuWindow(View view, int width, int height) {
        super(view, width, height);
    }

    /**
     * 显示在指定位置
     *
     * @param anchor anchor
     */
    public void showAtLocation(View anchor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Rect visibleFrame = new Rect();
            anchor.getGlobalVisibleRect(visibleFrame);
            int max = getMaxAvailableHeight(anchor);
            setHeight(max);
            super.showAtLocation(anchor, Gravity.NO_GRAVITY, 0, visibleFrame.bottom);
        } else if (android.os.Build.VERSION.SDK_INT == android.os.Build.VERSION_CODES.M) {
            int[] location = new int[2];
            anchor.getLocationInWindow(location);
            int y = location[1];
            int max = getMaxAvailableHeight(anchor);
            setHeight(max);
            super.showAtLocation(anchor, Gravity.NO_GRAVITY, 0, y + anchor.getHeight());
        } else {
            showAsDropDown(anchor);
        }
    }
    /**
     * 显示在指定位置
     *
     * @param anchor anchor
     */
    public void showAtLocationWithNoMaxAvailableHeight(View anchor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Rect visibleFrame = new Rect();
            anchor.getGlobalVisibleRect(visibleFrame);
            super.showAtLocation(anchor, Gravity.NO_GRAVITY, 0, visibleFrame.bottom);
        } else if (android.os.Build.VERSION.SDK_INT == android.os.Build.VERSION_CODES.M) {
            int[] location = new int[2];
            anchor.getLocationInWindow(location);
            int y = location[1];
            super.showAtLocation(anchor, Gravity.NO_GRAVITY, 0, y + anchor.getHeight());
        } else {
            showAsDropDown(anchor);
        }
    }
}
