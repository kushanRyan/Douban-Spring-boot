package fm.douban.spider;

import com.alibaba.fastjson.JSON;
import fm.douban.model.Singer;
import fm.douban.model.Song;
import fm.douban.service.SingerService;
import fm.douban.service.SongService;
import fm.douban.service.SubjectService;
import fm.douban.util.HttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class SingerSongSpider {
    // 系统自动注入主题服务
    @Autowired
    SubjectService subjectService;
    // 系统自动注入歌手服务
    @Autowired
    SingerService singerService;
    // 系统自动注入歌曲服务
    @Autowired
    SongService songService;
    // 系统启动的时候自动执行爬取任务
    @Autowired
    private HttpUtil hu;
    @PostConstruct
    public void init(){
//        doExcute();
    }
    //url
    private String bySingerApi1="https://douban.fm/j/v2/artist/";
    private static final String artist = "https://douban.fm/j/v2/artist/";
    private String Host ="fm.douban.com";
    
    // 开始执行爬取任务
    public void doExcute(){
//        getSongDataBySingers();
    }

    // 执行爬取歌曲数据
    private void getSongDataBySingers(){
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        //缓存变量
        Song tempSong = new Song();
        //取得歌手的数据
        List<Singer> allSingers = singerService.getAll();
        //类似艺术家缓存
        List<String> relatedArtString = new ArrayList<>();
        //通过id取得一个歌手的歌曲
        for(Singer aSinger:allSingers){
            //开始爬取
            String SecContent = hu.getContent(bySingerApi1+"1",hu.buildHeaderData(artist+"1" ,Host));
            Map mapString = JSON.parseObject(SecContent, Map.class);

            Map songList = (Map)mapString.get("songlist");
            Map creatorList = (Map)songList.get("creator");
            Map relatedChannel = (Map)mapString.get("related_channel");
            //获取类似艺术家
            List<Map> relatedArtist = (List)relatedChannel.get("similar_artists");
            for(Map aArtsit:relatedArtist){
                if(aArtsit.get("id").toString().equals("188236445")){

                }else{
                    relatedArtString.add(aArtsit.get("id").toString());
                    Singer singer = new Singer();
                    singer.setId(aArtsit.get("id").toString());
                    singer.setGmtCreated(LocalDateTime.parse("2019-01-01 00:00:00",df));
                    singer.setGmtModified(LocalDateTime.parse("2020-01-01 00:00:00",df));
                    singer.setName(aArtsit.get("name").toString());
                    singer.setAvatar(aArtsit.get("avatar").toString());
                    singer.setHomepage("1919810");
                    singer.setSimilarSingerIds(null);

                    singerService.addSinger(singer);
                }
            }

            //填数据
            tempSong.setId(mapString.get("id").toString());
            tempSong.setGmtCreated(LocalDateTime.parse(songList.get("created_time").toString(),df));
            tempSong.setGmtModified(LocalDateTime.parse(songList.get("updated_time").toString(),df));
            tempSong.setName(mapString.get("name").toString());
            tempSong.setLyrics("11414");
            tempSong.setCover(songList.get("cover").toString());
            tempSong.setUrl(creatorList.get("url").toString());
            tempSong.setSingerIds(relatedArtString);

            //修改对应歌手的数据（更新）
            songService.modify(tempSong);
        }
    }
}
