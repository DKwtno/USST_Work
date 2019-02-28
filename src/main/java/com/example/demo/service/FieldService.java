package com.example.demo.service;

import com.example.demo.dao.FieldDAO;
import com.example.demo.vo.util.Field;
import com.example.demo.vo.util.Pager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FieldService {
    @Autowired
    FieldDAO fieldDAO;
    @Transactional
    public void addField(Field field){
        fieldDAO.add(field);
    }
    @Transactional
    public void removeField(Field field){
        fieldDAO.delete(field.getFid());
    }
    @Transactional
    public void updateField(Field field){
        fieldDAO.update(field);
    }
    @Transactional
    public Pager getField(Pager pager){
        pager.setHql("from Field");
        return fieldDAO.pagerff(pager,null );
    }
}
