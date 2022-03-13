package fm.douban.app.control;

import fm.douban.model.Singer;
import fm.douban.service.SingerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Controller
public class SingerControl {
    @Autowired
    private SingerService singerService;

    @GetMapping(path = "/user-guide")
    public String myMhz(Model model){
        model.addAttribute("singers", randomSingers());
        return "userguide";
    }
    @GetMapping(path = "/singer/random")
    @ResponseBody
    public List<Singer> randomSingers(){
        List<Singer> singers = singerService.getAll();
        List<Singer> backSingers = new ArrayList<>();
        Random r = new Random();
        for(int i=0;i<10;i++){
            backSingers.add(singers.get(r.nextInt(singers.size())));
        }
        return backSingers;
    }
}
