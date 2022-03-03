package com.juandiegovqdev.flickrandroidapp.utils;

import android.graphics.Point;
import android.view.Display;

public class DeviceData {

    private static DeviceData instance = null;
    private Display display = null;

    private DeviceData() {
    }

    public static DeviceData getInstance() {
        if (instance == null) {
            instance = new DeviceData();
        }
        return instance;
    }

    public void setDisplay(Display display) {
        this.display = display;
    }

    public int getDisplayWidth() {
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }

    public int getDisplayHeight() {
        Point size = new Point();
        display.getSize(size);
        return size.y;
    }

}
