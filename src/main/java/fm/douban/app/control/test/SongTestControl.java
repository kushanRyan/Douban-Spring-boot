package fm.douban.app.control.test;

import fm.douban.model.Singer;
import fm.douban.model.Song;
import fm.douban.param.SongQueryParam;
import fm.douban.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
public class SongTestControl {

    @Autowired
    private SongService songService;

    @GetMapping("/test/song/add")
    @ResponseBody
    public Song testAdd(){
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Song testSong = new Song();
        testSong.setId("114515");
        testSong.setGmtCreated(LocalDateTime.parse("2019-01-01 00:00:00",df));
        testSong.setGmtModified(LocalDateTime.parse("2020-01-01 00:00:00",df));
        testSong.setName("abbbbb");
        testSong.setCover("https://img3.doubanio.com/view/subject/m/public/s2558680.jpg");
        testSong.setUrl("https://mr1.doubanio.com/d1f557ead98d5f5268f12833d2d93fe1/0/fm/song/p1032_128k.mp4");
        List<String> ids = new ArrayList<>();
        ids.add("1919811");
        testSong.setSingerIds(ids);
        return songService.add(testSong);
    }

    @GetMapping("/test/song/get")
    @ResponseBody
    public Song testGet(){
        return songService.get("0");
    }

    @GetMapping("/test/song/list")
    @ResponseBody
    public Page<Song> testList(){
        SongQueryParam songQP = new SongQueryParam();
        songQP.setPageNum(1);
        songQP.setId("0");
        songQP.setName("野兽先辈目力咆哮");
        songQP.setLyrics("哼哼哼啊啊啊啊啊啊啊");
        songQP.setCover("11414");
        songQP.setUrl("1919810");

        return songService.list(songQP);

    }

    @GetMapping("/test/song/modify")
    @ResponseBody
    public boolean testModify(){
        SongQueryParam singer = new SongQueryParam();
        singer.setPageNum(1);
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        singer.setId("1032");
        singer.setGmtCreated(LocalDateTime.parse("2019-01-01 00:00:00",df));
        singer.setGmtModified(LocalDateTime.parse("2020-01-01 00:00:00",df));
        singer.setName("Heroes&Martyrs");
        singer.setCover("https://img3.doubanio.com/view/subject/m/public/s2558680.jpg");
        singer.setUrl("https://mr1.doubanio.com/d1f557ead98d5f5268f12833d2d93fe1/0/fm/song/p1032_128k.mp4");
        List<String> ids = new ArrayList<>();
        ids.add("800");
        singer.setSingerIds(ids);
        return songService.modify(singer);
    }

    @GetMapping("/test/song/del")
    @ResponseBody
    public boolean testDelete(){
        return songService.delete("0");
    }

    @GetMapping("/test/song/getAll")
    @ResponseBody
    public List<Song> testGetAll(){
        return songService.getAll();
    }
}
