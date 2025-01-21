package com.xiangkai.community.alpha.transaction;

import com.xiangkai.community.dao.mapper.UserMapper;
import com.xiangkai.community.model.entity.User;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.Date;

@Component
public class ProgrammaticTransactionWithTransactionManager {

    private final DataSourceTransactionManager transactionManager;

    private final DefaultTransactionDefinition transactionDefinition;

    private final UserMapper userMapper;

    public ProgrammaticTransactionWithTransactionManager(DataSourceTransactionManager transactionManager,
                                                         UserMapper userMapper) {

        this.transactionManager = transactionManager;

        // 设置事务属性
        this.transactionDefinition = new DefaultTransactionDefinition();
        this.transactionDefinition.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        this.transactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);

        this.userMapper = userMapper;
    }

    public Object updateWithTransactionManager() {

        TransactionStatus status = transactionManager.getTransaction(transactionDefinition);
        try {
            User user = new User()
                    .setUsername("测试用户5")
                    .setPassword("qqq111")
                    .setSalt("qqq111")
                    .setEmail("qqq111@sina.com")
                    .setType(1)
                    .setStatus(1)
                    .setActivationCode("qqq111")
                    .setHeaderUrl("https://static.nowcoder.com/images/head/notify.png")
                    .setCreateTime(new Date())
                    .build();
            userMapper.insertUser(user);
            transactionManager.commit(status);
        } catch (Exception e) {
           transactionManager.rollback(status);
        }
        return "Insert user with exception!";
    }


}
