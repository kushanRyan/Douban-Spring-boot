package fm.douban.service;

import fm.douban.model.Singer;
import fm.douban.param.SingerQueryParam;

import java.util.List;


public interface SingerService {
    // 增加一个歌手
    Singer addSinger(Singer singer);
    // 根据歌手 id 查询歌手
    Singer get(String singerId);
    // 查询全部歌手
    List<Singer> getAll();
    // 修改歌手。只能修改、名称、头像、主页、相似的歌手id
    boolean modify(Singer singer);
    // 根据 id 主键删除歌手
    boolean delete(String singerId);

    Singer get(SingerQueryParam SQP);
}
