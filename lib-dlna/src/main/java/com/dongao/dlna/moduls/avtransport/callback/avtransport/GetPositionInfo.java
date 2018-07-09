package com.dongao.dlna.moduls.avtransport.callback.avtransport;

import com.dongao.dlna.moduls.avtransport.entity.PositionInfo;
import com.dongao.dlna.upnp.UpnpActionCallback;

import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.model.types.InvalidValueException;
import org.fourthline.cling.model.types.UnsignedIntegerFourBytes;

import java.util.Map;

/**
 * @author hubing
 * @version 1.0.0 2015-4-28
 */

public abstract class GetPositionInfo extends UpnpActionCallback {

	private static String TAG = GetPositionInfo.class.getSimpleName();

	public GetPositionInfo(UnsignedIntegerFourBytes instanceId,
			Service service) {
		super(new ActionInvocation(service.getAction("GetPositionInfo")));
		try {
			setInput("InstanceID", instanceId);
		} catch (InvalidValueException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void received(ActionInvocation invocation, Map<String, Object> result) {
		PositionInfo positionInfo = new PositionInfo(result);
		onSuccess(positionInfo);
	}

	public abstract void onSuccess(PositionInfo positionInfo);

}
