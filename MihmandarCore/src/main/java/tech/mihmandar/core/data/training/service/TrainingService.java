package tech.mihmandar.core.data.training.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.mihmandar.core.common.service.AbstractService;
import tech.mihmandar.core.data.training.dao.TrainingDao;
import tech.mihmandar.core.data.training.domain.Training;

import java.util.List;

/**
 * Created by Murat on 10/8/2017.
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TrainingService extends AbstractService<Training, TrainingDao> {
    protected TrainingService() {
        super(TrainingDao.class);
    }

    public List<Training> findAllAccessibleTrainingByUserIdSearchTextStartIndexAndSize(Long userId, String searchText,int startWith, int size) {
        return getDao().findAllAccessibleTrainingByUserIdStartIndexAndSize(userId, searchText, startWith, size);
    }
}
