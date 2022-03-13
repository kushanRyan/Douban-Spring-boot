package fm.douban.service.impl;

import com.mongodb.client.result.DeleteResult;
import fm.douban.model.Favorite;
import fm.douban.model.Singer;
import fm.douban.service.FavoriteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class FavoriteServiceImpl implements FavoriteService {
    private static final Logger LOG = LoggerFactory.getLogger(SingerServiceImpl.class);

    @Autowired
    private MongoTemplate mongoTemplate;
    // 新增一个喜欢
    public Favorite add(Favorite fav){
        if(fav == null) {
            LOG.error("input data is null.");
            return null;
        }

        return mongoTemplate.insert(fav);
    }


    // 计算喜欢数。如果数大于 0 ，表示已经喜欢
    public List<Favorite> list(Favorite favParam){
        //作为服务，要对入参进行判断，不能假设被调用时，入参一定正确
        if (favParam == null) {
            LOG.error("input subjectParam is not correct.");
            return null;
        }
        // 总条件
        Criteria criteria = new Criteria();
        // 可能有多个子条件
        List<Criteria> subCris = new ArrayList();
        //查询参数
        String UId = favParam.getUserId();
        if (StringUtils.hasText(UId)) {
            subCris.add(Criteria.where("userId").is(UId));
        }

        criteria.andOperator(subCris.toArray(new Criteria[]{}));
        // 条件对象构建查询对象
        Query query = new Query(criteria);

        query.limit(10);
        List<Favorite> favorites = mongoTemplate.find(query, Favorite.class);
        return  favorites;

    }
    // 删除一个喜欢。
    public boolean delete(Favorite favParam){
        if(favParam==null) {
            LOG.error("input singerId is blank.");
            return false;
        }

        DeleteResult result = mongoTemplate.remove(favParam);
        return result != null && result.getDeletedCount() > 0;
    }

    public List<Favorite> getAll(){
        List<Favorite> favorite = mongoTemplate.findAll(Favorite.class);
        return favorite;
    }

}
