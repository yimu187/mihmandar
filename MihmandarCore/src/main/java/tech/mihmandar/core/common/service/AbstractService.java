package tech.mihmandar.core.common.service;

import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import tech.mihmandar.core.common.dao.BaseDao;
import tech.mihmandar.core.common.entity.BaseEntity;

import java.util.List;

/**
 * Created by Murat on 9/11/2017.
 */
public abstract class AbstractService<E extends BaseEntity, D extends BaseDao<E>> {

    @Autowired
    D dao;

    private Class<D> daoClazz;

    protected AbstractService(Class<D> daoClazz) {
        this.daoClazz = daoClazz;
    }

    public D getDao() {
        return dao;
    }

    public E findById(long id) {
        return dao.findById(id);
    }

    public List<E> findAll() {
        return dao.findAll();
    }

    public List<E> findAllOrdered(Order order) {
        return dao.findAllOrdered(order);
    }

    public E save(E e) {
        return dao.merge(e);
    }

    public void delete(E e) {
        dao.delete(e);
    }
}
