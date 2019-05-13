package com.lucas.admin.util.websocketUtil.center.impl;

import com.lucas.admin.util.websocketUtil.center.JobHandler;
import com.lucas.admin.util.websocketUtil.center.MessageHandleErrorCallback;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedTransferQueue;

/**
 * Created by hyl on 2019/2/14.
 */
public abstract class AbstractJobHandler implements Runnable, MessageHandleErrorCallback, JobHandler {

    private BlockingQueue<TextMessageJob> jobContainer;

    public AbstractJobHandler() {
        this.jobContainer = new LinkedTransferQueue<>();
    }

    /**
     * 从队列取出消息发送
     */
    @Override
    public void run() {
        while (true) {
            TextMessageJob messageJob = null;
            try {
                messageJob = jobContainer.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(null != messageJob){
                internalSendMessage(messageJob);
            }

        }
    }

    /**
     * 消息放入队列
     * @param messageJob
     */
    @Override
    public void pushMessage(TextMessageJob messageJob) {
        try {
            this.jobContainer.put(messageJob);
        } catch (InterruptedException e) {
            onMessageHandleError(messageJob);
        }
    }

    /**
     * 调用发送消息
     * @param messageJob
     */
    public void internalSendMessage(TextMessageJob messageJob){
        try {
            try {
                messageJob.send(messageJob);
            } catch (IOException e) {
                onMessageHandleError(messageJob);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

}