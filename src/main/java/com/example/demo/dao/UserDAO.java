package com.example.demo.dao;

import com.example.demo.vo.user.User;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDAO extends BaseDAOImpl<User> {
    public User getByTelephone(User user){
        String hql = "from User u where u.telephone=:tel";
        Query query = this.getSession().createQuery(hql);
        query.setParameter("tel",user.getTelephone());
        List<User> users = query.list();
        return users.size()>0?users.get(0):null;
    }
    public User getByEmail(User user){
        String hql = "from User u where u.email=:email";
        Query query = this.getSession().createQuery(hql);
        query.setParameter("email",user.getEmail());
        List<User> users = query.list();
        return users.size()>0?users.get(0):null;
    }
}
