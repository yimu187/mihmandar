package tech.mihmandar.core.data.training.dao;

import org.springframework.stereotype.Repository;
import tech.mihmandar.core.common.dao.BaseDao;
import tech.mihmandar.core.data.training.domain.TrainingStep;

/**
 * Created by Murat on 10/8/2017.
 */
@Repository
public class TrainingStepDao extends BaseDao<TrainingStep> {
    public TrainingStepDao() {
        super(TrainingStep.class);
    }
}
