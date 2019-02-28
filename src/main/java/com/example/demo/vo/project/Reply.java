package com.example.demo.vo.project;

import com.example.demo.vo.msg.ReplyMsg;
import com.example.demo.vo.user.User;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "t_reply")
public class Reply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "persistentGenerator")
    @GenericGenerator(name = "persistentGenerator",strategy = "increment")
    private int rid;
    @ManyToOne
    private Issue issue;//从属于哪个帖子
    @ManyToOne
    private User author;//回复人
    private Timestamp replyTime;
    private String content;
    @ManyToOne
    private User replyTo;
    private int order;//这个回复是第几楼
    @OneToOne(mappedBy = "reply", cascade = CascadeType.ALL)
    private ReplyMsg replyMsg;

    public ReplyMsg getReplyMsg() {
        return replyMsg;
    }

    public void setReplyMsg(ReplyMsg replyMsg) {
        this.replyMsg = replyMsg;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    public User getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(User replyTo) {
        this.replyTo = replyTo;
    }

    public Issue getIssue() {
        return issue;
    }

    public void setIssue(Issue issue) {
        if(this.replyTo==null)
            this.replyTo = issue.getAuthor();
        this.issue = issue;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Timestamp getReplyTime() {
        return replyTime;
    }

    public void setReplyTime(Timestamp replyTime) {
        this.replyTime = replyTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
