package com.example.demo.vo.project;

import com.example.demo.vo.msg.InviteMsg;
import com.example.demo.vo.user.Student;
import com.example.demo.vo.user.Teacher;
import com.example.demo.vo.user.User;
import com.example.demo.vo.util.Tag;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Set;

@Entity
@Table(name = "t_project")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "persistentGenerator")
    @GenericGenerator(name = "persistentGenerator",strategy = "increment")
    private Integer pid;
    private String name;
    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Student> studentSet;//参加学员
    @ManyToOne
    private Teacher teacher;//指导老师
    @ManyToOne
    private Student captain;//队长
    private int click;//点击
    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<InviteMsg> inviting;//邀请用户
    @Min(0)
    @Max(50)
    private int partnerSize;
    @ManyToMany
    private Set<Tag> tagSet;
    private int curSize;
    @Column(name = "isActivated")
    @Type(type = "yes_no")//数据库中没有boolean
    private Boolean activated;
    @Column(name = "isClosed")
    @Type(type = "yes_no")//数据库中没有boolean
    private Boolean closed = false;
    private String content;
    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Issue> issueSet;
    @ManyToMany(fetch = FetchType.LAZY)
    private Set<User> banIssueUser;

    public Set<User> getBanIssueUser() {
        return banIssueUser;
    }

    public void setBanIssueUser(Set<User> banIssueUser) {
        this.banIssueUser = banIssueUser;
    }

    public Set<Issue> getIssueSet() {
        return issueSet;
    }

    public Boolean getClosed() {
        return closed;
    }

    public void setClosed(Boolean closed) {
        this.closed = closed;
    }

    public void setIssueSet(Set<Issue> issueSet) {
        this.issueSet = issueSet;
    }

    public int getCurSize() {
        return curSize;
    }

    public void setCurSize(int curSize) {
        this.curSize = curSize;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getActivated() {
        return activated;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    public Set<Tag> getTagSet() {
        return tagSet;
    }

    public void setTagSet(Set<Tag> tagSet) {
        this.tagSet = tagSet;
    }

    public int getPartnerSize() {
        return partnerSize;
    }

    public void setPartnerSize(int partnerSize) {
        this.partnerSize = partnerSize;
    }

    @Override
    public boolean equals(Object obj) {
        return this.pid.equals(((Project)obj).getPid());
    }

    @Override
    public int hashCode() {
        return this.pid.hashCode();
    }

    public Set<InviteMsg> getInviting() {
        return inviting;
    }

    public void setInviting(Set<InviteMsg> inviting) {
        this.inviting = inviting;
    }

    public int getClick() {
        return click;
    }

    public void setClick(int click) {
        this.click = click;
    }

    public Student getCaptain() {
        return captain;
    }

    public void setCaptain(Student captain) {
        this.captain = captain;
    }

    public Set<Student> getStudentSet() {
        return studentSet;
    }

    public void setStudentSet(Set<Student> studentSet) {
        this.studentSet = studentSet;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }
}
