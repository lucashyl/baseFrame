package com.lucas.admin.util.websocketUtil.center.impl;

import java.util.stream.IntStream;

/**
 * 总控制，启动线程 分发session
 * Created by hyl on 2019/2/14.
 */
public class JobCenter {
    private static SimpleJobHandler[] jobHandlers;

    private static int capacity;

    private volatile int status;

    private static final int STATUS_NEW = 0;

    private static final int STATUS_ON_EXECUTE = 1;

    public JobCenter(int capacity) {
        this.capacity = capacity;
        status = STATUS_NEW;
        jobHandlers = new SimpleJobHandler[capacity];
    }

    private void initJobHandler(int index) {
        jobHandlers[index] = new SimpleJobHandler(index, "SimpleJobHandler-" + index);
        Thread thread = new Thread(jobHandlers[index]);
        thread.setName(jobHandlers[index].getName());
        thread.start();
    }

    public synchronized void start() {
        if (status == STATUS_NEW) {
            IntStream.range(0, capacity).forEach(this::initJobHandler);
            status = STATUS_ON_EXECUTE;
        } else {
        }
    }

    public static void pushMessage(TextMessageJob message) {
        int index = (Math.abs(message.getSession().getId().hashCode()) % capacity);
        jobHandlers[index].pushMessage(message);
    }


//    //启动
//    public static void start(String[] args){
//        JobCenter jobCenter = new JobCenter(10);
//        jobCenter.start();
//    }

}
