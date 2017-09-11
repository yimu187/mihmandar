package tech.mihmandar.core.user;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import tech.mihmandar.core.data.user.domain.User;
import tech.mihmandar.core.data.user.service.UserService;

import java.util.List;

/**
 * Created by MURAT YILMAZ on 11/4/2016.
 */
public class UserServiceITCase {

    @Autowired
    UserService userService;

    @Test
    public void findAllSubscriber(){
        List<User> all = userService.findAll();
        Assert.assertNotNull(all);
    }

    @Test
    public void saveTutSubscriber(){
        User user = new User();
        user.setEmail("yimu187@gmail.com");
        user.setFirstName("Murat");
        user.setLastName("YILMAZ");
        user = userService.save(user);
        Assert.assertNotNull(user.getId());
    }
}
