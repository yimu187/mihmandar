package tech.mihmandar.core.data.user.dao;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import tech.mihmandar.core.common.dao.BaseDao;
import tech.mihmandar.core.data.user.domain.User;

/**
 * Created by Murat on 9/11/2017.
 */
@Repository
public class UserDao extends BaseDao<User> {

    public UserDao() {
        super(User.class);
    }

    public User findUserByUsername(String userName) {
        String hql = "select user from User user where user.email = :username";
        Query query = getCurrentSession().createQuery(hql);
        query.setParameter("username", userName);

        return (User) query.uniqueResult();
    }
}
