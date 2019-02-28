package com.example.demo.vo.user;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Set;

@Table(name = "t_speciality")
@Entity
public class Speciality {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "persistentGenerator")
    @GenericGenerator(name = "persistentGenerator",strategy = "increment")
    private int sid;
    @ManyToMany(mappedBy = "specialitySet")
    private Set<User> userSet;
    private int count;
    private String name;

    /**
     * @param obj
     * @return 判断两个标签是否相等，只看他的内容是否一致
     */
    @Override
    public boolean equals(Object obj) {
        return this.name.equals(((Speciality)obj).getName());
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public Set<User> getUserSet() {
        return userSet;
    }

    public void setUserSet(Set<User> userSet) {
        this.userSet = userSet;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    /**
     *
     * @param user 持久化对象
     */
    public void addUser(User user){
        this.userSet.add(user);
        this.count = this.userSet.size();
    }

    /**
     *
     * @param user 持久化对象
     */
    public void removeUser(User user){
       this.userSet.remove(user);
       this.count = this.userSet.size();
    }
}
