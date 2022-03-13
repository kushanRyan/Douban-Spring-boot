package fm.douban.service;

import fm.douban.model.ULInfo;
import fm.douban.param.UserQueryParam;
import org.springframework.data.domain.Page;

public interface UserService {


    ULInfo add(ULInfo user);

    ULInfo get(String id);

    Page<ULInfo> list(UserQueryParam param);

    boolean modify(ULInfo user);


    boolean delete(String id);
}
