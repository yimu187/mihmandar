package tech.mihmandar.core.trainings.tutorial.dao;

import org.springframework.stereotype.Repository;
import tech.mihmandar.core.common.dao.BaseDao;
import tech.mihmandar.core.trainings.tutorial.domain.TutSubscriber;

/**
 * Created by MURAT YILMAZ on 11/4/2016.
 */
@Repository
public class TutSubscriberDao extends BaseDao<TutSubscriber>{

    public TutSubscriberDao() {
        super(TutSubscriber.class);
    }
}
