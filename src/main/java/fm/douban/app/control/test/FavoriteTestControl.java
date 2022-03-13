package fm.douban.app.control.test;

import fm.douban.model.Favorite;
import fm.douban.model.Singer;
import fm.douban.service.FavoriteService;
import fm.douban.service.SingerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import fm.douban.util.FavoriteUtil;
@Controller
public class FavoriteTestControl {

    @Autowired
    private FavoriteService favoriteService;
    @Autowired
    private FavoriteUtil fu;

    @GetMapping("/test/favorite/add")
    @ResponseBody
    public Favorite add(){
        Favorite favorite = new Favorite();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        favorite.setId("1");
        favorite.setGmtCreated(LocalDateTime.parse("2019-01-01 00:00:00",df));
        favorite.setGmtModified(LocalDateTime.parse("2019-01-01 00:00:00",df));
        favorite.setUserId("114514");
        favorite.setType(fu.TYPE_RED_HEART);
        favorite.setItemType(fu.ITEM_TYPE_MHZ);
        favorite.setItemId("1");
        favoriteService.add(favorite);
        return favorite;
    }


    @GetMapping("/test/favorite/getAll")
    @ResponseBody
    public List<Favorite> getAll(){
        return favoriteService.getAll();
    }

    @GetMapping("/test/favorite/delete")
    @ResponseBody
    public boolean delete(){
        Favorite favorite = new Favorite();
        favorite.setId("0");
        return favoriteService.delete(favorite);
    }


}
