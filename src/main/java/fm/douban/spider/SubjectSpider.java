package fm.douban.spider;


import fm.douban.model.Singer;
import fm.douban.model.Song;
import fm.douban.model.Subject;
import fm.douban.service.SingerService;
import fm.douban.service.SongService;
import fm.douban.service.SubjectService;
import fm.douban.util.HttpUtil;
import fm.douban.util.SubjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class SubjectSpider {

    // 系统自动注入主题服务
    @Autowired
    private SubjectService subjectService;
    // 系统自动注入歌手服务
    @Autowired
    private SingerService singerService;

    @Autowired
    private HttpUtil hu;

    @Autowired
    private SubjectUtil su;

    @Autowired
    private SongService songService;

    private List artList = new ArrayList<>();
    private List lanList = new ArrayList<>();
    private List genList = new ArrayList<>();
    private List sceList = new ArrayList<>();
    private Map mapChannel = new HashMap<String, String>();  ;
    private String[] object = {"artist","language","genre","scenario"};

    private String mhzApi ="https://douban.fm/j/v2/rec_channels?specific=all";
    private String songApi1 ="https://douban.fm/j/v2/playlist?channel=" ;
    private String songApi2 = "&kbps=128&client=s%3Amainsite%7Cy%3A3.0&app_name=radio_website&version=100&type=n";
    private String Host ="fm.douban.com";
    private String singListApi="https://douban.fm/j/v2/songlist/explore?type=hot&genre=0&limit=20&sample_cnt=5";

    private static final String referer = "https://fm.douban.com/";

    // 系统启动的时候自动执行爬取任务
    @PostConstruct
    public void init(){
//        doExcute();
    }

    // 开始执行爬取任务
    public void doExcute(){
        getSubjectData();
        getCollectionsData();
    }


    // 执行爬取主题数据
    private void getSubjectData(){
        //爬取第一个数据
        String FirContent = hu.getContent(mhzApi,hu.buildHeaderData(mhzApi,Host));
        Map mapString = JSON.parseObject(FirContent, Map.class);
        //爬取data
        Map mapData = (Map)mapString.get("data");
        mapChannel = (Map)mapData.get("channels");

        //存放已存储的id和名字
        List singerList = new ArrayList<>();
        //时间格式
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter df2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        //判断触发器
        boolean Trigger=false;

        for(int j=0;j<4;j++){
            //存储channel的大类
            List mapContainer = (List)mapChannel.get(object[j]);

            for(int i=0;i<mapContainer.size();i++){
                //存储每个object
                Map tempMap=(Map)mapContainer.get(i);

                Subject subject = new Subject();
                subject.setId(String.valueOf(tempMap.get("id").toString()));
                subject.setGmtCreated(LocalDateTime.parse("2019-01-01 00:00:00",df));
                subject.setGmtModified((LocalDateTime.parse("2020-01-01 00:00:00",df)));
                subject.setName(tempMap.get("name").toString());
                subject.setDescription(tempMap.get("intro").toString());
                subject.setCover(tempMap.get("cover").toString());
                Map tempList=(Map)tempMap.get("creator");
                subject.setMaster((String)tempList.get("name"));
                subject.setPublishedDate(LocalDate.parse("1919-08-10",df2));
                subject.setSubjectType(SubjectUtil.TYPE_MHZ);
                subject.setSongIds((List)tempList.get("hot_songs"));

                // //标记已存储主题数据                    
                // subjectSubTypeList.add(tempList.get("subjectSubType").toString());
                // subjectSubTypeList.add(tempList.get("subjectType").toString()); 
                //对重复数据进行判断
                for(int z=0;z<singerList.size();z++){
                    if(tempMap.get("name").toString()==singerList.get(z)){
                        Trigger=true;
                        break;
                    }
                }

                for(int z=0;z<artList.size();z++){
                    if(subject==artList.get(z)){
                        Trigger=true;
                        break;
                    }
                }
                for(int z=0;z<lanList.size();z++){
                    if(subject==artList.get(z)){
                        Trigger=true;
                        break;
                    }
                }
                for(int z=0;z<genList.size();z++){
                    if(subject==artList.get(z)){
                        Trigger=true;
                        break;
                    }
                }
                for(int z=0;z<sceList.size();z++){
                    if(subject==artList.get(z)){
                        Trigger=true;
                        break;
                    }
                }
                //根据情况添加主题
                if(Trigger==false){
                    if(j==0){
                        subject.setSubjectSubType(SubjectUtil.TYPE_SUB_ARTIST);
                        artList.add(subject);
                    }else if(j==1){
                        subject.setSubjectSubType(SubjectUtil.TYPE_SUB_AGE);
                        lanList.add(subject);
                    }else if(j==2){
                        subject.setSubjectSubType(SubjectUtil.TYPE_SUB_STYLE);
                        genList.add(subject);
                    }else {
                        subject.setSubjectSubType(SubjectUtil.TYPE_SUB_MOOD);
                        sceList.add(subject);
                    }
//                    subjectService.addSubject(subject);
                    getSubjectSongData(tempMap.get("id").toString());
//                    singerSongSpider.getSongDataBySingers();
                }

                Trigger=false;
                //标记已存储歌手数据
                singerList.add(tempMap.get("name").toString());
            }
        }

    }
    // 执行爬取主题关联的歌曲数据
    private void getSubjectSongData(String subjectId){
        boolean trigger=false;
        //list_用来存储判断用变量
        List<Song> songList = new ArrayList<>();
        List<String> songsId = new ArrayList<>();

        //开始爬数据
        String url = songApi1 + subjectId + songApi2;
        String SecContent = hu.getContent(url,hu.buildHeaderData(referer,Host));


        Map mapString = JSON.parseObject(SecContent, Map.class);
        List listSongObj = (List)mapString.get("song");
        Subject subject = subjectService.get(subjectId);
        //分析List,解析每一个song
        for(int i = 0; i < listSongObj.size(); i++) {
            Map songData = (Map)listSongObj.get(i);
            Song song = new Song();
            songsId.add(songData.get("sid").toString());
            song.setId(songData.get("sid").toString());
            song.setUrl(songData.get("url").toString());
            song.setCover(songData.get("picture").toString());
            song.setName(songData.get("title").toString());
            List<String> singersId = new ArrayList<>();
            //解析song熟悉里面的singers
            List listSingerObj = (List)songData.get("singers");
            for(int j = 0; j < listSingerObj.size(); j++) {
                Map singerData = (Map)listSingerObj.get(j);
                Singer singer = new Singer();
                singersId.add(singerData.get("id").toString());
                singer.setId(singerData.get("id").toString());
                singer.setAvatar(singerData.get("avatar").toString());
                singer.setName(singerData.get("name").toString());
            }
            song.setSingerIds(singersId);
            //添加歌曲
            if(!trigger){
//                songService.add(song);
                subjectService.modify(subject);
            }
            //重复判断
            for(int j=0;j<songList.size();j++){
                if(songList.get(i)==song){
                    trigger=true;
                }
            }
            trigger=false;
        }
    }

    // 爬取歌单数据
    private void getCollectionsData(){
        //暂时存储歌手和歌单
        Singer singListCreator = new Singer();
        Subject tempSub = new Subject();
        //格式化日期
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter df2 = DateTimeFormatter.ofPattern("yyyy-MM-dd ");
        //爬取初始数据
        String FirContent = hu.getContent(singListApi,hu.buildHeaderData(singListApi,Host));
        List mapList = JSON.parseObject(FirContent, List.class);

        //开始数据修改操作

        //修改歌单
        for(int i=0;i<mapList.size();i++){
            Map aMap  = (Map)mapList.get(i);

            Map creator = (Map)aMap.get("creator");
            List relatedSong = (List)aMap.get("sample_songs");
            List songIds = new ArrayList<>();

            for(int z=0;z<relatedSong.size();z++){

                Map aSong = (Map)relatedSong.get(z);
                songIds.add(aSong.get("sid").toString());
            }
            //修改作者
            singListCreator.setId(creator.get("id").toString());
            singListCreator.setAvatar(creator.get("picture").toString());
            singListCreator.setName(creator.get("name").toString());
            singListCreator.setGmtCreated(null);
            singListCreator.setGmtModified(null);
            singListCreator.setHomepage(creator.get("url").toString());
            singListCreator.setSimilarSingerIds(null);

            //修改歌单
            tempSub.setId(aMap.get("id").toString());
            tempSub.setGmtCreated(LocalDateTime.parse(aMap.get("created_time").toString(),df));
            tempSub.setGmtModified(LocalDateTime.parse(aMap.get("updated_time").toString(),df));
            tempSub.setName(aMap.get("title").toString());
            tempSub.setDescription(aMap.get("intro").toString());
            tempSub.setCover(aMap.get("cover").toString());
            tempSub.setMaster(creator.get("url").toString());
            tempSub.setPublishedDate(null);
            tempSub.setSubjectType(su.TYPE_COLLECTION);
            tempSub.setSubjectSubType(su.TYPE_SUB_ARTIST);
            tempSub.setSongIds(songIds);
//            subjectService.addSubject(tempSub);
//            singerService.addSinger(singListCreator);
        }
    }
}

