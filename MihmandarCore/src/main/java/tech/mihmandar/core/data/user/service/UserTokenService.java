package tech.mihmandar.core.data.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.mihmandar.core.common.enums.EnumYN;
import tech.mihmandar.core.common.service.AbstractService;
import tech.mihmandar.core.data.user.dao.UserTokenDao;
import tech.mihmandar.core.data.user.domain.User;
import tech.mihmandar.core.data.user.domain.UserToken;

import java.util.Date;

/**
 * Created by Murat on 9/14/2017.
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserTokenService extends AbstractService<UserToken, UserTokenDao> {

    @Autowired
    UserService userService;

    protected UserTokenService() {
        super(UserTokenDao.class);
    }

    public UserToken findUserTokenByToken(String token) {
        return getDao().findUseTokenByToken(token);
    }

    public void saveUserTokenByUserNameTokenStateAndExpMinutes(String userName, String token, EnumYN state, Long expMinutes) {
        UserToken userToken = new UserToken();
        User user = userService.findUserByUsername(userName);
        userToken.setUser(user);
        userToken.setToken(token);
        userToken.setExpirationMinutes(expMinutes);
        userToken.setTokenRequestTime(new Date());
        userToken.setState(state);
        save(userToken);
    }

    public User findUserByTokenId(Long userTokenId) {
        return getDao().findUserByTokenId(userTokenId);
    }
}
