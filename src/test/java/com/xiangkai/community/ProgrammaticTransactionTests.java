package com.xiangkai.community;

import com.xiangkai.community.alpha.transaction.ProgrammaticTransactionWithTransactionManager;
import com.xiangkai.community.alpha.transaction.ProgrammaticTransactionWithTransactionTemplate;
import com.xiangkai.community.alpha.transaction.ProgrammaticTransactionWithTransactionalOperator;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ProgrammaticTransactionTests {

    private static final Logger logger = LoggerFactory.getLogger(ProgrammaticTransactionTests.class);

    @Autowired
    private ProgrammaticTransactionWithTransactionTemplate programmaticTransactionWithTransactionTemplate;

    private ProgrammaticTransactionWithTransactionalOperator programmaticTransactionWithTransactionalOperator;

    @Autowired
    private ProgrammaticTransactionWithTransactionManager  programmaticTransactionWithTransactionManager;

    @Test
    public void testUpdateWithTransactionTemplate() {
        Object result = programmaticTransactionWithTransactionTemplate.updateWithTransactionTemplate();
        logger.info(result.toString());
    }

    @Test
    public void testUpdateWithTransactionalOperator() {
        Object result = programmaticTransactionWithTransactionalOperator.updateWithTransactionalOperator();
    }

    @Test
    public void testUpdateWithTransactionManager() {
        Object result = programmaticTransactionWithTransactionManager.updateWithTransactionManager();
        logger.info(result.toString());
    }

}
