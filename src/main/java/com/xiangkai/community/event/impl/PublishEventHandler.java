package com.xiangkai.community.event.impl;

import com.xiangkai.community.constant.CommunityConstant;
import com.xiangkai.community.event.AbstractEventHandler;
import com.xiangkai.community.model.entity.DiscussPost;
import com.xiangkai.community.model.entity.Event;
import com.xiangkai.community.service.DiscussPostService;
import com.xiangkai.community.service.ElasticSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PublishEventHandler extends AbstractEventHandler implements CommunityConstant {

    @Autowired
    private ElasticSearchService elasticSearchService;

    @Autowired
    private DiscussPostService discussPostService;

    @Override
    public void handle(Event event) {
        DiscussPost post = discussPostService.findDiscussPostById(event.getEntityId());
        elasticSearchService.savePost(post);
    }

    @Override
    public String getType() {
        return TOPIC_PUBLISH;
    }
}
