package com.xiangkai.community.task;

import com.xiangkai.community.constant.CommunityConstant;
import com.xiangkai.community.model.entity.DiscussPost;
import com.xiangkai.community.service.DiscussPostService;
import com.xiangkai.community.service.LikeService;
import com.xiangkai.community.util.RedisUtil;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class ScoreJob implements Job, CommunityConstant {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScoreJob.class);

    private static Date epoch;

    private static final ThreadLocal<DateFormat> df = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    static {
        try {
            epoch = df.get().parse("2014-08-01 00:00:00");
        } catch (ParseException e) {
            LOGGER.error("解析纪元失败:\n" + e);
        }
    }

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private LikeService likeService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        String postScoreKey = RedisUtil.getPostScoreKey();

        BoundSetOperations<String, Object> operations = redisTemplate.boundSetOps(postScoreKey);

        if (operations.size() == 0) {
            LOGGER.info("没有要计算分数的帖子，结束任务！");
            return;
        }

        if (operations.size() != null) {
            LOGGER.info("开始计算帖子分数");
            while (operations.size() > 0) {
                refreshScore((Integer) operations.pop());
            }
            LOGGER.info("结束计算帖子分数");
        }
    }

    private void refreshScore(Integer id) {
        DiscussPost post = discussPostService.findDiscussPostById(id);
        if (post == null) {
            LOGGER.error("帖子不存在！");
            return;
        }

        boolean wonderful = post.getStatus() == 1;

        Integer commentCount = post.getCommentCount();

        Long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST, id);

        //计算权重
        double w = (wonderful ? 75 : 0) + commentCount * 10 + likeCount * 2;

        double score = Math.log10(Math.max(w, 1)) +
                (
                        (post.getCreateTime().getTime() - epoch.getTime())
                                / (1.0 * 3600 * 1000 * 24)
                );

        discussPostService.updateScore(id, score);
    }
}
