package com.example.demo.vo.user;

import com.example.demo.vo.msg.AdminReadMsg;
import com.example.demo.vo.msg.InviteMsg;
import com.example.demo.vo.msg.Message;
import com.example.demo.vo.project.Issue;
import com.example.demo.vo.project.Project;
import com.example.demo.vo.project.Reply;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

/**
 * 用手机号或邮箱登录，要求注册的时候可以通过手机号或邮箱注册
 * 发送激活码/激活邮件
 */

@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "t_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "persistentGenerator")
    @GenericGenerator(name = "persistentGenerator",strategy = "increment")
    @Column(updatable=false)
    private Integer uid;
    @NotNull
    private String password;
    private String telephone;
    private String email;
    @ColumnDefault("1")
    @Column(updatable=false)
    private Integer role;//0代表管理员,1代表学生,2代表老师
    @Temporal(value = TemporalType.DATE)
    @Column(updatable=false)
    private Date createDate;//时间戳方便存储，到2038年32位用尽
    private String imageUrl;//头像本地路径
    @OneToMany(mappedBy = "receiver", fetch = FetchType.LAZY)
    private Set<Message> received;
    @OneToMany(mappedBy = "sender", fetch = FetchType.LAZY)
    private Set<Message> sent;
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<AdminReadMsg> adminMessages;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "ban_banned")
    private Set<User> banner;
    @ManyToMany(mappedBy = "banner", fetch = FetchType.LAZY)
    private Set<User> banned;//banned和banner应该是相反的
    @OneToMany(mappedBy = "invited")
    private Set<InviteMsg> inviteMsgs;
    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Issue> issueSet;
    @ManyToMany
    private Set<Speciality> specialitySet;
    @Column(name = "isActivated")
    @Type(type = "yes_no")//数据库中没有boolean
    private Boolean activated = true;//是否能使用
    @ManyToMany(mappedBy = "banIssueUser")
    private Set<Project> beBanned;
    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Reply> replies;//回复别人的记录
    @OneToMany(mappedBy = "replyTo", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Reply> replied;//被别人回复的记录

    public Set<Reply> getReplies() {
        return replies;
    }

    public void setReplies(Set<Reply> replies) {
        this.replies = replies;
    }

    public Set<Reply> getReplied() {
        return replied;
    }

    public Set<Speciality> getSpecialitySet() {
        return specialitySet;
    }

    public void setSpecialitySet(Set<Speciality> specialitySet) {
        this.specialitySet = specialitySet;
    }

    public void setReplied(Set<Reply> replied) {
        this.replied = replied;
    }

    public Set<Project> getBeBanned() {
        return beBanned;
    }

    public void setBeBanned(Set<Project> beBanned) {
        this.beBanned = beBanned;
    }

    public Boolean getActivated() {
        return activated;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    public Set<Issue> getIssueSet() {
        return issueSet;
    }

    public void setIssueSet(Set<Issue> issueSet) {
        this.issueSet = issueSet;
    }

    public Set<InviteMsg> getInviteMsgs() {
        return inviteMsgs;
    }

    public void setInviteMsgs(Set<InviteMsg> inviteMsgs) {
        this.inviteMsgs = inviteMsgs;
    }

    @Override
    public boolean equals(Object obj) {
        return this.uid.equals(((User)obj).getUid());
    }

    @Override
    public int hashCode() {
        return this.uid.hashCode();
    }
    public Set<User> getBanner() {
        return banner;
    }

    public void setBanner(Set<User> banner) {
        this.banner = banner;
    }

    public Set<User> getBanned() {
        return banned;
    }

    public void setBanned(Set<User> banned) {
        this.banned = banned;
    }

    public Set<AdminReadMsg> getAdminMessages() {
        return adminMessages;
    }

    public void setAdminMessages(Set<AdminReadMsg> adminMessages) {
        this.adminMessages = adminMessages;
    }

    public Set<Message> getReceived() {
        return received;
    }

    public void setReceived(Set<Message> received) {
        this.received = received;
    }

    public Set<Message> getSent() {
        return sent;
    }

    public void setSent(Set<Message> sent) {
        this.sent = sent;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}
