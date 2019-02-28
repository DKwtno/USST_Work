package com.example.demo.vo.util;

import com.example.demo.vo.project.Project;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Table(name = "t_tag")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "persistentGenerator")
    @GenericGenerator(name = "persistentGenerator",strategy = "increment")
    private Integer tid;
    @NotNull
    @Column(unique = true)
    private String name;
    private int count;
    @ManyToMany(mappedBy = "tagSet", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Project> projectSet;

    public Set<Project> getProjectSet() {
        return projectSet;
    }

    public void setProjectSet(Set<Project> projectSet) {
        this.projectSet = projectSet;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    private Field field;//每个tag专属于一个field

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public Integer getTid() {
        return tid;
    }

    public void setTid(Integer tid) {
        this.tid = tid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
