package com.xiangkai.community.alpha.bean;

import com.xiangkai.community.dao.mapper.CommentMapper;
import com.xiangkai.community.model.entity.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class BeanA implements CommentMapper {

    private BeanB beanB;

    public BeanA(BeanB beanB) {
        this.beanB = beanB;
    }

    public void m1() {
        System.out.println("BeanA.m1");
    }
}
