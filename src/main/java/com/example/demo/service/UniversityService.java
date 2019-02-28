package com.example.demo.service;

import com.example.demo.dao.UniversityDAO;
import com.example.demo.vo.school.University;
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
public class UniversityService {
    @Autowired
    UniversityDAO universityDAO;
    @Transactional
    public List<University> getAllUniversities(){
        return universityDAO.listAll();
    }
    @Transactional
    //pager里的hql要求写好
    public Pager getByPage(Pager pager){
        String hql = "from University";
        pager.setHql(hql);
        return universityDAO.pagerff(pager,null);
    }
    @Transactional
    public Pager getByKeyWord(Pager pager, String keyword){
        Map<String, Object> map = new HashMap<>();
        String hql = "from University where name like '%"+keyword+"%'";
        pager.setHql(hql);
        return universityDAO.pagerff(pager, map);
    }
    @Transactional
    public void addUniversity(University university){
        universityDAO.add(university);
    }

    @Transactional
    public void deleteUniversity(int id){
        University university = universityDAO.load(id);
        for(Student student:university.getSetStudent()){
            student.setUniversity(null);
        }
        for(Teacher teacher:university.getSetTeacher())
            teacher.setUniversity(null);
        universityDAO.delete(id);
    }
    @Transactional
    public void updateUniversity(University university){
        universityDAO.update(university);
    }

    @Transactional
    public void removeUniversity(University university){
        universityDAO.delete(university.getUid());
    }
}
