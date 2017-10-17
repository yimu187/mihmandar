package tech.mihmandar.core.data.training.dao;

import org.springframework.stereotype.Repository;
import tech.mihmandar.core.common.dao.BaseDao;
import tech.mihmandar.core.data.training.domain.Training;

/**
 * Created by Murat on 10/8/2017.
 */
@Repository
public class TrainingDao extends BaseDao<Training> {

    public TrainingDao() {
        super(Training.class);
    }
}
