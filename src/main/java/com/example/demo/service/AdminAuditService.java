package com.example.demo.service;

import com.example.demo.dao.InviteMsgDAO;
import com.example.demo.dao.ProjectDAO;
import com.example.demo.dao.UserDAO;
import com.example.demo.excp.AuthorityNotMatchException;
import com.example.demo.excp.ProjectFullException;
import com.example.demo.vo.msg.InviteMsg;
import com.example.demo.vo.project.Project;
import com.example.demo.vo.user.User;
import com.example.demo.vo.util.Pager;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Service
public class AdminAuditService {
    @Autowired
    ProjectDAO projectDAO;
    @Autowired
    UserDAO userDAO;
    @Autowired
    InviteMsgDAO inviteMsgDAO;
    @Autowired
    ProjectService projectService;
    @Transactional
    public Pager getUnactivatedProject(Pager pager){
        String hql = "from Project where activated is null";
        pager.setHql(hql);
        return projectDAO.pagerff(pager,null);
    }
    @Transactional//审核通过项目
    public void activateProject(int pid){
        Project project = projectDAO.load(pid);
        try {
            projectService.checkOpenNum(project.getCaptain().getUid());
        }catch (ProjectFullException e){
            throw new ProjectFullException("项目编号 "+pid+" 审核出错，具体原因为："+e.getMsg());
        }
        project.setActivated(true);
        sendActivatedMessage(project, true, "");
    }
    @Transactional
    public void banUser(int admin, int uid){
        checkAdminAuthority(admin);
        User user = userDAO.load(uid);
        user.setActivated(false);//禁止使用
    }

    @Transactional
    public void activateUser(int admin, int uid){
        checkAdminAuthority(admin);
        User user = userDAO.load(uid);
        user.setActivated(true);//激活用户
    }
    private void checkAdminAuthority(int admin) {
        if(!userDAO.load(admin).getRole().equals(0))
            throw new AuthorityNotMatchException("只有管理员有禁止用户的权限！");
    }
    //根据审核结果传回信息
    private void sendActivatedMessage(Project project, boolean success, String message) {
        InviteMsg inviteMsg = new InviteMsg();
        inviteMsg.setCaptainId(project.getCaptain().getUid());
        if(success)
            inviteMsg.setMsgType(3);//审核通过
        else
            inviteMsg.setMsgType(4);//审核不通过
        inviteMsg.setInvited(userDAO.load(inviteMsg.getCaptainId()));//发送给项目队长
        inviteMsg.setMessage(message);//审核结果，若通过则无结果
        inviteMsg.setInviteTime(new Timestamp(System.currentTimeMillis()));
        inviteMsgDAO.add(inviteMsg);
    }

    @Transactional
    public void activateProjects(List<Integer> pids){
//        String hql = "update Project set activated = true where pid in (:pids)";
//        Query query = projectDAO.getSession().createQuery(hql);
//        query.setParameterList("pids",pids);
//        query.executeUpdate();
        for(int pid:pids)
            activateProject(pid);
    }
    @Transactional
    public void rejectProjects(List<Integer> pids, List<String> messages){
        if(pids.size()!=messages.size())
            throw new RuntimeException("消息数目和审核数量不匹配！");
        String hql = "update Project set activated = true where pid in (:pids)";
        Query query = projectDAO.getSession().createQuery(hql);
        query.setParameterList("pids",pids);
        query.executeUpdate();
        for(int i = 0; i < pids.size(); i++){
            rejectProject(pids.get(i), messages.get(i));
        }
    }
    @Transactional//不通过审核，以及为什么不通过
    public void rejectProject(int pid, String message){
        Project project = projectDAO.load(pid);
        project.setActivated(false);
        sendActivatedMessage(project, false, message);
    }
}
