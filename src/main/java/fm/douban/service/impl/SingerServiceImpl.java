package fm.douban.service.impl;

import fm.douban.model.Singer;
import fm.douban.model.Subject;
import fm.douban.param.SingerQueryParam;
import fm.douban.service.SingerService;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

@Service
public class SingerServiceImpl implements SingerService {

    private static final Logger LOG = LoggerFactory.getLogger(SingerServiceImpl.class);

    @Autowired
    private MongoTemplate mongoTemplate;
    // 增加一个歌手
    public Singer addSinger(Singer singer){
        if(singer == null) {
            LOG.error("input singer data is null.");
            return null;
        }

        return mongoTemplate.insert(singer);
    }


    @Override
    // 根据歌手 id 查询歌手
    public Singer get(String singerId){
        if(!StringUtils.hasText(singerId)) {
            LOG.error("input singerId is blank.");
            return null;
        }

        Singer singer = mongoTemplate.findById(singerId, Singer.class);
        return singer;
    }



    public Singer get(SingerQueryParam SQP){
        // 作为服务，要对入参进行判断，不能假设被调用时，入参一定正确
        if (SQP == null) {
            LOG.error("input subjectParam is not correct.");
            return null;
        }
        // 总条件
        Criteria criteria = new Criteria();
        // 可能有多个子条件
        List<Criteria> subCris = new ArrayList();
        //查询参数
        String name = SQP.getName();
        if (StringUtils.hasText(name)) {
            subCris.add(Criteria.where("name").is(name));
        }

        // 三个子条件以 and 关键词连接成总条件对象，相当于 name='' and lyrics='' and subjectId=''
        criteria.andOperator(subCris.toArray(new Criteria[]{}));
        // 条件对象构建查询对象
        Query query = new Query(criteria);

        query.limit(10);
        List<Singer> singers = mongoTemplate.find(query, Singer.class);

        return singers.get(0);
    }



    // 查询全部歌手
    public List<Singer> getAll(){
        List<Singer> singers = mongoTemplate.findAll(Singer.class);
        return singers;
    }

    // 修改歌手。只能修改、名称、头像、主页、相似的歌手id
    public boolean modify(Singer singer){
        // 作为服务，要对入参进行判断，不能假设被调用时，入参一定正确
        if (singer== null || !StringUtils.hasText(singer.getId())) {
            LOG.error("input song data is not correct.");
            return false;
        }
        //upd类，修改数据库的值
        Update updateData = new Update();
        // 主键不能修改,用获取id的方式来获得想修改的歌手位置
        Query query = new Query(Criteria.where("id").is(singer.getId()));

        // 值为 null 表示不修改。值为长度为 0 的字符串 "" 表示清空此字段
        //开始修改名称、头像、主页、相似的歌手id
        if (singer.getName() != null) {
            updateData.set("name", singer.getName());
        }

        if (singer.getAvatar() != null) {
            updateData.set("cover", singer.getAvatar());
        }

        if (singer.getHomepage() != null) {
            updateData.set("homepage", singer.getHomepage());
        }

        if (singer.getSimilarSingerIds() != null) {
            updateData.set("similarSingerIds", singer.getSimilarSingerIds());
        }

        //修改数据，并上传
        UpdateResult result = mongoTemplate.updateFirst(query, updateData, Singer.class);
        return result != null && result.getModifiedCount() > 0;
    }

    // 根据 id 主键删除歌手
    public boolean delete(String singerId){
        if(!StringUtils.hasText(singerId)) {
            LOG.error("input singerId is blank.");
            return false;
        }

        Singer singer = new Singer();
        singer.setId(singerId);

        DeleteResult result = mongoTemplate.remove(singer);
        return result != null && result.getDeletedCount() > 0;

    }
}
