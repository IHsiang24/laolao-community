package com.xiangkai.community.alpha.transaction;

import com.xiangkai.community.dao.mapper.UserMapper;
import com.xiangkai.community.model.entity.User;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Date;

@Component
public class ProgrammaticTransactionWithTransactionTemplate {

    private final TransactionTemplate transactionTemplate;

    private final UserMapper userMapper;

    public ProgrammaticTransactionWithTransactionTemplate(TransactionTemplate transactionTemplate,
                                                          UserMapper userMapper) {

        this.transactionTemplate = transactionTemplate;
        this.userMapper = userMapper;

        // 设置隔离级别和传播行为
        this.transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        this.transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
    }

    public Object updateWithTransactionTemplate() {

        return transactionTemplate.execute(new TransactionCallback<Object>() {
            @Override
            public Object doInTransaction(TransactionStatus transactionStatus) {
                try {
                    User user = new User()
                            .setUsername("测试用户3")
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
                    throw new RuntimeException("Insert user with exception!");
                } catch (Exception e) {
                    transactionStatus.setRollbackOnly();
                }
                return "Insert user with exception!";
            }
        });
    }
}
