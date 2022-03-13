package fm.douban.app.control;

import fm.douban.model.Singer;
import fm.douban.model.Song;
import fm.douban.model.Subject;
import fm.douban.param.SingerQueryParam;
import fm.douban.param.SubjectQueryParam;
import fm.douban.service.SingerService;
import fm.douban.service.SongService;
import fm.douban.service.SubjectService;
import fm.douban.util.HttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


import fm.douban.util.SubjectUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//http://127.0.0.1:8080/artist?subjectId=12816
@Controller
public class SubjectControl {
    @Autowired
    private SubjectService subjectService;
    @Autowired
    private SingerService singerService;
    @Autowired
    private SongService songService;
    @Autowired
    private SubjectUtil su;

    @GetMapping(path = "/artist")
    public String mhzDetail(Model model, @RequestParam(name = "subjectId") String subjectId){

        //获取主题
        Subject subject  = subjectService.get(subjectId);
        //获取相关歌曲
        List<String> realtedSongs = subject.getSongIds();
        //获取主题歌手
//        Singer singer = singerService.get(subject.getMaster());
        //获取相关歌手
//        List<String> relSingers = singer.getSimilarSingerIds();
//        List<Singer> relatedSinger = new ArrayList<>();

//        for(int i=0;i<relSingers.size();i++){
//            relatedSinger.add(singerService.get(relSingers.get(i)));
//        }
        model.addAttribute("subject",subject);
        model.addAttribute("songs",realtedSongs);
//        model.addAttribute("singer",singer);
//        model.addAttribute("simSingers",relatedSinger);
        return "mhzdetail";
    }

    // 歌单列表
    @GetMapping(path = "/collection")
    public String collection(Model model){
        List<Subject> allSubjects = subjectService.getAll();

        List<Map> allSubject = new ArrayList<>();

        int counter = 0;

            for(Subject aSubject:allSubjects){
                Map theSubject = new HashMap();
                //获取名字
                theSubject.put("name",aSubject.getName());

                //获取关联歌曲ID
                List<String> songIds = aSubject.getSongIds();

                //获取关联歌曲名称
                List<String> songNames = new ArrayList<>();
                for(String aId:songIds){
                    Song aSong = songService.get(aId);
                    songNames.add(aSong.getName());
                }
                theSubject.put("songNames",songNames);
                //获取关联头像url
                theSubject.put("avatar",aSubject.getCover());
                //获取关联作者
                theSubject.put("master",aSubject.getMaster());

                allSubject.add(theSubject);
                counter++;
                if(counter>=10){
                    break;
                }
            }


        model.addAttribute("subjectInfo",allSubject);
        return "collection";
    }
    // 歌单详情
    @GetMapping(path = "/collectiondetail")
    public String collectionDetail(Model model, @RequestParam(name = "subjectId") String subjectId){
//        SubjectQueryParam SQP = new SubjectQueryParam();
//        SQP.setId(subjectId);
//        SQP.setSubjectType(su.TYPE_MHZ);
//        //查询对像
//        List<Subject> subjects = subjectService.getSubjects(SQP);
//        Subject aSubject = subjects.get(0);
//
//        //sub关联的歌手
//        SingerQueryParam SingerQP = new SingerQueryParam();
//        SingerQP.setName(aSubject.getMaster());
//        Singer relatedSinger = singerService.get(SingerQP);
//
//            //sub对应的歌曲对象
//        List<String> songIds = aSubject.getSongIds();
//        List<Song> songs = new ArrayList<>();
//        for(String aSongId:songIds){
//            songs.add(songService.get(aSongId));
//        }
//        //关联的歌手的其他歌单
//        //null
//
//        model.addAttribute("subject",aSubject);
//        model.addAttribute("singer",relatedSinger);
//        model.addAttribute("songs",songs);
//        model.addAttribute("otherSubjects",aSubject);
//
//        return "collectiondetail";
        SubjectQueryParam SQP = new SubjectQueryParam();
        SQP.setId(subjectId);
        SQP.setSubjectType(su.TYPE_MHZ);
        Subject aSubject = new Subject();
        List<Song> songs = new ArrayList<>();
        Singer relatedSinger = new Singer();

        try{
            List<Subject> subjects = subjectService.getSubjects(SQP);
            aSubject = subjects.get(0);

            //sub关联的歌手
            SingerQueryParam SingerQP = new SingerQueryParam();
            SingerQP.setName(aSubject.getMaster());
            relatedSinger = singerService.get(SingerQP);

            //sub对应的歌曲对象
            List<String> songIds = aSubject.getSongIds();

            for(String aSongId:songIds){
                songs.add(songService.get(aSongId));
            }
        }catch(Exception e){

        }
        //查询对像

        //关联的歌手的其他歌单
        //null
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


        model.addAttribute("subject",subject);
        model.addAttribute("singer",relatedSinger);
        model.addAttribute("songs",songs);
        model.addAttribute("otherSubjects",aSubject);

        return "collectiondetail";
    }


}
