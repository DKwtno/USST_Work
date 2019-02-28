package com.example.demo.vo.msg;

import com.example.demo.vo.user.User;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Table(name = "t_admin_read")
public class AdminReadMsg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "persistentGenerator")
    @GenericGenerator(name = "persistentGenerator",strategy = "increment")
    private int adminRead;
    @ManyToOne
    private User user;
    @ManyToOne
    private AdminMessage adminMessage;

    @Column(name = "isRead")
    @Type(type = "yes_no")//数据库中没有boolean
    private Boolean read = false;

    public Boolean getRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof AdminMessage)
            return this.adminMessage.equals(obj);
        return this.adminRead == ((AdminReadMsg)obj).getAdminRead();
    }

    @Override
    public int hashCode() {
        return this.adminMessage.hashCode();
    }
    public AdminMessage getAdminMessage() {
        return adminMessage;
    }

    public void setAdminMessage(AdminMessage adminMessage) {
        this.adminMessage = adminMessage;
    }

    public int getAdminRead() {
        return adminRead;
    }

    public void setAdminRead(int adminRead) {
        this.adminRead = adminRead;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
