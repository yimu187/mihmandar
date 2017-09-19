package tech.mihamdar.ui.presentation.util.common;

import org.junit.Assert;
import org.junit.Test;
import tech.mihmandar.core.data.user.domain.UserToken;
import tech.mihmandar.ui.presentation.common.CookieUtil;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Murat on 9/16/2017.
 */
public class CookieUtilTest {

    @Test
    public void testCalculationOfExpirationDate(){
        int expirationDurationInMınutes = 55;
        UserToken userToken = new UserToken();
        Calendar instance = Calendar.getInstance();
        int reqHour = instance.get(Calendar.HOUR_OF_DAY);
        int reqMin = instance.get(Calendar.MINUTE);
        Date time = instance.getTime();
        userToken.setTokenRequestTime(time);
        userToken.setExpirationMinutes(new Long(expirationDurationInMınutes));
        Date date = CookieUtil.calculateExpirationDate(userToken);

        Calendar expirationCalendar = Calendar.getInstance();
        expirationCalendar.setTime(date);
        int expHour = expirationCalendar.get(Calendar.HOUR_OF_DAY);
        int expMin = expirationCalendar.get(Calendar.MINUTE);

        int diff = ((expHour - reqHour) * 60) + (expMin - reqMin);
        Assert.assertTrue(diff == expirationDurationInMınutes);
    }
}
