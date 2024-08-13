package com.xiangkai.community.controller;

import com.xiangkai.community.constant.CommunityConstant;
import com.xiangkai.community.model.entity.DiscussPost;
import com.xiangkai.community.model.entity.Page;
import com.xiangkai.community.model.entity.User;
import com.xiangkai.community.model.vo.SearchVO;
import com.xiangkai.community.service.DiscussPostService;
import com.xiangkai.community.service.ElasticSearchService;
import com.xiangkai.community.service.LikeService;
import com.xiangkai.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ElasticSearchController implements CommunityConstant {

    @Autowired
    private UserService userService;

    @Autowired
    private ElasticSearchService elasticSearchService;

    @Autowired
    private LikeService likeService;

    @RequestMapping(path = "/search", method = RequestMethod.GET)
    public String search(Model model, Page page, String keyword) {

        List<DiscussPost> posts = elasticSearchService.search(keyword,
                page.getCurrent() - 1, page.getLimit());

        List<SearchVO> searchResults = new ArrayList<>();

        if (!CollectionUtils.isEmpty(posts)) {
            for (DiscussPost post : posts) {
                SearchVO vo = new SearchVO();
                vo.setPost(post);

                User user = userService.findUserById(post.getUserId());
                vo.setUser(user);

                Long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST, post.getId());
                vo.setLikeCount(likeCount);

                searchResults.add(vo);
            }
        }

        model.addAttribute("searchResults", searchResults);
        model.addAttribute("keyword", keyword);

        page.setPath("/search?keyword=" + keyword);
        page.setRows(CollectionUtils.isEmpty(posts) ? 0 : posts.size());

        return "/site/search";
    }

}
