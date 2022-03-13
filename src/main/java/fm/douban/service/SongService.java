package fm.douban.service;

import fm.douban.model.Song;
import fm.douban.param.SongQueryParam;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SongService {
    // 增加一个歌曲
    Song add(Song song);
    // 根据 id 查询
    Song get(String songId);
    // 查询全部歌曲
    Page<Song> list(SongQueryParam songParam);
    // 修改一首歌
    boolean modify(Song song);
    // 删除一首歌
    boolean delete(String songId);
    //获取所有歌
    List<Song> getAll();
}
