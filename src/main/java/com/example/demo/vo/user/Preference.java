package com.example.demo.vo.user;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "t_preference")
public class Preference implements Serializable {
    public static final Integer NOT_SHOW_TALK_MSG = 1,
            NOT_SHOW_ADMIN_MSG = 2, NOT_SHOW_INVITE_MSG = 3,
            NOT_BE_INVITED = 4;
    @Id
    private int uid;
    @Id
    private int preference;


    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getPreference() {
        return preference;
    }

    public void setPreference(int preference) {
        this.preference = preference;
    }
}
