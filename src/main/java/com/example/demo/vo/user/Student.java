package com.example.demo.vo.user;

import com.example.demo.vo.msg.InviteMsg;
import com.example.demo.vo.project.Project;
import com.example.demo.vo.school.College;
import com.example.demo.vo.school.Major;
import com.example.demo.vo.school.University;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Set;

@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "t_student")
public class Student {
    @Id
    private Integer uid;
    private String name;//真名
    private String identity;//身份证
    private String sno;//学号
    @ManyToOne(fetch = FetchType.EAGER)
    private University university;
    @ManyToOne(fetch = FetchType.EAGER)
    private Major major;
    @ManyToOne(fetch = FetchType.EAGER)
    private College college;
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "studentSet")
    private Set<Project> project;
    @OneToMany(mappedBy = "captain")
    private Set<Project> projectAsCaptain;

    @Override
    public boolean equals(Object obj) {
        return this.uid.equals(((Student)obj).getUid());
    }

    @Override
    public int hashCode() {
        return this.uid.hashCode();
    }

    public Set<Project> getProjectAsCaptain() {
        return projectAsCaptain;
    }

    public void setProjectAsCaptain(Set<Project> projectAsCaptain) {
        this.projectAsCaptain = projectAsCaptain;
    }

    public Set<Project> getProject() {
        return project;
    }

    public void setProject(Set<Project> project) {
        this.project = project;
    }

    public University getUniversity() {
        return university;
    }

    public void setUniversity(University university) {
        this.university = university;
    }

    public Major getMajor() {
        return major;
    }

    public void setMajor(Major major) {
        this.major = major;
    }

    public College getCollege() {
        return college;
    }

    public void setCollege(College college) {
        this.college = college;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

}
