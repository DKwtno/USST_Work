package com.example.demo.vo.msg;

import com.example.demo.vo.user.User;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "t_message",
        indexes = {@Index(name = "msg_send", columnList = "sender_uid"),
        @Index(name = "msg_receive", columnList = "receiver_uid")})
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "persistentGenerator")
    @GenericGenerator(name = "persistentGenerator",strategy = "increment")
    private int mid;
    private String content;
    @ManyToOne(fetch = FetchType.EAGER)
    private User sender;
    @ManyToOne(fetch = FetchType.EAGER)
    private User receiver;
    private Timestamp sendTime;
    private int talkId;
    @Column(name = "isRead")
    @Type(type = "yes_no")//数据库中没有boolean
    private Boolean read = false;

    public Boolean isRead() {
        return read;
    }

    public void setRead(Boolean read) {
        if(read!=null)
            this.read = read;
    }

    public int getTalkId() {
        return talkId;
    }

    public void setTalkId(int talkId) {
        this.talkId = talkId;
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public Timestamp getSendTime() {
        return sendTime;
    }

    public void setSendTime(Timestamp sendTime) {
        this.sendTime = sendTime;
    }
}
