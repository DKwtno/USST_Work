package com.example.demo.vo.project;

import com.example.demo.vo.msg.ReplyMsg;
import com.example.demo.vo.user.User;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;

@Entity
@Table(name = "t_issue")
public class Issue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "persistentGenerator")
    @GenericGenerator(name = "persistentGenerator",strategy = "increment")
    private int id;
    private String title;
    private String content;
    @ManyToOne
    private User author;
    @ManyToOne(fetch = FetchType.LAZY)
    private Project project;
    private Timestamp publishTime;
    private Timestamp lastAnswerTime;
    @Column(name = "isSolved")
    @Type(type = "yes_no")//数据库中没有boolean
    private Boolean solved = false;//是否被队长回答
    @OneToMany(mappedBy = "issue", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Reply> replies;
    @OneToMany(mappedBy = "issue", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ReplyMsg> replyMsgSet;
    private int replyNumber;
    private int maxOrder;//最高回复楼层

    public Set<ReplyMsg> getReplyMsgSet() {
        return replyMsgSet;
    }

    public void setReplyMsgSet(Set<ReplyMsg> replyMsgSet) {
        this.replyMsgSet = replyMsgSet;
    }

    public int getMaxOrder() {
        return maxOrder;
    }

    public void setMaxOrder(int maxOrder) {
        this.maxOrder = maxOrder;
    }

    public int getReplyNumber() {
        return replyNumber;
    }

    public void setReplyNumber(int replyNumber) {
        this.replyNumber = replyNumber;
    }

    public Set<Reply> getReplies() {
        return replies;
    }

    public void setReplies(Set<Reply> replies) {
        this.replies = replies;
    }

    public Boolean getSolved() {
        return solved;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSolved(Boolean solved) {
        this.solved = solved;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Timestamp getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Timestamp publishTime) {
        this.publishTime = publishTime;
    }

    public Timestamp getLastAnswerTime() {
        return lastAnswerTime;
    }

    public void setLastAnswerTime(Timestamp lastAnswerTime) {
        this.lastAnswerTime = lastAnswerTime;
    }
}
