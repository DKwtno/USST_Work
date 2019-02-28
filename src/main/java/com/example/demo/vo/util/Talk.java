package com.example.demo.vo.util;

import com.example.demo.vo.msg.Message;

import java.util.List;

public class Talk {
    private List<Message> messages;
    private boolean isBanned;//是否被拉黑？
    private boolean isBanner;//是否拉黑？

    public boolean isBanner() {
        return isBanner;
    }

    public void setBanner(boolean banner) {
        isBanner = banner;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public boolean isBanned() {
        return isBanned;
    }

    public void setBanned(boolean banned) {
        isBanned = banned;
    }
}
