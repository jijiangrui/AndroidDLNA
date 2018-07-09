package com.dongao.dlna.moduls.avtransport.bll;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.dongao.dlna.moduls.avtransport.constants.MediaEventWhat;
import com.dongao.dlna.moduls.avtransport.entity.AVTransportInfo;
import com.dongao.dlna.moduls.avtransport.entity.RenderingControlInfo;
import com.dongao.dlna.moduls.avtransport.event.AVTransportEvent;
import com.dongao.dlna.moduls.avtransport.event.RenderingControlEvent;
import com.dongao.dlna.upnp.UpnpServiceBiz;

import org.fourthline.cling.model.gena.CancelReason;
import org.fourthline.cling.model.gena.GENASubscription;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.model.types.UDAServiceType;

/**
 * @author hubing
 * @version 1.0.0 2015-5-5
 */

public class MediaEventBiz {

	private static String TAG = MediaControlBiz.class.getSimpleName();
	
	private Device device;
	private UpnpServiceBiz upnpBiz;
	protected Handler handler;

	private RenderingControlEvent serviceRCEvent;
	private AVTransportEvent transportEvent;
	
	public MediaEventBiz(Device device, Handler handler) {
		this.device = device;
		this.handler = handler;
		upnpBiz = UpnpServiceBiz.newInstance();
	}
	
	public void addRenderingEvent() {
		Service serviceRC = device.findService(new UDAServiceType("RenderingControl", 1));
		serviceRCEvent = new RenderingControlEvent(serviceRC) {
			
			@Override
			protected void failed(GENASubscription subscription,
                                  UpnpResponse responseStatus, Exception exception, String defaultMsg) {
				Log.d(TAG, "Rendering failed:" + defaultMsg);
			}
			
			@Override
			protected void eventsMissed(GENASubscription subscription,
					int numberOfMissedEvents) {
				Log.d(TAG, "Rendering eventsMissed:" + numberOfMissedEvents);
			}
			
			@Override
			protected void established(GENASubscription subscription) {
				Log.d(TAG, "Rendering established:" + subscription.toString());
			}
			
			@Override
			protected void ended(GENASubscription subscription, CancelReason reason,
                                 UpnpResponse responseStatus) {
				Log.d(TAG, "Rendering ended");
			}
			
			@Override
			public void received(RenderingControlInfo controlInfo) {
				Log.d(TAG, "Rendering received:" + controlInfo.toString());
				Message msg = Message.obtain(handler);
				msg.what = MediaEventWhat.RENDERING_CONTROL;
				msg.obj = controlInfo;
				msg.sendToTarget();
			}
		};
		upnpBiz.execute(serviceRCEvent);
	}
	
	public void removeRenderingEvent() {
		if (serviceRCEvent != null) {
			serviceRCEvent.end();
			Log.d(TAG, "Remove rendering event");
		}
	}
	
	public void addAVTransportEvent() {
		Service serviceAVT = device.findService(new UDAServiceType("AVTransport", 1));
		transportEvent = new AVTransportEvent(serviceAVT) {
			
			@Override
			protected void failed(GENASubscription subscription,
                                  UpnpResponse responseStatus, Exception exception, String defaultMsg) {
				Log.d(TAG, "AVTransport failed:" + defaultMsg);
			}
			
			@Override
			protected void eventsMissed(GENASubscription subscription,
					int numberOfMissedEvents) {
				Log.d(TAG, "AVTransport eventsMissed:" + numberOfMissedEvents);
			}
			
			@Override
			protected void established(GENASubscription subscription) {
				Log.d(TAG, "AVTransport established:" + subscription.toString());
			}
			
			@Override
			protected void ended(GENASubscription subscription, CancelReason reason,
                                 UpnpResponse responseStatus) {
				Log.d(TAG, "AVTransport ended");
			}

			@Override
			public void received(AVTransportInfo avTransportInfo) {
				Log.d(TAG, "AVTransport received:" + avTransportInfo.toString());
				Message msg = Message.obtain(handler);
				msg.what = MediaEventWhat.AV_TRANSPORT;
				msg.obj = avTransportInfo;
				msg.sendToTarget();
			}
		};
		upnpBiz.execute(transportEvent);
	}
	
	public void removeAVTransportEvent() {
		if (transportEvent != null) {
			transportEvent.end();
			Log.d(TAG, "Remove AVTransport event");
		}
	}
	
}

