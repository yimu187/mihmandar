package tech.mihmandar.core.data.training.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.mihmandar.core.common.service.AbstractService;
import tech.mihmandar.core.data.training.dao.TraininStepRelationDao;
import tech.mihmandar.core.data.training.domain.TraininStepRelation;
import tech.mihmandar.core.data.training.domain.TrainingStep;

import java.util.List;

/**
 * Created by Murat on 10/9/2017.
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TraininStepRelationService extends AbstractService<TraininStepRelation, TraininStepRelationDao> {
    protected TraininStepRelationService() {
        super(TraininStepRelationDao.class);
    }

    public List<TrainingStep> findAllTraininStepRelationByTrainingId(Long trainingId) {
        return getDao().findAllTraininStepRelationByTrainingId(trainingId);
    }
}
