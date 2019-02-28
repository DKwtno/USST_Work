package com.example.demo.service;

import com.example.demo.dao.*;
import com.example.demo.excp.*;
import com.example.demo.vo.msg.AdminMessage;
import com.example.demo.vo.msg.AdminReadMsg;
import com.example.demo.vo.msg.InviteMsg;
import com.example.demo.vo.msg.Message;
import com.example.demo.vo.project.Project;
import com.example.demo.vo.user.Student;
import com.example.demo.vo.user.Teacher;
import com.example.demo.vo.user.User;
import com.example.demo.vo.util.Pager;
import com.example.demo.vo.util.Talk;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.relation.Role;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class MessageService {
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
    @Transactional//发送消息
    public void sendMessage(Message message, int send, int receive)
            throws BeBannedException,BannedException{
        if(checkBanned(send, receive))
            throw new BeBannedException("你被对方拉黑了");
        if(checkBanned(receive,send))
            throw new BannedException("对方被你拉黑了");
        message.setSendTime(new Timestamp(System.currentTimeMillis()));
        User sender = userDAO.load(send);
        User receiver = userDAO.load(receive);
        sender.getSent().add(message);
        receiver.getReceived().add(message);
        message.setSender(sender);
        message.setReceiver(receiver);
        int talkId = getTalkId(send, receive);
        message.setTalkId(talkId);
        messageDAO.add(message);
    }
    @Transactional
    protected boolean checkBanned(int send, int receive){
        User user = userDAO.load(send);
        User banner = userDAO.load(receive);
        if(user.getBanned().contains(banner))
            return false;
        return true;
    }

    @Transactional
    public int getTalkId(int send, int receive) {
        List<Message> messages = messageDAO.list("from Message m where m.sender.uid = "+send+
                " and m.receiver.uid = "+receive+" or m.sender.uid = "+receive + " and m.receiver.uid = "+
                send);
        if(messages==null || messages.size()==0){
            return 1+(int) messageDAO.getSession().createQuery("select max(m.talkId) from Message m").uniqueResult();
        }
        return messages.get(0).getTalkId();
    }

    @Transactional
    public List<Message> getLatestTalking(int offset, int uid, int row){
        Query query = messageDAO.getSession().createQuery(
                "select distinct m.talkId from Message m where " +
                "m.sender.uid="+uid+" or m.receiver.uid="+uid+" order by m.sendTime desc");
        query.setFirstResult(offset);
        query.setMaxResults(row);
        List<Integer> list = query.list();//返回的是talk_id(按照日期降序)
        List<Message> messages = new ArrayList<>();
        for(int i:list){
            Query<Message> query1 = messageDAO.getSession().createQuery("from Message where talkId="+i+
                    " order by sendTime");
            query1.setMaxResults(1);
            messages.add(query1.list().get(0));
        }
        return messages;
    }
    @Transactional
    //分页获取消息记录
    //同时应该设置所有消息为已读
    //已读用户私信
    public Talk getTalkByPage(Pager pager, int talkId, int uid, int other){
        String hql = "from Message where talkId = "+ talkId;
        pager.setHql(hql);
        messageDAO.executeUpdate("update Message set is_read = true where talkId="+talkId);
        pager = messageDAO.pagerff(pager,null);
        Talk talk = new Talk();
        talk.setMessages((List<Message>) pager.getList());
        if(!checkBanned(uid,other))
            talk.setBanned(true);//是否被拉黑
        if(!checkBanned(other,uid))
            talk.setBanner(true);//是否拉黑了对方
        return talk;
    }

    @Transactional
    //获得用户的系统邮件
    //需要每隔一段时间就清空系统消息，不然数据量超过100000之后可能会溢出
    //改用hashcode优化
    //未测试
    public Pager getAdminMessage(int uid, Pager pager){
        pager.setHql("from AdminMessage order by sendTime desc");//排序获取官方消息页面
        pager = adminMessageDAO.pagerff(pager,null);//获取表中所有公告
        User user = userDAO.load(uid);
        Set<AdminReadMsg> adminReadMsgs = user.getAdminMessages();//该用户已读的系统消息
        List<AdminReadMsg> adminMessages2 = new ArrayList<>();
        List<AdminMessage> adminMessages = (List<AdminMessage>) pager.getList();//获取所有消息
        for(AdminMessage adminMessage:adminMessages){
            AdminReadMsg adminReadMsg = new AdminReadMsg();
            adminReadMsg.setAdminMessage(adminMessage);
            if(adminReadMsgs.contains(adminMessage)){//已读消息
                adminReadMsg.setRead(true);
            }
            adminMessages2.add(adminReadMsg);
        }
        pager.setList(adminMessages2);
        return pager;
    }
    @Transactional
    //已读系统邮件
    public void readAdminMsg(int uid, int amid){
        User user = userDAO.load(uid);
        AdminMessage adminMessage = adminMessageDAO.load(amid);
        AdminReadMsg adminReadMsg = new AdminReadMsg();
        adminReadMsg.setUser(user);
        adminReadMsg.setAdminMessage(adminMessage);
        user.getAdminMessages().add(adminReadMsg);
        adminMessage.getAdminReadMsgSet().add(adminReadMsg);
        adminReadDAO.add(adminReadMsg);//已读
    }
    @Transactional
    public void addAdminMsg(AdminMessage adminMessage){
        adminMessageDAO.add(adminMessage);
    }

    @Transactional
    //uid 把 ban 拉入黑名单，不接收来自其的任何消息
    public void banMessage(int uid, int ban){
        User user = userDAO.load(uid);
        User beBanned = userDAO.load(ban);
        user.getBanner().add(beBanned);
        beBanned.getBanned().add(user);
    }
    @Transactional
    public void removeBanned(int uid, int ban){
        User user = userDAO.load(uid);
        User beBanned = userDAO.load(ban);
        user.getBanner().remove(beBanned);
        beBanned.getBanned().remove(user);
    }
    @Transactional//未读私信数目
    public int getUnreadTalkNum(int uid){
        return (int) messageDAO.getSession().createQuery("select count(*) from Message " +
                "where receiver.uid="+uid).uniqueResult();
    }
    @Transactional//未读系统消息数目
    public int getUnreadAdminMsgNum(int uid){
        int adminMsgNum = (int) messageDAO.getSession()
                .createQuery("select count(*) from AdminMessage").uniqueResult();
        int readMsgNum = (int) messageDAO.getSession()
                .createQuery("select count(*) from AdminReadMsg where user.uid="+uid).uniqueResult();
        return adminMsgNum-readMsgNum;
    }
}
