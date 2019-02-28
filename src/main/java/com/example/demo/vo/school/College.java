package com.example.demo.vo.school;

import com.example.demo.vo.user.Student;
import com.example.demo.vo.user.Teacher;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "t_college")
/**
 * 学院
 */
public class College {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "persistentGenerator")
    @GenericGenerator(name = "persistentGenerator",strategy = "increment")
    private Integer cid;
    private String name;
    @OneToMany(mappedBy = "college")
    private Set<Student> setStudent;
    @OneToMany(mappedBy = "college")
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

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
