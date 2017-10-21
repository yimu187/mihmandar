package tech.mihmandar.core.data.user.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tech.mihmandar.core.common.dto.UserDto;
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

    public String encryptPassword(String pass){
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(pass);
    }

    public boolean checkPassword(String passEntered, String passEncrypted){
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        boolean matched = passwordEncoder.matches(passEntered, passEncrypted);
        return matched;
    }

    public UserDto convertUserDtoByUser(User user) {
        UserDto userDto = new UserDto();
        userDto.setUserId(user.getId());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        String email = user.getEmail();
        userDto.setEmail(email.replaceAll(" ", ""));
        userDto.setLocation(user.getLocation());
        userDto.setBio(user.getBio());
        userDto.setGender(user.getGender());
        userDto.setWebsite(user.getWebsite());
        userDto.setPhone(user.getPhone());
        userDto.setTitle(user.getTitle());
        return userDto;
    }

    public void updateUserByUserDto(User user, UserDto userDto) {
        user = findById(user.getId());
        user.setFirstName(user.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setLocation(userDto.getLocation());
        user.setBio(userDto.getBio());
        user.setWebsite(userDto.getWebsite());
        user.setPhone(userDto.getPhone());
        user.setTitle(userDto.getTitle());
        user.setGender(userDto.getGender());
        user.setEmail(userDto.getEmail());
        save(user);
    }
}
