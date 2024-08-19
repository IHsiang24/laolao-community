package com.xiangkai.community.event.impl;

import com.xiangkai.community.constant.CommunityConstant;
import com.xiangkai.community.event.AbstractEventHandler;
import com.xiangkai.community.model.entity.Event;
import com.xiangkai.community.service.ElasticSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DeleteEventHandler extends AbstractEventHandler implements CommunityConstant {

    @Autowired
    private ElasticSearchService elasticSearchService;

    @Override
    public void handle(Event event) {
        elasticSearchService.deletePostById(event.getEntityId());
    }

    @Override
    public String getType() {
        return TOPIC_DELETE;
    }
}
