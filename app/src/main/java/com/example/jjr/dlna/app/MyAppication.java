package com.example.jjr.dlna.app;

import android.app.Application;

import com.dongao.dlna.DLNALibrary;
import com.dongao.dlna.moduls.searchdevice.entity.DeviceDisplay;
import com.dongao.dlna.upnp.UpnpServiceBiz;

import org.fourthline.cling.support.model.item.Item;

/**
 * @author jjr
 * @date 2018/6/19
 */
public class MyAppication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        DLNALibrary.getInstance().init(this);
    }

    @Override
    public void onTerminate() {
        UpnpServiceBiz.newInstance().closeUpnpService(this);
        super.onTerminate();
    }

}
