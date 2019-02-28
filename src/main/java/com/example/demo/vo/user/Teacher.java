package com.example.demo.vo.user;

import com.example.demo.vo.project.Project;
import com.example.demo.vo.school.College;
import com.example.demo.vo.school.University;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Set;

@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "t_teacher")
public class Teacher {
    @Id
    private Integer uid;

    @ManyToOne(cascade = {CascadeType.ALL},fetch = FetchType.EAGER)
    @JoinColumn(name = "university_id")
    private University university;
    @JoinColumn(name = "college_id")
    @ManyToOne(cascade = {CascadeType.ALL},fetch = FetchType.EAGER)
    private College college;
    @OneToMany(mappedBy = "teacher")
    private Set<Project> projects;

    @Override
    public boolean equals(Object obj) {
        return this.uid.equals(((Teacher)obj).getUid());
    }

    @Override
    public int hashCode() {
        return this.uid.hashCode();
    }
    public Set<Project> getProjects() {
        return projects;
    }

    public void setProjects(Set<Project> projects) {
        this.projects = projects;
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

    public University getUniversity() {
        return university;
    }

    public void setUniversity(University university) {
        this.university = university;
    }
}
