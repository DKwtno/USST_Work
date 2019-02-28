package com.example.demo.service;

import com.example.demo.dao.TagDAO;
import com.example.demo.vo.util.Pager;
import com.example.demo.vo.util.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
//增删改查
public class TagService {
    @Autowired
    TagDAO tagDAO;
    @Transactional
    public void addTag(Tag tag){
        tagDAO.add(tag);
    }

    @Transactional
    public void removeTag(Tag tag){
        tagDAO.delete(tag.getTid());
    }

    @Transactional
    public void updateTag(Tag tag){
        tagDAO.update(tag);
    }

    @Transactional
    public Pager getTag(Pager pager){
        pager.setHql("from Tag");
        return tagDAO.pagerff(pager,null);
    }
}
