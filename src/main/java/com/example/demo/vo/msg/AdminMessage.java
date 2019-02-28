package com.example.demo.vo.msg;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;

@Entity
@Table(name = "t_admin_message")
public class AdminMessage {
    private String content;
    private Timestamp sendTime;
    private String title;
    @OneToMany(mappedBy = "adminMessage", cascade = {CascadeType.ALL})//删除后删除
    //仔细想想用ManyToMany就好了……
    private Set<AdminReadMsg> adminReadMsgSet;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "persistentGenerator")
    @GenericGenerator(name = "persistentGenerator",strategy = "increment")
    private int amid;

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof AdminReadMsg)
            return ((AdminReadMsg) obj).getAdminMessage().equals(this);
        return this.amid == ((AdminMessage)obj).getAmid();
    }

    @Override
    public int hashCode() {
        return this.amid;
    }

    public Set<AdminReadMsg> getAdminReadMsgSet() {
        return adminReadMsgSet;
    }

    public void setAdminReadMsgSet(Set<AdminReadMsg> adminReadMsgSet) {
        this.adminReadMsgSet = adminReadMsgSet;
    }

    public int getAmid() {
        return amid;
    }

    public void setAmid(int amid) {
        this.amid = amid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getSendTime() {
        return sendTime;
    }

    public void setSendTime(Timestamp sendTime) {
        this.sendTime = sendTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
