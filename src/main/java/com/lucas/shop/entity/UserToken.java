package com.lucas.shop.entity;

import java.io.Serializable;

/**
 */
public class UserToken implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long memberId;
    private String userName;

    public Long getMemberId() {
        return memberId;
    }

    public UserToken(Long memberId, String userName) {
        this.memberId = memberId;
        this.userName = userName;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}

