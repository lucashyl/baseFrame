package com.lucas.admin.util.websocketUtil.center;

import com.lucas.admin.util.websocketUtil.center.impl.TextMessageJob;

/**
 * Created by hyl on 2019/2/14.
 */
public interface MessageHandleErrorCallback {
     void onMessageHandleError(TextMessageJob textMessageJob);
}
