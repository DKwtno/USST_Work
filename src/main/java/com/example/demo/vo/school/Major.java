package com.example.demo.vo.school;

import com.example.demo.vo.user.Student;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "t_department")
public class Major {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "persistentGenerator")
    @GenericGenerator(name = "persistentGenerator",strategy = "increment")
    private Integer mid;
    private String name;
    @OneToMany(mappedBy = "major")
    private Set<Student> setStudent;

    public Set<Student> getSetStudent() {
        return setStudent;
    }

    public void setSetStudent(Set<Student> setStudent) {
        this.setStudent = setStudent;
    }

    public Integer getMid() {
        return mid;
    }

    public void setMid(Integer mid) {
        this.mid = mid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
