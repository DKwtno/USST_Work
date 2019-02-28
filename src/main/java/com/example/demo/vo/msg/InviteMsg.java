package com.example.demo.vo.msg;

import com.example.demo.vo.project.Project;
import com.example.demo.vo.user.User;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "t_inv_apl_msg")
public class InviteMsg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "persistentGenerator")
    @GenericGenerator(name = "persistentGenerator",strategy = "increment")
    private int inid = 0;
    @ManyToOne
    private User invited;
    @ManyToOne
    private Project project;
    private int captainId;
    private Timestamp inviteTime;
    @Column(name = "isRead")
    @Type(type = "yes_no")//数据库中没有boolean
    private Boolean read = false;
    @Column(name = "isAgreed")
    @Type(type = "yes_no")//数据库中没有boolean
    private Boolean agreed;
    private int msgType;//1代表邀请信息，2代表申请信息,3代表项目审核通过消息，4代表审核不通过消息
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public int getCaptainId() {
        return captainId;
    }

    public void setCaptainId(int captainId) {
        this.captainId = captainId;
    }

    public Boolean getAgreed() {
        return agreed;
    }

    public void setAgreed(Boolean agreed) {
        this.agreed = agreed;
    }

    public Boolean getRead() {
        return read;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }

    public int getInid() {
        return inid;
    }

    public void setInid(int inid) {
        this.inid = inid;
    }

    public User getInvited() {
        return invited;
    }

    public void setInvited(User invited) {
        this.invited = invited;
    }


    public Timestamp getInviteTime() {
        return inviteTime;
    }

    public void setInviteTime(Timestamp inviteTime) {
        this.inviteTime = inviteTime;
    }
}
