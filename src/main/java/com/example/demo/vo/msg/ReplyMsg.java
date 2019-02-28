package com.example.demo.vo.msg;

import com.example.demo.vo.project.Issue;
import com.example.demo.vo.project.Reply;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Table(name = "t_reply_msg")
/**
 * 暂时没想清楚生成一个replyMsg类和直接在rely里设置isRead的区别，似乎那样做更简单<br>
 * 就当是为了之后可扩展性吧
 */
public class ReplyMsg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "persistentGenerator")
    @GenericGenerator(name = "persistentGenerator",strategy = "increment")
    private int rid;
    @OneToOne
    private Reply reply;
    @Column(name = "isRead")
    @Type(type = "yes_no")//数据库中没有boolean
    private Boolean read = false;
    private int replyTo;
    @ManyToOne
    private Issue issue;
    private int msgType = 1;

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public Issue getIssue() {
        return issue;
    }
    public ReplyMsg(Issue issue, int replyTo){
        this.issue = issue;
        this.replyTo = replyTo;
    }
    public void setIssue(Issue issue) {
        this.issue = issue;
    }

    public int getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(int replyTo) {
        this.replyTo = replyTo;
    }

    public ReplyMsg(Reply reply){
        this.reply = reply;
        this.replyTo = reply.getReplyTo().getUid();
    }
    public Boolean getRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    public Reply getReply() {
        return reply;
    }

    public void setReply(Reply reply) {
        this.reply = reply;
    }
}
