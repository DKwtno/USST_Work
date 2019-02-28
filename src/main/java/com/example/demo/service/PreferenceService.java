package com.example.demo.service;

import com.example.demo.dao.PreferenceDAO;
import com.example.demo.vo.user.Preference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
//用户偏好设置
public class PreferenceService {
    @Autowired
    PreferenceDAO preferenceDAO;
    @Transactional//进行偏好设置
    public void addPreference(Preference preference){
        preferenceDAO.add(preference);
    }
    @Transactional
    public void deletePreference(Preference preference){
        preference = preferenceDAO.getSession().load(Preference.class,preference);
        preferenceDAO.getSession().delete(preference);
    }
    @Transactional
    public List<Preference> getPreference(int uid){
        return preferenceDAO.list("from Preference where uid="+uid);
    }
}
