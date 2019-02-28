package com.example.demo.vo.school;

import com.example.demo.vo.user.Student;
import com.example.demo.vo.user.Teacher;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "t_university")
public class University {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "persistentGenerator")
    @GenericGenerator(name = "persistentGenerator",strategy = "increment")
    private Integer uid;
    private String address;
    private String name;
    @OneToMany(mappedBy = "university")
    private Set<Student> setStudent;
    @OneToMany(mappedBy = "university")
    private Set<Teacher> setTeacher;

    public Set<Student> getSetStudent() {
        return setStudent;
    }

    public void setSetStudent(Set<Student> setStudent) {
        this.setStudent = setStudent;
    }

    public Set<Teacher> getSetTeacher() {
        return setTeacher;
    }

    public void setSetTeacher(Set<Teacher> setTeacher) {
        this.setTeacher = setTeacher;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
