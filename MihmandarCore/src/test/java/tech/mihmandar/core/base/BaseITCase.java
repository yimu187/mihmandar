package tech.mihmandar.core.base;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by MURAT YILMAZ on 11/4/2016.
 */
@Transactional(rollbackFor = Exception.class)
@RunWith(SpringJUnit4ClassRunner.class)
// defaultRollback = false olursa db ye commit edilir.
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
@ContextConfiguration("classpath:applicationcontext-servicetest.xml")
public abstract class BaseITCase extends AbstractTransactionalJUnit4SpringContextTests {

    @Ignore
    public void shouldInitSpring() {

    }

}
