package com.example.demo.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;

import com.example.demo.vo.util.Pager;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class BaseDAOImpl<T> extends HibernateDaoSupport implements BaseDao<T>
{

    @Autowired
    EntityManager entityManager;
    @Autowired
    public void setSuperSessionFactory(EntityManagerFactory entityManagerFactory) {
        SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
        super.setSessionFactory(sessionFactory);
    }

    //获取Session
    public Session getSession() {
        return entityManager.unwrap(Session.class);
    }


    /**
     * 获取泛型的类
     */
    private Class<T> clz;
    @SuppressWarnings("unchecked")
    public Class<T> getClz() {
        if (clz == null) {
            clz = (Class<T>)(((ParameterizedType)(this.getClass().getGenericSuperclass())).getActualTypeArguments()[0]);
        }
        return clz;
    }

    public Serializable add(T t) {
        return this.getSession().save(t);
    }
    public void delete(int id) {
        T entity = load(id);
        this.getSession().delete(entity);
    }
    public void update(T t) {
        this.getSession().update(t);
    }
    public T load(int id) {
        return this.getSession().load(getClz(), id);
    }
    public T get(int id) {
        return this.getSession().get(getClz(), id);
    }

    public List<T> list(String hql) {
        Query<T> query = this.getSession().createQuery(hql);
        List<T> list = query.list();
        return list;
    }

    public List<T> listAll() {
        String hql = "from "+getClz().toString().split(" ")[1];
        Query<T> query = this.getSession().createQuery(hql);
        List<T> list = query.list();
        return list;
    }
    public Pager pagerff(Pager p, Map<String, Object> pram) {
        Session session = getSession();
        Query query1 = session.createQuery("select count(*) "+p.getHql().
                substring(p.getHql().indexOf("from")));
        query1.setProperties(pram);
        long count = (Long) query1.uniqueResult();
        String hql=p.getHql();//获取查询语句
        Query query= session.createQuery(hql).setCacheable(true);
            //设置参数
        query.setProperties(pram);
            //查询具体数据
        p.setRowsTotal(count);
        //指定从那个对象开始查询，参数的索引位置是从0开始的，
        query.setFirstResult((p.getPage()-1)*p.getRows());
        //分页时，一次最多产寻的对象数
        query.setMaxResults(p.getRows());
        List<T> list1=query.list();
        p.setList(list1);
        return p;
    }
    public void executeUpdate(String hql) {
        Query<T> query = this.getSession().createQuery(hql);
        query.executeUpdate();
    }
}