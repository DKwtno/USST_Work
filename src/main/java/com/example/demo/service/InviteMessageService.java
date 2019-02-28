package com.example.demo.service;

import com.example.demo.dao.*;
import com.example.demo.excp.*;
import com.example.demo.vo.msg.InviteMsg;
import com.example.demo.vo.project.Project;
import com.example.demo.vo.user.Preference;
import com.example.demo.vo.user.Student;
import com.example.demo.vo.user.Teacher;
import com.example.demo.vo.user.User;
import com.example.demo.vo.util.Pager;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@Repository
public class InviteMessageService {
    @Autowired
    TeacherDAO teacherDAO;
    @Autowired
    MessageDAO messageDAO;
    @Autowired
    InviteMsgDAO inviteMsgDAO;
    @Autowired
    AdminMessageDAO adminMessageDAO;
    @Autowired
    UserDAO userDAO;
    @Autowired
    AdminReadDAO adminReadDAO;
    @Autowired
    ProjectDAO projectDAO;
    @Autowired
    StudentDAO studentDAO;
    @Autowired
    PreferenceService preferenceService;
    @Autowired
    Environment environment;
    @Transactional//队伍邀请学生入队
    public void inviteStudent(int pid, int uid){
        Project project = projectDAO.load(pid);
        checkProjectAuthority(project);
        User user = userDAO.load(uid);
        if(!user.getRole().equals(1))
            throw new AuthorityNotMatchException("邀请的不是学生！");
        inviteUser(user,project);
    }
    @Transactional
    public Pager userGetInvitingProject(int uid, Pager pager){
        String hql = "from InviteMsg inv where inv.invited.uid="
                +uid+" and inv.msgType = 1 order by inviteTime desc";
        pager.setHql(hql);
        return inviteMsgDAO.pagerff(pager,null);
    }
    @Transactional//包括所有已读和未读邀请消息
    public Pager studentGetInvitingProject(int uid,Pager pager){
        User user = userDAO.load(uid);
        if(user.getRole()!=1)
            throw new AuthorityNotMatchException("该用户不是学生！");
        return userGetInvitingProject(uid,pager);
    }
    @Transactional//获取已经邀请的学生
    public Pager getInvitedStudents(int captainId, Pager pager){
        String hql = "from InviteMsg inv where inv.invited.role=1 " +
                "and inv.captainId="+captainId+" and inv.msgType=1 order by inviteTime desc";
        pager.setHql(hql);
        return inviteMsgDAO.pagerff(pager,null);
    }
    @Transactional//邀请教师
    public void inviteTeacher(int pid, int uid){
        Project project = projectDAO.load(pid);
        checkProjectAuthority(project);
        User user = userDAO.load(uid);
        if(!user.getRole().equals(2))
            throw new AuthorityNotMatchException("邀请的不是老师！");
        inviteUser(user, project);
    }

    private void checkProjectAuthority(Project project) {
        if(project.getActivated()==null || !project.getActivated())
            throw new ProjectActivatedException("该项目尚未通过审核！");
        if(project.getClosed())
            throw new ProjectActivatedException("该项目已关闭！");
    }

    private void inviteUser(User user, Project project) {
        List<Preference> preferences = preferenceService.getPreference(user.getUid());
        for(Preference preference:preferences)//傻办法，反正最多十来个设置
            if(preference.getPreference()==Preference.NOT_BE_INVITED)
                throw new AuthorityNotMatchException("该用户设置为无法被邀请！");
        int interval = Integer.valueOf(environment.getProperty("invite.interval"));
        if(!inviteInterval(user.getUid(),project.getPid(),interval))
            throw new InviteIntervalException(String.format("距离上次邀请该用户未超过%s天！",interval));
        InviteMsg inviteMsg = new InviteMsg();
        inviteMsg.setInvited(user);
        inviteMsg.setProject(project);
        inviteMsg.setInviteTime(new Timestamp(System.currentTimeMillis()));
        inviteMsg.setCaptainId(project.getCaptain().getUid());//设置队长id
        inviteMsg.setMsgType(1);
        project.getInviting().add(inviteMsg);
        user.getInviteMsgs().add(inviteMsg);
        inviteMsgDAO.getSession().save(inviteMsg);
    }

