package com.example.demo.vo.util;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
@Entity
@Table(name = "t_field")
public class Field {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "persistentGenerator")
    @GenericGenerator(name = "persistentGenerator",strategy = "increment")
    private Integer fid;
    @OneToMany(mappedBy = "field", cascade = {CascadeType.ALL},fetch = FetchType.EAGER)
    private Set<Tag> setTag = new HashSet<>();//每个Field下有多个Tag
    private String name;

    public Set<Tag> getSetTag() {
        return setTag;
    }

    public void setSetTag(Set<Tag> setTag) {
        this.setTag = setTag;
    }

    public Integer getFid() {
        return fid;
    }

    public void setFid(Integer fid) {
        this.fid = fid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
