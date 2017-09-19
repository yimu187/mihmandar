package tech.mihmandar.core.data.user.dao;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import tech.mihmandar.core.common.dao.BaseDao;
import tech.mihmandar.core.data.user.domain.User;
import tech.mihmandar.core.data.user.domain.UserToken;

/**
 * Created by Murat on 9/14/2017.
 */
@Repository
public class UserTokenDao extends BaseDao<UserToken> {
    public UserTokenDao() {
        super(UserToken.class);
    }

    public UserToken findUseTokenByToken(String token) {
        String hql = " select userToken from UserToken userToken " +
                " left join fetch userToken.user user " +
                " where userToken.token = :token";
        Query query = getCurrentSession().createQuery(hql);
        query.setParameter("token", token);

        return (UserToken) query.uniqueResult();
    }

    public User findUserByTokenId(Long userTokenId) {
        String hql = " select user from UserToken token" +
                " left join token.user user " +
                " where token.id = :id ";

        Query query = getCurrentSession().createQuery(hql);
        query.setParameter("id", userTokenId);
        return (User) query.uniqueResult();
    }
}
