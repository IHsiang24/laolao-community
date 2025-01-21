package com.xiangkai.community.alpha.transaction;

import com.xiangkai.community.dao.mapper.UserMapper;
import com.xiangkai.community.model.entity.User;
import org.reactivestreams.Publisher;
import org.springframework.transaction.ReactiveTransaction;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.reactive.TransactionCallback;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Flux;

import java.util.Date;

public class ProgrammaticTransactionWithTransactionalOperator {

    private final TransactionalOperator transactionalOperator;

    private final UserMapper userMapper;

    public ProgrammaticTransactionWithTransactionalOperator(ReactiveTransactionManager transactionManager,
                                                            UserMapper userMapper) {

        DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        transactionDefinition.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        transactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        this.transactionalOperator = TransactionalOperator.create(transactionManager, transactionDefinition);

        this.userMapper = userMapper;
    }

    public Object updateWithTransactionalOperator() {

        Flux<Object> flux = transactionalOperator.execute(new TransactionCallback<Object>() {
            @Override
            public Publisher<Object> doInTransaction(ReactiveTransaction reactiveTransaction) {

                try {
                    User user = new User()
                            .setUsername("测试用户4")
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
                    reactiveTransaction.setRollbackOnly();
                }
                return new Flux<Object>() {
                    @Override
                    public void subscribe(CoreSubscriber<? super Object> coreSubscriber) {

                    }
                };
            }
        });

        return flux.collectList().block();
    }

}
