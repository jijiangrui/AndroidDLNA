package com.dongao.dlna;

import android.app.Application;

import com.dongao.dlna.moduls.searchdevice.entity.DeviceDisplay;

import org.fourthline.cling.support.model.item.Item;

/**
 * @author jjr
 * @date 2018/6/22
 */
public final class DLNALibrary {

    private static DLNALibrary mInstance;
    private static Application mApplication;
    private DeviceDisplay device;
    private Item item;

    private DLNALibrary() {

    }

    public static synchronized DLNALibrary getInstance() {
        if (null == mInstance) {
            mInstance = new DLNALibrary();
        }
        return mInstance;
    }

    public void init(Application application) {
        mApplication = application;
    }

    public Application getApplicationContext() {
        return mApplication;
    }

    public void setDeviceDisplay(DeviceDisplay device) {
        this.device = device;
    }

    public DeviceDisplay getDeviceDisplay() {
        return device;
    }


    public void setItem(Item item) {
        this.item = item;
    }

    public Item getItem() {
        return item;
    }

}
