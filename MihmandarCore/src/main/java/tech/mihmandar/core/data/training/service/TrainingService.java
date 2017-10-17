package tech.mihmandar.core.data.training.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.mihmandar.core.common.service.AbstractService;
import tech.mihmandar.core.data.training.dao.TrainingDao;
import tech.mihmandar.core.data.training.domain.Training;

/**
 * Created by Murat on 10/8/2017.
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TrainingService extends AbstractService<Training, TrainingDao> {
    protected TrainingService() {
        super(TrainingDao.class);
    }
}
