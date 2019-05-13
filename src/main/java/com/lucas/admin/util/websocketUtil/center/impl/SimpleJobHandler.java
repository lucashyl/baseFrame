package com.lucas.admin.util.websocketUtil.center.impl;

/**
 * Created by hyl on 2019/2/14.
 */
public class SimpleJobHandler extends AbstractJobHandler {
    private int index;

    private String name;

    public SimpleJobHandler(int index){
        this.index = index;
    }

    public SimpleJobHandler(int index, String name){
        this.index = index;
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void onMessageHandleError(TextMessageJob textMessageJob) {

    }
}
