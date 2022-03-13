package fm.douban.app.control;

import fm.douban.model.*;
import fm.douban.param.SongQueryParam;
import fm.douban.service.FavoriteService;
import fm.douban.service.SingerService;
import fm.douban.service.SongService;
import fm.douban.service.SubjectService;
import fm.douban.util.FavoriteUtil;
import fm.douban.util.SubjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MainControl {

    @Autowired
    private SongService songService;

    @Autowired
    private SingerService singerService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private FavoriteService favoriteService;
    @Autowired
    private SubjectUtil su;

    @Autowired
    private FavoriteUtil fu;


    @GetMapping("/index")
    public String index(Model model){
        indexProcesser1(model);
        indexProcesser2(model);
        return "index";
    }

    //处理播放器数据
    public void indexProcesser1(Model model){
        //创建分页变量
        SongQueryParam sqp = new SongQueryParam();
        sqp.setPageNum(1);
        sqp.setPageSize(1);
        //查询分页
        Page<Song> aSong = songService.list(sqp);
        Song theSong = aSong.getContent().get(0);
        model.addAttribute("song",theSong);

        //获取关联歌手数据
        List<String> singerIds = new ArrayList<>();
        for(int i=0;i<theSong.getSingerIds().size();i++){
            singerIds.add(theSong.getSingerIds().get(i));
        }
        List<Singer> singerList = new ArrayList<>();
        for(int i=0;i<singerIds.size();i++){
            singerList.add(singerService.get("i"));
        }
        model.addAttribute("singers",singerList);
    }

    public void indexProcesser2(Model model) {
        List<Subject> theSubjects =  subjectService.getSubjects(SubjectUtil.TYPE_MHZ);
        List artSubList = new ArrayList<>();
        List ageSubList = new ArrayList<>();
        List styleSubList = new ArrayList<>();
        List moodSubList = new ArrayList<>();

        for(int i=0;i<theSubjects.size();i++){
            String subType = theSubjects.get(i).getSubjectSubType();
            if(subType.equals(su.TYPE_SUB_ARTIST) ){
                artSubList.add(theSubjects.get(i));
            }else if(subType.equals(su.TYPE_SUB_AGE)){
                ageSubList.add(theSubjects.get(i));
            }else if(subType.equals(su.TYPE_SUB_STYLE)){
                styleSubList.add(theSubjects.get(i));
            }else if(subType.equals(su.TYPE_SUB_MOOD)){
                moodSubList.add(theSubjects.get(i));
            }
        }
        MhzViewModel ageMhz = new MhzViewModel();
        ageMhz.setTitle(su.TYPE_SUB_ARTIST);
        ageMhz.setSubjects(artSubList);
        MhzViewModel styleMhz = new MhzViewModel();
        styleMhz.setTitle(su.TYPE_SUB_STYLE);
        styleMhz.setSubjects(styleSubList);
        MhzViewModel moodMhz = new MhzViewModel();
        moodMhz.setTitle(su.TYPE_SUB_AGE);
        moodMhz.setSubjects(moodSubList);

        List<MhzViewModel> mhzViewModels = new ArrayList<>();
        mhzViewModels.add(ageMhz);
        mhzViewModels.add(styleMhz);
        mhzViewModels.add(moodMhz);
        model.addAttribute("artistDatas",artSubList);
        model.addAttribute("mhzViewModels",mhzViewModels);

        for(int i=0;i<artSubList.size();i++){
            artSubList.get(i);
        }

    }

    // 搜索页
    @GetMapping(path = "/search")
    public String search(Model model){
        return "search";
    }

    // 搜索结果
    @GetMapping(path = "/searchContent")
    @ResponseBody
    public Map searchContent(@RequestParam(name = "keyword") String keyword){
        List<Song> allSongs = songService.getAll();
        List<Song> rightSong = new ArrayList<>();
        Map songMap = new HashMap();
        for(Song songs:allSongs){
            if(songs.getName().startsWith(keyword)){
                rightSong.add(songs);
            }
        }
        songMap.put("songs",rightSong);
        return songMap;
    }

    // 我的页面
    @GetMapping(path = "/my")
    public String myPage(Model model,HttpServletRequest request,HttpServletResponse response){
        Favorite favorite = new Favorite();
        favorite.setUserId("114514");
        List<Favorite> isFavorite = favoriteService.list(favorite);
        List<Song> songs = new ArrayList<>();
        for(Favorite favorites:isFavorite){
            songs.add(songService.get(favorites.getItemId()));
        }

        model.addAttribute("favorites",isFavorite);
        model.addAttribute("songs",songs);
        return "my";
    }

    // 喜欢或不喜欢操作。对前端比较简单，不必判断状态
    // 已经喜欢，则删除，表示执行不喜欢操作
    // 还没有喜欢记录，则新增，表示执行喜欢操作
    @GetMapping(path = "/fav")
    @ResponseBody
    public Map doFav(@RequestParam(name = "itemType") String itemType,@RequestParam(name = "itemId") String itemId,HttpServletRequest request,HttpServletResponse response){
        Map aMap = new HashMap();
//        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        favorite.setId("0");
//        favorite.setGmtCreated(LocalDateTime.parse("2019-01-01 00:00:00",df));
//        favorite.setGmtModified(LocalDateTime.parse("2019-01-01 00:00:00",df));
//        favorite.setUserId("114514");
//        favorite.setItemType(itemType);
//        favorite.setItemType(fu.ITEM_TYPE_MHZ);
//        favorite.setItemId(itemId);

        Favorite favorite = new Favorite();
        favorite.setUserId("114514");
        favorite.setItemType(itemType);
        favorite.setItemId(itemId);

        List<Favorite> isFavorite = favoriteService.list(favorite);
        if(isFavorite==null){
            favoriteService.add(favorite);
        }else{
            favoriteService.delete(favorite);
        }
        aMap.put("fav",favorite);
        return aMap;
    }

}