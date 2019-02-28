package com.example.demo.service;

import com.example.demo.dao.CollegeDAO;
import com.example.demo.vo.school.College;
import com.example.demo.vo.user.Student;
import com.example.demo.vo.user.Teacher;
import com.example.demo.vo.util.Pager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CollegeService {
    @Autowired
    CollegeDAO collegeDAO;
    @Transactional
    public void addCollege(College college){
        collegeDAO.add(college);
    }

    @Transactional
    public void deleteCollege(int id){
        College college = collegeDAO.load(id);
        for(Student student:college.getSetStudent())
            student.setCollege(null);
        for(Teacher teacher:college.getSetTeacher())
            teacher.setCollege(null);
        collegeDAO.delete(id);
    }
    @Transactional
    public Pager getByKeyWord(Pager pager, String keyword){
        Map<String, Object> map = new HashMap<>();
        String hql = "from College where name like '%"+keyword+"%'";
        pager.setHql(hql);
        return collegeDAO.pagerff(pager, map);
    }
    @Transactional
    public void updateCollege(College college){
        collegeDAO.update(college);
    }
    @Transactional
    public List<College> getAllColleges(){
        return collegeDAO.listAll();
    }
}
