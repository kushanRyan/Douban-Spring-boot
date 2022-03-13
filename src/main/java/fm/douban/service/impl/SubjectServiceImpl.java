package fm.douban.service.impl;

import com.mongodb.client.result.DeleteResult;

import com.mongodb.client.result.UpdateResult;
import fm.douban.model.Song;
import fm.douban.model.Subject;
import fm.douban.service.SubjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class SubjectServiceImpl implements SubjectService {

    private static final Logger LOG = LoggerFactory.getLogger(SingerServiceImpl.class);
    @Autowired
    private MongoTemplate mongoTemplate;
    // 增加一个主题
    public Subject addSubject(Subject subject){
        if(subject == null) {
            LOG.error("input sub data is null.");
            return null;
        }

        return mongoTemplate.insert(subject);
    }
    // 查询单个主题
    public Subject get(String subjectId){
        if(!StringUtils.hasText(subjectId)) {
            LOG.error("input sub is blank.");
            return null;
        }

        Subject subject = mongoTemplate.findById(subjectId,Subject.class);
        return subject;
    }
    // 查询一组主题
    public List<Subject> getSubjects(String type){

        // 作为服务，要对入参进行判断，不能假设被调用时，入参一定正确
        if (type == null) {
            LOG.error("input song data is not correct.");
            return null;
        }
        //将type作为查询条件
        Criteria criteria = Criteria.where("subjectType").is(type);

        // 条件对象构建查询对象
        Query query = new Query(criteria);
        // 仅演示：由于很多同学都在运行演示程序，所以需要限定输出，以免查询数据量太大
        query.limit(100);
        List<Subject> subjects = mongoTemplate.find(query, Subject.class);

        return subjects;

    }
    // 查询一组主题
    public List<Subject> getSubjects(String type, String subType){
        // 总条件
        Criteria criteria = new Criteria();
        // 可能有多个子条件
        List<Criteria> subCris = new ArrayList();
        if (StringUtils.hasText(type)) {
            subCris.add(Criteria.where("subjectType").is(type));
        }

        if (StringUtils.hasText(subType)) {
            subCris.add(Criteria.where("subjectSubType").is(subType));
        }

        // 必须至少有一个查询条件
        if (subCris.isEmpty()) {
            LOG.error("input ST&SST is not correct.");
            return null;
        }

        // 三个子条件以 and 关键词连接成总条件对象，相当于 name='' and lyrics='' and subjectId=''
        criteria.andOperator(subCris.toArray(new Criteria[]{}));

        // 条件对象构建查询对象
        Query query = new Query(criteria);
        // 仅演示：由于很多同学都在运行演示程序，所以需要限定输出，以免查询数据量太大
        query.limit(100);
        List<Subject> subjects = mongoTemplate.find(query, Subject.class);

        return subjects;

    }
    // 删除一个主题

    @Override
    public List<Subject> getSubjects(Subject subjectParam){
        // 作为服务，要对入参进行判断，不能假设被调用时，入参一定正确
        if (subjectParam == null) {
            LOG.error("input subjectParam is not correct.");
            return null;
        }
        // 总条件
        Criteria criteria = new Criteria();
        // 可能有多个子条件
        List<Criteria> subCris = new ArrayList();
        //查询参数
        String type = subjectParam.getSubjectType();
//        String subType = subjectParam.getSubjectSubType();
//        String master = subjectParam.getMaster();
        String id = subjectParam.getId();
//        //填充subcris
        if (StringUtils.hasText(type)) {
            subCris.add(Criteria.where("subjectType").is(type));
        }
//
//        if (StringUtils.hasText(subType)) {
//            subCris.add(Criteria.where("subjectSubType").is(subType));
//        }
//        if (StringUtils.hasText(master)) {
//            subCris.add(Criteria.where("master").is(master));
//        }
        if (StringUtils.hasText(id)) {
            subCris.add(Criteria.where("id").is(id));
        }

        // 三个子条件以 and 关键词连接成总条件对象，相当于 name='' and lyrics='' and subjectId=''
        criteria.andOperator(subCris.toArray(new Criteria[]{}));
        // 条件对象构建查询对象
        Query query = new Query(criteria);

        query.limit(100);
        List<Subject> subjects = mongoTemplate.find(query, Subject.class);

        return subjects;
    }

    public boolean delete(String subjectId){
        if(!StringUtils.hasText(subjectId)) {
            LOG.error("input sub is blank.");
            return false;
        }

        Subject subject = new Subject();
        subject.setId(subjectId);

        DeleteResult result = mongoTemplate.remove(subject);
        return result != null && result.getDeletedCount() > 0;

    }

    @Override
    public boolean modify(Subject subject) {

        if(subject == null || !StringUtils.hasText(subject.getId())) {
            return false;
        }

        Query query = new Query(Criteria.where("id").is(subject.getId()));

        Update updateData = new Update();

        if(subject.getCover() != null) {
            updateData.set("cover", subject.getCover());
        }

        if(subject.getDescription() != null) {
            updateData.set("description", subject.getDescription());
        }

        if(subject.getMaster() != null) {
            updateData.set("master", subject.getMaster());
        }

        if(subject.getName() != null) {
            updateData.set("name", subject.getName());
        }

        if(subject.getPublishedDate() != null) {
            updateData.set("publishedDate", subject.getPublishedDate());
        }

        if(subject.getSubjectType() != null) {
            updateData.set("subjectType", subject.getSubjectType());
        }

        if(subject.getSubjectSubType() != null) {
            updateData.set("subjectSubType", subject.getSubjectSubType());
        }

        if(subject.getSongIds() != null) {
            updateData.set("songIds", subject.getSongIds());
        }

        UpdateResult result =mongoTemplate.updateFirst(query, updateData, Subject.class);
        return result != null && result.getModifiedCount() > 0;
    }

    @Override
    public List<Subject> getAll(){
        return mongoTemplate.findAll(Subject.class);
    }
}
