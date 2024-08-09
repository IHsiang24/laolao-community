package com.xiangkai.community.dao.repository;

import com.xiangkai.community.model.entity.DiscussPost;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface  DiscussPostRepository extends ElasticsearchRepository<DiscussPost, Integer> {
}
