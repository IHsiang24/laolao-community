package com.xiangkai.community.service;

import com.xiangkai.community.dao.repository.elasticsearch.DiscussPostElasticsearchRepository;
import com.xiangkai.community.model.entity.DiscussPost;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ElasticSearchService {

    @Autowired
    private DiscussPostElasticsearchRepository repository;

    @Autowired
    private ElasticsearchRestTemplate template;

    public void savePost(DiscussPost post) {
        repository.save(post);
    }

    public void deletePostById(Integer id) {
        repository.deleteById(id);
    }

    public List<DiscussPost> search(String keyword, Integer current, Integer limit) {

        Query query = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.multiMatchQuery(keyword, "title", "content"))
                .withSort(SortBuilders.fieldSort("type").order(SortOrder.DESC))
                .withSort(SortBuilders.fieldSort("score").order(SortOrder.DESC))
                .withSort(SortBuilders.fieldSort("createTime").order(SortOrder.DESC))
                .withPageable(PageRequest.of(current, limit))
                .withHighlightFields(
                        new HighlightBuilder.Field("title").preTags("<em>").postTags("</em>"),
                        new HighlightBuilder.Field("content").preTags("<em>").postTags("</em>")
                )
                .build();

        SearchHits<DiscussPost> search = template.search(query, DiscussPost.class);

        List<DiscussPost> posts = new ArrayList<>();

        for (SearchHit<DiscussPost> hit : search) {

            DiscussPost post = hit.getContent();

            Map<String, List<String>> highlightFields = hit.getHighlightFields();

            List<String> titles = highlightFields.get("title");
            if (!CollectionUtils.isEmpty(titles)) {
                post.setTitle(String.join(",", titles));
            }

            List<String> contents = highlightFields.get("content");
            if (!CollectionUtils.isEmpty(contents)) {
                post.setContent(String.join(",", contents));
            }

            posts.add(post);
        }
        return posts;
    }
}
