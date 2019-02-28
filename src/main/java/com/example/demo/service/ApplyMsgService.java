package com.example.demo.service;

import com.example.demo.dao.*;
import com.example.demo.excp.AuthorityNotMatchException;
import com.example.demo.excp.InviteIntervalException;
import com.example.demo.excp.ProjectActivatedException;
import com.example.demo.excp.ProjectFullException;
import com.example.demo.vo.msg.InviteMsg;
import com.example.demo.vo.project.Project;
import com.example.demo.vo.user.Student;
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
public class ApplyMsgService {

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
    Environment environment;
    @Transactional//申请项目
    public void applyProject(int uid, int pid){
        int interval = Integer.valueOf(environment.getProperty("invite.interval"));
        if(!inviteInterval(uid,pid,interval))
            throw new InviteIntervalException(String.format("距离上次申请该项目未超过%s天！",interval));
        InviteMsg inviteMsg = new InviteMsg();
        User user = userDAO.load(uid);
        Project project = projectDAO.load(pid);
        checkProjectAuthority(project);
        inviteMsg.setInvited(user);
        inviteMsg.setProject(project);
        inviteMsg.setInviteTime(new Timestamp(System.currentTimeMillis()));
        inviteMsg.setMsgType(2);
        inviteMsg.setCaptainId(project.getCaptain().getUid());
        inviteMsgDAO.add(inviteMsg);
    }

    private void checkProjectAuthority(Project project) {
        if(project.getActivated()==null || !project.getActivated())
            throw new ProjectActivatedException("该项目尚未通过审核！");
        if(project.getClosed())
            throw new ProjectActivatedException("该项目已关闭！");
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

    @Transactional//同意申请
    public void agreeApply(int apid) throws ProjectFullException,
            AuthorityNotMatchException{
        InviteMsg inviteMsg = inviteMsgDAO.load(apid);
        Project project = inviteMsg.getProject();
        Set<Student> studentSet = project.getStudentSet();
        if(studentSet.size()>=project.getPartnerSize()){
            throw new ProjectFullException("项目人数已满！");
        }
        if(!inviteMsg.getInvited().getRole().equals(1))
            throw new AuthorityNotMatchException("申请人不是学生！");
        Student student = studentDAO.load(inviteMsg.getInvited().getUid());
        studentSet.add(student);//添加队友
        project.setCurSize(studentSet.size());
        student.getProject().add(project);
        inviteMsg.setAgreed(true);
        inviteMsg.setRead(false);
    }
    @Transactional
    public void rejectApply(int apid){
        InviteMsg inviteMsg = inviteMsgDAO.load(apid);
        inviteMsg.setAgreed(false);
        inviteMsg.setRead(false);
    }
    @Transactional
    public void readApply(int apld){
        InviteMsg inviteMsg = inviteMsgDAO.load(apld);
        inviteMsg.setRead(true);
    }
    @Transactional//获取用户的申请记录
    public Pager getApplyingMsg(int uid, Pager pager){
        studentDAO.load(uid);//检测是否是学生
        String hql = "from InviteMsg inv where inv.invited.uid="
                +uid+" and inv.msgType = 2 order by inv.inviteTime desc";
        pager.setHql(hql);
        return inviteMsgDAO.pagerff(pager,null);
    }
    @Transactional//获取申请的学生
    public Pager getApplyingStudent(Pager pager, int captainId){
        studentDAO.load(captainId);//检测是否是学生
        String hql = "from InviteMsg inv where inv.captainId="
                +captainId+" and inv.msgType = 2 order by inv.inviteTime desc";
        pager.setHql(hql);
        return inviteMsgDAO.pagerff(pager,null);
    }
}
