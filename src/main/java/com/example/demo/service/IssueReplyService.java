package com.example.demo.service;

import com.example.demo.dao.*;
import com.example.demo.excp.AuthorityNotMatchException;
import com.example.demo.excp.ProjectActivatedException;
import com.example.demo.vo.msg.ReplyMsg;
import com.example.demo.vo.project.Issue;
import com.example.demo.vo.project.Project;
import com.example.demo.vo.project.Reply;
import com.example.demo.vo.user.User;
import com.example.demo.vo.util.Pager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Set;

@Service
public class IssueReplyService {
    @Autowired
    ProjectDAO projectDAO;
    @Autowired
    UserDAO userDAO;
    @Autowired
    IssueDAO issueDAO;
    @Autowired
    ReplyDAO replyDAO;
    @Autowired
    ReplyMsgDAO replyMsgDAO;
    @Transactional
    /**
     * 创建帖子的时候向队长发送消息
     */
    public void setUpIssue(Issue issue, int pid, int author){
        Project project = projectDAO.load(pid);
        issue.setProject(project);
        issue.setPublishTime(new Timestamp(System.currentTimeMillis()));
        User user = userDAO.load(author);
        issue.setAuthor(user);
        issue.setLastAnswerTime(issue.getPublishTime());
        ReplyMsg replyMsg = new ReplyMsg(issue, project.getCaptain().getUid());
        replyMsg.setMsgType(2);
        issue.getReplyMsgSet().add(replyMsg);
        issueDAO.add(issue);
        replyMsgDAO.add(replyMsg);//发送消息
    }
    @Transactional
    public void banReply(int pid, int captain, int beBanned){
        Project project = projectDAO.load(pid);
        checkProjectAuthority(project);
        if(!project.getCaptain().getUid().equals(captain)){
            throw new AuthorityNotMatchException("该用户没有禁言权限！");
        }
        User user = userDAO.load(beBanned);
        project.getBanIssueUser().add(user);
        user.getBeBanned().add(project);
    }
    @Transactional
    /**
     * 如果没有给出回复对象，就填0（或负数）
     * reply要有内容，作者，回复对象
     * replyTo如果非0，就代表一定有回复的对象！
     */
    public void replyTo(Reply reply, int issueId, int replyTo){
        checkReplyAuthority(issueId, reply.getAuthor());
        if(replyTo>0){
            User user = userDAO.load(replyTo);
            reply.setReplyTo(user);
        }
        Issue issue = issueDAO.load(issueId);
        Project project = issue.getProject();
        checkProjectAuthority(project);
        if(project.getCaptain().getUid().equals(reply.getAuthor())){//项目队长回复了这个问题
            issue.setSolved(true);//标记已经被队长回复
        }
        reply.setIssue(issue);
        reply.setOrder(issue.getMaxOrder()+1);
        issue.setMaxOrder(reply.getOrder());//最高楼层
        issue.getReplies().add(reply);
        issue.setReplyNumber(issue.getReplies().size());
        ReplyMsg replyMsg = new ReplyMsg(reply);
        reply.setReplyMsg(replyMsg);//发送消息，其实一方管理，这一步根本没必要，不过以防万一
        replyDAO.add(reply);
        replyMsgDAO.add(replyMsg);
    }
    @Transactional
    /**
     * 获取用户的“我的回复”
     */
    public Pager getReply(int uid, Pager pager){
        String hql = "from ReplyMsg where replyTo = "+uid;
        pager.setHql(hql);
        return replyMsgDAO.pagerff(pager,null);
    }
    /**
     * @param pid
     * @param pager
     * @return 返回被禁止在该板块下留言的用户
     */
    @Transactional
    public Pager getBannedUser(int pid, Pager pager){
        String hql = "select u from Project p join p.banIssueUser u where p.pid = "+pid;
        pager.setHql(hql);
        return projectDAO.pagerff(pager, null);
    }
    /**
     * 根据用户判断是否能在该project下回复
     * @param issueId
     * @param author
     */
    private void checkReplyAuthority(int issueId, User author) {
        Issue issue = issueDAO.load(issueId);
        Set<User> users= issue.getProject().getBanIssueUser();
        if(users.contains(author)){
            throw new AuthorityNotMatchException("你被禁止在该版块回复！");
        }
    }

    private void checkProjectAuthority(Project project) {
        if(project.getActivated()==null || !project.getActivated())
            throw new ProjectActivatedException("该项目尚未通过审核！");
        if(project.getClosed())
            throw new ProjectActivatedException("该项目已关闭！");
    }
    public void deleteReply(int rid){
        Reply reply = replyDAO.load(rid);
        Issue issue = reply.getIssue();
        issue.getReplies().remove(reply);//移除回复
        issue.setReplyNumber(issue.getReplies().size());//更新回复数量
        reply.setIssue(null);
        replyDAO.delete(rid);
    }
}
