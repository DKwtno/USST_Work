package com.example.demo;

import com.example.demo.service.*;
import com.example.demo.vo.project.Project;
import com.example.demo.vo.user.Preference;
import com.example.demo.vo.user.User;
import com.example.demo.vo.util.Pager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {
    @Autowired
    UserService userService;
    @Autowired
    Environment environment;
    @Autowired
    UniversityService universityService;
    @Autowired
    AdminAuditService adminAuditService;
    @Autowired
    StudentService studentService;
    @Autowired
    ProjectService projectService;
    @Autowired
    MessageService messageService;
    @Autowired
    PreferenceService preferenceService;
    @Autowired
    InviteMessageService inviteMessageService;
    @Autowired
    IssueReplyService issueReplyService;
    @Test
    public void testCascade(){
//        universityService.deleteUniversity(129);
//        Student student = new Student();
//        University university = new University();
//        university.setUid(129);
//        student.setUid(26);
//        student.setUniversity(university);
//        studentService.addStudent(student);
//        for(int i = 0; i < 10; i++) {
//            University university = new University();
//            university.setAddress("12");
//            university.setName("fas"+i);
//            universityService.addUniversity(university);
//        }
//        universityService.deleteUniversity(1);
//        projectService.banReply(235,177,178);
        projectService.deleteProject(177,235);
    }

    @Test
    public void testFun2(){
        int[] arr = new int[10];
        long temp = 1;
        for(int i = 1; i < 100000; i++){
            temp *= i;
            while(temp > i*10)
                temp /= 10;
            int idx = String.valueOf(temp).charAt(0)-'0';
            System.out.println(temp);
            arr[idx]++;
        }
        for(int i = 0; i < arr.length; i++)
            System.out.println(i+":"+arr[i]);
    }
    @Test
    public void testUser(){
        User user = new User();
        user.setPassword("dasdasf");
        userService.doRegister(user);
    }
    @Test
    public void testProject(){
        Pager pager = new Pager();
        pager.setPage(1);
        pager.setRows(11);
        pager = projectService.getProjectAsMember(181,pager);
        System.out.println(pager);
//        inviteMessageService.inviteStudent(243,181);
//        inviteMessageService.inviteStudent(243,182);
//        inviteMessageService.inviteStudent(243,183);
//        inviteMessageService.inviteStudent(243,184);
//        inviteMessageService.inviteStudent(243,185);
//        inviteMessageService.inviteStudent(243,186);
//        inviteMessageService.agreeInvitation(181,246);
//        inviteMessageService.agreeInvitation(182,247);
//        inviteMessageService.rejectInvitation(183,248);
//        projectService.deleteProject(177,242);
//        adminAuditService.activateProject(242);
//        adminAuditService.activateProject(243);
//        inviteMessageService.inviteStudent(242,179);
//        inviteMessageService.inviteStudent(242,180);
//        projectService.banReply(242,177,181);
//        Project project = new Project();
//        project.setName("21231ad");
//        projectService.setUpProject(project,177);
//        project.setName("412fdsw2");
//        projectService.setUpProject(project,179);
//        projectService.deleteMember(235,148);
//        Pager pager = new Pager();
//        pager.setPage(1);
//        pager.setRows(11);
//        Map map = new HashMap();
//        map.put("keyword","你好");
//        map.put("partnerSize","4-7");
//        projectService.getProjectByFilter(pager,true,map);
//        Project project = new Project();
//        project.setPid(235);
//        project.setName("412da");
//        Student student = new Student();
//        student.setUid(177);
//        project.getStudentSet().add();
//        projectService.updateProject(project);
    }
    @Test
    public void testIssue(){
        issueReplyService.banReply(243,179,181);
        issueReplyService.banReply(243,179,183);
        issueReplyService.banReply(243,179,186);
        Pager pager = new Pager();
        pager.setPage(1);
        pager.setRows(11);
        pager = issueReplyService.getBannedUser(243,pager);
        System.out.println(pager);
    }
    @Test
    public void testMsg(){
//        Message message = new Message();
//        message.setContent("内容2");
//        messageService.sendMessage(message, 178, 171);
//        List<Message> messages = messageService.getLatestTalking(0,171,3);
//        for(Message message:messages)
//            System.out.println(message.getTalkId());
//        Pager pager = new Pager();
//        pager.setRows(10);
//        pager.setPage(1);
//        messageService.inviteStudent(235,171);
//        Talk talk = messageService.getTalkByPage(pager,0, 136, 138);
//        System.out.println(talk.isBanned()+":"+talk.isBanner());
//        for(Message message:(List<Message>)pager.getList()){//此处安全性欠佳，可能需要验证用户身份
//            System.out.println(message.getContent()+":"+message.getTalkId());
//        }
//        messageService.getAdminMessage(179);
//        userService.getAll();
//        messageService.getLatestTalking(0,171,10);
//        messageService.banMessage(136,137);
//        messageService.banMessage(137,136);
//        messageService.banMessage(136,138);
//        messageService.removeBanned(136,138);
    }
    @Test
    public void testPreference(){
//        Preference preference = new Preference();
//        preference.setUid(177);
//        Preference preference1 = new Preference();
//        preference1.setUid(177);
//        preference.setPreference(Preference.NOT_BE_INVITED);
//        preference1.setPreference(Preference.NOT_SHOW_INVITE_MSG);
//        preferenceService.addPreference(preference);
//        preferenceService.addPreference(preference1);
//        List<Preference> preferences = preferenceService.getPreference(177);
//        for(Preference preference2:preferences){
//            System.out.println(preference2.getUid()+":"+preference2.getPreference());
//        }
        Preference preference2 = new Preference();
        preference2.setUid(177);
        preference2.setPreference(Preference.NOT_SHOW_INVITE_MSG);
        preferenceService.deletePreference(preference2);
    }
    @Test
    public void testPage(){
        Pager pager = new Pager();
        pager.setRows(10);
        pager.setPage(1);
        Map<String, Object> map = new HashMap<>();
        map.put("uid",26);
        map.put("sno",3);
        pager = studentService.getStudentByFilter(pager,map,"college.cid");
        System.out.println(pager);

    }
    @Test
    public void testShowPage(){
        Pager pager = new Pager();
        pager.setRows(10);
        pager.setPage(4);
        pager = userService.getAllByPage(pager, "uid", "telephone");
        for(Object[] objects:(List<Object[]>) pager.getList()){
            System.out.println(objects[0]+":"+objects[1]);
        }
    }
    @Test
    public void testEnv(){
        try {
            userService.updateHead(new
                    MockMultipartFile("file", "test.txt",
                    ",multipart/form-data", "hello upload".getBytes("UTF-8")),26);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void contextLoads() {
        for(int i = 1; i < 100; i++){
            User user = new User();
            user.setPassword(i+"100");
            user.setTelephone(i+"5142");
            userService.doRegister(user);
        }
//        userService.doRegister(user);
    }


}
