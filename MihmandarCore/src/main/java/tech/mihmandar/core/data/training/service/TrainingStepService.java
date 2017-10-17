package tech.mihmandar.core.data.training.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.mihmandar.core.common.service.AbstractService;
import tech.mihmandar.core.data.training.dao.TrainingStepDao;
import tech.mihmandar.core.data.training.domain.TrainingStep;

/**
 * Created by Murat on 10/8/2017.
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TrainingStepService extends AbstractService<TrainingStep, TrainingStepDao> {
    protected TrainingStepService() {
        super(TrainingStepDao.class);
    }
}
