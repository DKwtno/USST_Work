package com.example.demo.service;

import com.example.demo.dao.StudentDAO;
import com.example.demo.vo.user.Student;
import com.example.demo.vo.util.Pager;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class StudentService {
    @Autowired
    StudentDAO studentDAO;
    @Transactional
    public Student getById(int id){
        return studentDAO.load(id);
    }
    @Transactional
    public void updateStudentInfo(Student student){
        studentDAO.update(student);
    }
    @Transactional
    public void addStudent(Student student){
        studentDAO.add(student);
    }
    @Transactional
    public void deleteStudent(int id){
        studentDAO.delete(id);
    }
    @Transactional
    /**
     * params为筛选条件，字符串数组为返回的列，默认全返回
     */
    @SuppressWarnings("Duplicates")
    public Pager getStudentByFilter(Pager pager, Map<String,Object> params, String ... rows){
        StringBuilder hql = new StringBuilder(" from Student ");
        if(rows.length!=0) {
            if(rows.length==1)
                hql = hql.insert(0, "select "+rows[0]);
            else
                hql = hql.insert(0,"select " + StringUtils.join(Arrays.asList(rows), ','));
        }
        if(params!=null && params.size()>0){
            hql = hql.append(" where ");
            for(String s:params.keySet()){
                hql = hql.append(s+" = '"+params.get(s)+"' and ");
            }
            hql.delete(hql.length() - 4,hql.length() - 1);
        }
        pager.setHql(hql.toString());
        return studentDAO.pagerff(pager, null);
    }
}
