package tech.mihmandar.core.trainings.tutorial;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import tech.mihmandar.core.base.BaseITCase;
import tech.mihmandar.core.trainings.tutorial.dao.TutSubscriberDao;
import tech.mihmandar.core.trainings.tutorial.domain.TutSubscriber;
import tech.mihmandar.core.trainings.tutorial.enums.EnumTutAccountType;

import java.util.List;

/**
 * Created by MURAT YILMAZ on 11/4/2016.
 */
public class SimpleTutorialITCase extends BaseITCase {

    @Autowired
    TutSubscriberDao tutSubscriberDao;

    @Test
    public void findAllSubscriber(){
        List<TutSubscriber> all = tutSubscriberDao.findAll();
        Assert.assertNotNull(all);
    }

    @Test
    public void saveTutSubscriber(){
        TutSubscriber tutSubscriber = new TutSubscriber();
        tutSubscriber.setAccountType(EnumTutAccountType.NORMAL);
        tutSubscriber.setEmail("yimu187@gmail.com");
        tutSubscriber.setUsername("yimu187");
        tutSubscriber.setPassword("yimu187");
        tutSubscriber = tutSubscriberDao.merge(tutSubscriber);
        Assert.assertNotNull(tutSubscriber.getId());
    }
}
