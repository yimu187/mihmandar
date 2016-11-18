package tech.mihmandar.core.common.entity;

import javax.persistence.MappedSuperclass;

/**
 * Created by MURAT YILMAZ on 11/4/2016.
 */
@MappedSuperclass
public abstract class BaseEntity implements BaseModel{

    public abstract String toString();
}
