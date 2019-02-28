package com.example.demo.dao;
import com.example.demo.vo.util.Pager;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

interface BaseDao<T>{
    public Serializable add(T t);
    public void delete(int id);
    public void update(T t);
    public T load(int id);
    public T get(int id);
    public List<T> list(String hql);
    public void executeUpdate(String hql);
    public List<T> listAll();
    public Pager pagerff(Pager p, Map<String, Object> pram);
}