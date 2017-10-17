package tech.mihmandar.core.data.training.dao;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import tech.mihmandar.core.common.dao.BaseDao;
import tech.mihmandar.core.data.training.domain.TraininStepRelation;
import tech.mihmandar.core.data.training.domain.TrainingStep;

import java.util.List;

/**
 * Created by Murat on 10/9/2017.
 */
@Repository
public class TraininStepRelationDao extends BaseDao<TraininStepRelation> {

    public TraininStepRelationDao() {
        super(TraininStepRelation.class);
    }

    public List<TrainingStep> findAllTraininStepRelationByTrainingId(Long trainingId) {
        String hql = " select relation.trainingStep from TraininStepRelation relation " +
                " where relation.training.id = :trainingId order by relation.trainingStep.stepNo asc";
        Query query = getCurrentSession().createQuery(hql);
        query.setParameter("trainingId", trainingId);
        return query.list();
    }
}
