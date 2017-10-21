package tech.mihmandar.core.data.training.dao;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import tech.mihmandar.core.common.dao.BaseDao;
import tech.mihmandar.core.common.enums.EnumAccessType;
import tech.mihmandar.core.data.training.domain.Training;

import java.util.List;

/**
 * Created by Murat on 10/8/2017.
 */
@Repository
public class TrainingDao extends BaseDao<Training> {

    public TrainingDao() {
        super(Training.class);
    }

    public List<Training> findAllAccessibleTrainingByUserIdStartIndexAndSize(Long userId, String searchText, int startWith, int size) {
        String hql = " select training from Training training " +
                " left join fetch training.user " +
                " where ( " +
                " (training.accessType is null or training.accessType = :genel or training.accessType = :ozel) " +
                " or (training.accessType = :gizli and training.user.id = :userId " +
                //TODO Buraya yetki mekanizması eklenecek
                " " +
                " ) " +
                " ) ";

        if (StringUtils.hasText(searchText)) {
            hql += "and (training.name like :searchText or training.description like :searchText)";

        }
        String orderBy = " order by training.id desc ";
        hql += orderBy;

        Query query = getCurrentSession().createQuery(hql);
        if(StringUtils.hasText(searchText)){
            query.setParameter("searchText", searchText + "%");
        }

        query.setParameter("userId", userId);
        query.setParameter("genel", EnumAccessType.PUBLIC);
        query.setParameter("ozel", EnumAccessType.PRIVATE);
        query.setParameter("gizli", EnumAccessType.SECRET);
        query.setFirstResult(startWith);
        query.setMaxResults(size);

        return query.list();
    }
}
