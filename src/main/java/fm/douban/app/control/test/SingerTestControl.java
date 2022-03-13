package fm.douban.app.control.test;

import fm.douban.model.Singer;
import fm.douban.service.SingerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
public class SingerTestControl {

    @Autowired
    private SingerService singerService;

    @GetMapping("/test/singer/add")
    @ResponseBody
    public Singer testAddSinger(){
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Singer singer = new Singer();
        singer.setId("194298819");
        singer.setGmtCreated(LocalDateTime.parse("2019-01-01 00:00:00",df));
        singer.setGmtModified(LocalDateTime.parse("2020-01-01 00:00:00",df));
        singer.setName("豆瓣FM");
        singer.setAvatar("https://img3.doubanio.com/view/subject/m/public/s2558680.jpg");
        singer.setHomepage("https://mr1.doubanio.com/d1f557ead98d5f5268f12833d2d93fe1/0/fm/song/p1032_128k.mp4");
        List<String> ids = new ArrayList<>();
        ids.add("800");
        singer.setSimilarSingerIds(ids);

        Singer singerResult = singerService.addSinger(singer);
        return singerResult;
    }

    @GetMapping("/test/singer/getAll")
    @ResponseBody
    public List<Singer> testGetAll(){
        return singerService.getAll();
    }

    @GetMapping("/test/singer/getOne")
    @ResponseBody
    public Singer testGetSinger(){
        return singerService.get("0");
    }

    @GetMapping("/test/singer/modify")
    @ResponseBody
    public boolean testModifySinger(){

        return singerService.modify(null);
    }

    @GetMapping("/test/singer/del")
    @ResponseBody
    public boolean testDelSinger(){
        return singerService.delete("0");
    }

}
