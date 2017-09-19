package tech.mihmandar.ui.presentation.common;

import com.vaadin.server.VaadinService;
import tech.mihmandar.core.common.enums.EnumYN;
import tech.mihmandar.core.data.user.domain.UserToken;
import tech.mihmandar.utility.Base64Coder;

import javax.servlet.http.Cookie;
import java.io.*;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Murat on 9/14/2017.
 */
public class CookieUtil {

    public final static String AUTHENTICATION_COOKIE_NAME = "userAuthenticationCookie";

    public static Object deserializeFromString(String s) throws IOException,
            ClassNotFoundException {
        byte[] data = Base64Coder.decode(s);
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
        Object o = ois.readObject();
        ois.close();
        return o;
    }

    public static String serializeToString(Serializable o) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(o);
        oos.close();
        return new String(Base64Coder.encode(baos.toByteArray()));
    }

    public static Cookie getAuthenticationCookie() {
        Cookie cookie = null;
        if(VaadinService.getCurrentRequest() != null) {

            Cookie[] cookies = VaadinService.getCurrentRequest().getCookies();
            for (Cookie ckie : cookies) {
                if (CookieUtil.AUTHENTICATION_COOKIE_NAME.equals(ckie.getName())) {
                    cookie = ckie;
                }
            }
        }
        return cookie;
    }

    public static void saveAuthenticationCookieToBrowser(String userName, String password, String token, EnumYN state) {
        CookieDto cookieDto = new CookieDto();
        cookieDto.setUsername(userName);
        cookieDto.setPassword(password);

        cookieDto.setToken(token);

        String result = null;
        try {
            result = CookieUtil.serializeToString(cookieDto);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Cookie cookie = new Cookie(CookieUtil.AUTHENTICATION_COOKIE_NAME, result);
        cookie.setMaxAge(60 * 60 * 24 * 365); // Store cookie for 1 year
        cookie.setPath(VaadinService.getCurrentRequest().getContextPath());
        VaadinService.getCurrentResponse().addCookie(cookie);
    }

    public static Date calculateExpirationDate(UserToken userToken){
        Date expTime;
        if(userToken == null || (userToken != null && (userToken.getExpirationMinutes() == null || userToken.getTokenRequestTime() == null))){
            expTime = new Date();
        }else{
            Date startDate = userToken.getTokenRequestTime();
            int minutes = userToken.getExpirationMinutes().intValue();
            Calendar instance = Calendar.getInstance();
            instance.setTime(startDate);
            instance.add(Calendar.MINUTE, minutes);
            expTime = instance.getTime();
        }

        return expTime;
    }

    public static void clearAuthenticationCookie() {
        CookieDto cookieDto = new CookieDto();
        String result = null;
        try {
            result = CookieUtil.serializeToString(cookieDto);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Cookie cookie = new Cookie(CookieUtil.AUTHENTICATION_COOKIE_NAME, result);
        cookie.setMaxAge(60 * 60 * 24 * 365); // Store cookie for 1 year
        cookie.setPath(VaadinService.getCurrentRequest().getContextPath());
        VaadinService.getCurrentResponse().addCookie(cookie);
    }
}
