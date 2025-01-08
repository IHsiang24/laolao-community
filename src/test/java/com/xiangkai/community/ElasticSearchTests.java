package com.xiangkai.community;

import com.xiangkai.community.dao.mapper.DiscussPostMapper;
import com.xiangkai.community.dao.repository.elasticsearch.DiscussPostElasticsearchRepository;
import com.xiangkai.community.model.entity.DiscussPost;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class ElasticSearchTests {

    @Autowired
    private DiscussPostElasticsearchRepository repository;

    @Autowired(required = false)
    private DiscussPostMapper mapper;

    @Autowired
    private ElasticsearchRestTemplate template;

    @Test
    public void testAdd() {
        DiscussPost post = mapper.selectById(109);
        repository.save(post);
        List<DiscussPost> discussPosts = mapper.selectDiscussPosts(0, 110000, 40000, 0);
        for(DiscussPost post2 : discussPosts){
            repository.save(post2);
        }
    }

    @Test
    public void testDelete() {
        DiscussPost post = mapper.selectById(109);
        repository.delete(post);
    }

    @Test
    public void testUpdate() {
        DiscussPost post = mapper.selectById(109);
        post.setContent("1111");
        repository.save(post);
    }

    @Test
    public void testSelect() {
        DiscussPost post = mapper.selectById(109);
        DiscussPost post1 = repository.findById(post.getId()).get();
        System.out.println(post1);
    }

    @Test
    public void testSearch() {
        DiscussPost post1 = mapper.selectById(109);
        DiscussPost post2 = mapper.selectById(110);
        DiscussPost post3 = mapper.selectById(111);
        DiscussPost post4 = mapper.selectById(112);
        DiscussPost post5 = mapper.selectById(113);
        DiscussPost post6 = mapper.selectById(114);

        repository.save(post1);
        repository.save(post2);
        repository.save(post3);
        repository.save(post4);
        repository.save(post5);
        repository.save(post6);

        Query query = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.multiMatchQuery("因特网寒冬", "title", "content"))
                .withSort(SortBuilders.fieldSort("type").order(SortOrder.DESC))
                .withSort(SortBuilders.fieldSort("score").order(SortOrder.DESC))
                .withSort(SortBuilders.fieldSort("createTime").order(SortOrder.DESC))
                .withPageable(PageRequest.of(0, 10))
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
                post.setTitle(String.join(",", contents));
            }

            posts.add(post);
        }

        for (DiscussPost post : posts) {
            System.out.println(post);
        }
    }
}





