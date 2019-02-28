package com.example.demo.service;

import com.example.demo.dao.ProjectDAO;
import com.example.demo.dao.StudentDAO;
import com.example.demo.dao.UserDAO;
import com.example.demo.excp.AuthorityNotMatchException;
import com.example.demo.excp.ProjectActivatedException;
import com.example.demo.excp.ProjectFullException;
import com.example.demo.vo.project.Project;
import com.example.demo.vo.user.Student;
import com.example.demo.vo.user.User;
import com.example.demo.vo.util.Pager;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Service
public class ProjectService {
    @Autowired
    ProjectDAO projectDAO;
    @Autowired
    StudentDAO studentDAO;
    @Autowired
    UserDAO userDAO;
    @Autowired
    Environment environment;
    @Transactional//创建项目（待审核）
    public void setUpProject(Project project, int captain){
        Student student = studentDAO.load(captain);
        checkOpenNum(captain);
        project.setCaptain(student);
        projectDAO.add(project);
    }

    public void checkOpenNum(int captain) {
        int maxProjectNum = Integer.parseInt(environment.getProperty("captain.project.num.max"));
        int openProjectNum = getOpenNum(captain);
        if(openProjectNum>=maxProjectNum)
            throw new ProjectFullException("用户进行中的项目超过限制"+maxProjectNum+"个！");
    }

    private int getOpenNum(int captain) {
        String hql = "select count(*) from Project where catpain.uid="+captain+" and closed = true";
        return (int) projectDAO.getSession().createQuery(hql).uniqueResult();
    }

    @Transactional
    public void deleteMember(int pid, int uid){
        Project project = projectDAO.load(pid);
        checkProjectAuthority(project);
        Student student = studentDAO.load(uid);
        student.setUid(uid);
        if(project.getCaptain().getUid().equals(uid)){//如果退出的是队长
            if(project.getStudentSet().size()==0){//没有其他人了
                deleteProject(pid, uid);
            }else{
                Iterator<Student> studentIterator = project.getStudentSet().iterator();
                Student student1 = studentIterator.next();
                project.setCaptain(student1);//顺位补上
                project.getStudentSet().remove(student1);
                project.setCurSize(project.getStudentSet().size());
                student1.getProject().remove(project);
            }
        }else{//如果退出/删除的是普通群员
            project.getStudentSet().remove(student);
            project.setCurSize(project.getStudentSet().size());
            student.getProject().remove(project);
        }
    }
    @Transactional//彻底删除项目
    public void deleteProject(int captain,int pid){
        if(!projectDAO.load(pid).getCaptain().getUid().equals(captain))
            throw new AuthorityNotMatchException("只有队长能删除项目！");
        projectDAO.delete(pid);
    }

    @Transactional//搜索项目
    public Pager getProject(Pager pager){
        String hql = "from Project where activated=true";
        pager.setHql(hql);
        return projectDAO.pagerff(pager, null);
    }
    @SuppressWarnings("Duplicates")
    @Transactional
    /**
     * params为筛选条件，字符串数组为返回的列，默认全返回
     * pager中的hql语句可以为null
     * partnerSize这一列的参数必须是"3-6"这样的范围输入，中间是减号
     */
    public Pager getProjectByFilter(Pager pager, boolean showFull,
                                    Map<String,Object> params, String ... rows){
        StringBuilder hql = new StringBuilder(" from Project p join p.tagSet t ");
        if(rows==null || rows.length==0){
            hql = hql.insert(0, "select p ");
        }else{
            if(rows.length==1)
                hql = hql.insert(0, "select "+rows[0]);
            else
                hql = hql.insert(0,"select " + StringUtils.join(Arrays.asList(rows), ','));
        }
        String keyword = null;
        if(params!=null && params.size()>0){
            hql = hql.append(" where ");
            for(String s:params.keySet()){
                if(s.equals("keyword")){
                    keyword = (String)params.get(s);
                }else if(s.equals("partnerSize")){
                    String[] range = ((String)params.get(s)).split("-");
                    hql = hql.append("partnerSize > "+range[0]+" and partnerSize < "+range[1]);
                }else{
                    hql = hql.append(s + " = '" + params.get(s) + "' and ");
                }
            }
            if(hql.toString().endsWith("and "))
                hql.delete(hql.length() - 4,hql.length());
        }
        //这里的关键字一定要有东西！
        if(keyword!=null){
            String append = " and (p.name like '%s' or p.content like '%s'" +
                    " or t.name like '%s')";//只对标题、内容和tag标签检查
            hql.append(append.replace("%s","%"+keyword+"%"));
        }
        if(!showFull){//人满了就不显示了
            String append = " and curSize < partnerSize";
            hql.append(append.replace("%s","%"+keyword+"%"));
        }
        pager.setHql(hql.toString());
        return studentDAO.pagerff(pager, null);
    }

    @Transactional
    public void updateProject(Project project){
        projectDAO.update(project);//不确定null的部分作为外键是否会被update
    }

    private void checkProjectAuthority(Project project) {
        if(project.getActivated()==null || !project.getActivated())
            throw new ProjectActivatedException("该项目尚未通过审核！");
        if(project.getClosed())
            throw new ProjectActivatedException("该项目已关闭！");
    }

    @Transactional
    public Pager getProjectAsCaptain(int captain, Pager pager){
        pager.setHql("from Project where captain.uid="+captain);
        return projectDAO.pagerff(pager, null);
    }
    @Transactional
    public Pager getProjectAsMember(int uid, Pager pager){
        Student student = studentDAO.load(uid);
        pager.setHql("select p from Project p join p.studentSet s where s.uid="+uid);
        Map<String,Object> map = new HashMap<>();
        map.put("student",student);
        return projectDAO.pagerff(pager,map);
    }
    @Transactional
    public void closeProject(int captain, int pid){
        Project project = projectDAO.load(pid);
        if(!project.getCaptain().getUid().equals(captain))
            throw new AuthorityNotMatchException("只有队长能关闭项目！");
        project.setClosed(true);
    }
    @Transactional
    public void openProject(int captain, int pid){
        Project project = projectDAO.load(pid);
        if(!project.getCaptain().getUid().equals(captain))
            throw new AuthorityNotMatchException("只有队长能开启项目！");
        checkOpenNum(captain);
        String hql = "update Project set closed = true where pid = "+pid;
        projectDAO.executeUpdate(hql);
    }
}
