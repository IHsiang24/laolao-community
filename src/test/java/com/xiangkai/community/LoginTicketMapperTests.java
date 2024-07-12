package com.xiangkai.community;

import com.xiangkai.community.model.entity.LoginTicket;
import com.xiangkai.community.mapper.LoginTicketMapper;
import com.xiangkai.community.util.CommunityUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
public class LoginTicketMapperTests {

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Test
    void insert() {
        LoginTicket ticket = new LoginTicket()
                .setUserId(101)
                .setTicket(CommunityUtil.generateUUID())
                .setStatus(0)
                .setExpired(new Date())
                .build();

        loginTicketMapper.insertTicket(ticket);
    }

    @Test
    void select() {
        LoginTicket ticket = loginTicketMapper.selectByTicket("6145bb60edbb412b86388d1f0bd7c744");
        System.out.println(ticket.toString());
    }

    @Test
    void update() {
        loginTicketMapper.updateStatus(101, 1);
    }
}
