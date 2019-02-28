package com.example.demo.service;

import com.example.demo.dao.MajorDAO;
import com.example.demo.vo.school.Major;
import com.example.demo.vo.user.Student;
import com.example.demo.vo.util.Pager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MajorService {
    @Autowired
    MajorDAO majorDAO;
    @Transactional
    public void addMajor(Major major){
        majorDAO.add(major);
    }
    @Transactional
    public void deleteMajor(int id){
        Major major = majorDAO.load(id);
        for(Student student:major.getSetStudent())
            student.setMajor(null);
        majorDAO.delete(id);
    }
    @Transactional
    public void updateMajor(Major major){
        majorDAO.update(major);
    }
    @Transactional
    public List<Major> getAllMajors(){
        return majorDAO.listAll();
    }
    @Transactional
    public Pager getByKeyWord(Pager pager, String keyword){
        Map<String, Object> map = new HashMap<>();
        String hql = "from Major where name like '%"+keyword+"%'";
        pager.setHql(hql);
        return majorDAO.pagerff(pager, map);
    }
}
