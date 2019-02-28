package com.example.demo.service;

import com.example.demo.dao.StudentDAO;
import com.example.demo.dao.TeacherDAO;
import com.example.demo.dao.UserDAO;
import com.example.demo.excp.AuthorityNotMatchException;
import com.example.demo.vo.user.Student;
import com.example.demo.vo.user.Teacher;
import com.example.demo.vo.user.User;
import com.example.demo.vo.util.Pager;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;


@Service
public class UserService {
    @Autowired
    UserDAO userDAO;
    @Autowired
    StudentDAO studentDAO;
    @Autowired
    TeacherDAO teacherDAO;
    @Autowired
    Environment environment;
    @Transactional
    public User getById(int uid){
        return userDAO.get(uid);
    }
    //注册
    @Transactional
    public void doRegister(User user) throws RuntimeException {
        if (!checkTel(user) && !checkEmail(user)) {
            throw new RuntimeException("手机号与邮箱未通过测试");
        }
        user.setCreateDate(new Date());
        if (user.getRole() == null)
            user.setRole(1);
        Integer uid = (Integer) userDAO.add(user);
        if (user.getRole() == 1) {
            //学生
            Student student = new Student();
            student.setUid(uid);
            studentDAO.add(student);
        } else if (user.getRole() == 2) {
            Teacher teacher = new Teacher();
            teacher.setUid(uid);
            teacherDAO.add(teacher);
        }
//        throw new RuntimeException("测试回滚");
    }

    //查看电话是否重复or为空
    private boolean checkTel(User user) {
        if (userDAO.getByTelephone(user) == null)
            return true;
        return false;
    }

    //查看邮箱是否重复or为空
    private boolean checkEmail(User user) {
        if (userDAO.getByEmail(user) == null)
            return true;
        return false;
    }

    //登录
    @Transactional
    public boolean doLogin(User user) {
        User user1;
        if (user.getTelephone() != null) {
            user1 = userDAO.getByTelephone(user);
        } else if (user.getEmail() != null) {
            user1 = userDAO.getByEmail(user);
        } else {
            return false;
        }
        if(!user1.getActivated()){
            throw new AuthorityNotMatchException("该用户被禁止使用，请联系管理员！");
        }
        return user1.getPassword().equals(user.getPassword());
    }

    @Transactional
    public String getPassword(int id) {
        return userDAO.load(id).getPassword();
    }
    //更新密码
    @Transactional
    public void updatePassword(User user) {
        if (user.getUid() == null)//傻办法，没时间，之后优化
            throw new RuntimeException("uid不能为null!");
        userDAO.executeUpdate("update User u set u.password='" + user.getPassword() + "'");
    }
    //更新头像
    @Transactional
    public void updateHead(MultipartFile multipartFile, int uid) throws IOException {
        if(userDAO.get(uid)==null)
            throw new RuntimeException("uid不存在！");
        String fileName = environment.getProperty("file.head.path");
        File file=new File(fileName);
        if(!file.exists())
        {
            file.mkdir();
        }
        Calendar cal=Calendar.getInstance();
        String separator = environment.getProperty("file.separator");

//如果年的目录不存在，创建年的目录
        int year=cal.get(Calendar.YEAR);
        fileName=fileName + separator + year;
        file=new File(fileName);
        if(!file.exists())
        {
            file.mkdir();
        }
//如果月份不存在，创建月份的目录
        int month=cal.get(Calendar.MONTH)+1;
        fileName=fileName+separator;
        if(month<10)
        {
            fileName=fileName+"0";
        }
        fileName=fileName+month;
        file=new File(fileName);
        if(!file.exists())
        {
            file.mkdir();
        }


//生成文件名的日部分
        int day=cal.get(Calendar.DAY_OF_MONTH);
        fileName=fileName+separator;
        if(day<10)
        {
            fileName=fileName+"0";
        }
        fileName=fileName+day;


//生成文件名的小时部分
        int hour=cal.get(Calendar.HOUR_OF_DAY);
        if(hour<10)
        {
            fileName=fileName+"0";
        }
        fileName=fileName+hour;


//生成文件名的分钟部分
        int minute=cal.get(Calendar.MINUTE);
        if(minute<10)
        {
            fileName=fileName+"0";
        }
        fileName=fileName+minute;


//生成文件名的秒部分
        int second=cal.get(Calendar.SECOND);
        if(second<10)
        {
            fileName=fileName+"0";
        }
        fileName=fileName+second;


//生成文件名的毫秒部分
        int millisecond=cal.get(Calendar.MILLISECOND);
        if(millisecond<10)
        {
            fileName=fileName+"0";
        }
        if(millisecond<100)
        {
            fileName=fileName+"0";
        }

        fileName=fileName+millisecond;

        System.out.println(fileName);
        String imageFileName = multipartFile.getOriginalFilename();
//生成文件的扩展名部分
        fileName = fileName + imageFileName.substring(imageFileName.indexOf("."));
        multipartFile.transferTo(new File(fileName));
        fileName = fileName.replace(separator,"_");//保存入数据库转换
        userDAO.executeUpdate("update User u set image_url = '"+
                fileName +"' where u.uid = "+uid);
    }
    @Transactional
    //输入想要返回的字段名
    public Pager getAllByPage(Pager pager, String ... rows){
        String hql = " from User";
        if(rows.length!=0) {
            if(rows.length==1)
                hql = hql + rows[0];
            else
                hql = hql + StringUtils.join(Arrays.asList(rows), ',');
            pager.setHql("select " + hql);
        }
        return userDAO.pagerff(pager,null);
    }
    @Transactional
    public void getAll() {
        userDAO.listAll();
    }
}
