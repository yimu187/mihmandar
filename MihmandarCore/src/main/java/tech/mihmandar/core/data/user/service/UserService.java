package tech.mihmandar.core.data.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.mihmandar.core.common.service.AbstractService;
import tech.mihmandar.core.data.user.dao.UserDao;
import tech.mihmandar.core.data.user.domain.User;

/**
 * Created by Murat on 9/11/2017.
 */
@Service
public class UserService extends AbstractService<User, UserDao>{

    protected UserService() {
        super(UserDao.class);
    }

    public User findUserByUsername(String userName) {
        return getDao().findUserByUsername(userName);
    }
}
