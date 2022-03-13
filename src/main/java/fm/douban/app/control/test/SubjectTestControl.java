package fm.douban.app.control.test;

import fm.douban.model.Song;
import fm.douban.model.Subject;
import fm.douban.service.SubjectService;
import fm.douban.util.HttpUtil;
import fm.douban.util.SubjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
public class SubjectTestControl {
    @Autowired
    private SubjectService subjectService;

    @GetMapping("/test/subject/add")
    @ResponseBody
    public Subject testAdd(){
        List songIds= new ArrayList<String>();
        songIds.add("1032");
        songIds.add("1");
        songIds.add("114514");
        songIds.add("114515");
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter df2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Subject subject = new Subject();
        subject.setId("0");
        subject.setGmtCreated(LocalDateTime.parse("2019-01-01 00:00:00",df));
        subject.setGmtModified(LocalDateTime.parse("2020-01-01 00:00:00",df));
        subject.setName("苏大友联盟金曲");
        subject.setDescription("由田所夫斯基,MUR等人制作的 高 雅 音乐(确信)");
        subject.setCover("https://img9.doubanio.com/img/fmadmin/large/31596.jpg");
        subject.setMaster("田所浩二");
        subject.setPublishedDate(LocalDate.parse("2019-01-01",df2));
        subject.setSubjectType(SubjectUtil.TYPE_MHZ);
        subject.setSubjectSubType(SubjectUtil.TYPE_SUB_ARTIST);
        subject.setSongIds(songIds);

        return subjectService.addSubject(subject);
    }

    @GetMapping("/test/subject/get")
    @ResponseBody
    public Subject testGet(){

        return subjectService.get("14");
    }

    @GetMapping("/test/subject/getByType")
    @ResponseBody
    public List<Subject> testGetByType(){
        return subjectService.getSubjects(SubjectUtil.TYPE_MHZ);
    }

    @GetMapping("/test/subject/getBySubType")
    @ResponseBody
    public List<Subject> testGetBySubType(){
        return subjectService.getSubjects(SubjectUtil.TYPE_MHZ,SubjectUtil.TYPE_SUB_ARTIST);
    }

    @GetMapping("/test/subject/del")
    @ResponseBody
    public boolean testDelete(){

        subjectService.delete("1");
        subjectService.delete("2");
        subjectService.delete("3");
        subjectService.delete("4");
        subjectService.delete("5");
        subjectService.delete("6");
        subjectService.delete("7");
        subjectService.delete("8");
        subjectService.delete("9");


        return subjectService.delete("0");
    }

    @GetMapping("/test/subject/getAll")
    @ResponseBody
    public List<Subject> testGetAll(){
        return subjectService.getAll();
    }

    @GetMapping("/test/subject/modify")
    @ResponseBody
    public boolean testModify(){
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter df2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List songIds= new ArrayList<String>();
        songIds.add("1032");
        songIds.add("1");
        songIds.add("114514");
        songIds.add("114515");

        Subject subject = new Subject();
        subject.setId("25114");
        subject.setGmtCreated(LocalDateTime.parse("2019-01-01 00:00:00",df));
        subject.setGmtModified(LocalDateTime.parse("2020-01-01 00:00:00",df));
        subject.setName("Robbie Williams 系");
        subject.setDescription("为你推荐 Robbie Williams 以及相似的艺术家");
        subject.setCover("https://img9.doubanio.com/img/fmadmin/large/31596.jpg");
        subject.setMaster("豆瓣FM");
        subject.setPublishedDate(LocalDate.parse("2019-01-01",df2));
        subject.setSubjectType(SubjectUtil.TYPE_MHZ);
        subject.setSubjectSubType(SubjectUtil.TYPE_SUB_ARTIST);
        subject.setSongIds(songIds);

        return subjectService.modify(subject);
    }


}
