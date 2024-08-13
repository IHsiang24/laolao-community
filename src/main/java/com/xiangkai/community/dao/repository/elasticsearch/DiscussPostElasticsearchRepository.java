package com.xiangkai.community.dao.repository.elasticsearch;

import com.xiangkai.community.model.entity.DiscussPost;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscussPostElasticsearchRepository extends ElasticsearchRepository<DiscussPost, Integer> {
}