    @Transactional//同意邀请加入队伍
    public void agreeInvitation(int uid, int inid)
            throws AuthorityNotMatchException, ProjectFullException, RoleNotFoundException {
        InviteMsg inviteMsg = inviteMsgDAO.load(inid);
        User user = inviteMsg.getInvited();
        if(uid != user.getUid())
            throw new AuthorityNotMatchException("非本人操作！");
        Project project = inviteMsg.getProject();
        if(user.getRole().equals(1)){//学生
            Set<Student> studentSet = project.getStudentSet();
            if(studentSet.size()>=project.getPartnerSize()){
                throw new ProjectFullException("该队伍人数已满，请联系队长！");
            }
            Student student = studentDAO.load(uid);
            studentSet.add(student);
            project.setCurSize(studentSet.size());
            student.getProject().add(project);
        }else if(user.getRole().equals(2)){//老师
            if(project.getTeacher()!=null){
                throw new ProjectFullException("该队伍已有指导教师！");
            }
            Teacher teacher = teacherDAO.load(uid);
            project.setTeacher(teacher);
        }else{
            throw new RoleNotFoundException("身份匹配错误！请联系管理员！");
        }
        inviteMsg.setAgreed(true);//用户同意加入项目之后，要设置未读，方便发出消息的项目得知
        inviteMsg.setRead(false);
    }
    @Transactional//拒绝邀请
    public void rejectInvitation(int uid, int inid){
        InviteMsg inviteMsg = inviteMsgDAO.load(inid);
        if(!inviteMsg.getInvited().getUid().equals(uid))
            throw new AuthorityNotMatchException("非本人操作！");
        inviteMsg.setAgreed(false);
        inviteMsg.setRead(false);
    }
    @Transactional//设置已读邀请
    public void readInvitation(int inid){
        InviteMsg inviteMsg = inviteMsgDAO.load(inid);
        inviteMsg.setRead(true);
    }
    @SuppressWarnings("Duplicates")
    private boolean inviteInterval(int uid, int pid, int interval) {
        String hql = "from InviteMsg where invited.uid="+uid+" and project.pid="
                +pid+" order by inviteTime desc";
        Query query = inviteMsgDAO.getSession().createQuery(hql);
        query.setMaxResults(1);
        List<InviteMsg> inviteMsgs = query.list();
        if(inviteMsgs==null || inviteMsgs.isEmpty()){//此前没有邀请过
            return true;
        }
        Timestamp before = new Timestamp(System.currentTimeMillis()-86400000l*interval);
        //每天有86400000毫秒，before代表interval天之前的timestamp
        return inviteMsgs.get(0).getInviteTime().before(before);
    }

    @Transactional
    public Pager teacherGetInvitingProject(int uid, Pager pager){
        User user = userDAO.load(uid);
        if(user.getRole()!=2)
            throw new AuthorityNotMatchException("该用户不是教师！");
        return userGetInvitingProject(uid,pager);
    }
    //    @Transactional//获取已经发送邀请的老师
//    public Pager getInvitedTeachers(int pid, Pager pager){
//        String hql = "from InviteMsg inv where inv.invited.role=2 " +
//                "and inv.project.pid="+pid+" order by inviteTime desc";
//        pager.setHql(hql);
//        return inviteMsgDAO.pagerff(pager,null);
//    }
    @Transactional//获取用户被邀请的次数
    public int userGetInvitingProjectNum(int uid){
        String hql = "select count(*) from InviteMsg where read=false and invited="+uid;
        Query query = inviteMsgDAO.getSession().createQuery(hql);
        return (int) query.uniqueResult();
    }
}
