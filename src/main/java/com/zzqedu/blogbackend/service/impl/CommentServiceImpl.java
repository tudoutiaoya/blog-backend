package com.zzqedu.blogbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zzqedu.blogbackend.dao.mapper.ArticleBodyMapper;
import com.zzqedu.blogbackend.dao.mapper.ArticleMapper;
import com.zzqedu.blogbackend.dao.mapper.CommentMapper;
import com.zzqedu.blogbackend.dao.pojo.Article;
import com.zzqedu.blogbackend.dao.pojo.Comment;
import com.zzqedu.blogbackend.dao.pojo.SysUser;
import com.zzqedu.blogbackend.service.CommentService;
import com.zzqedu.blogbackend.service.SysUserService;
import com.zzqedu.blogbackend.service.ThreadService;
import com.zzqedu.blogbackend.util.UserThreadLocal;
import com.zzqedu.blogbackend.vo.CommentVo;
import com.zzqedu.blogbackend.vo.Result;
import com.zzqedu.blogbackend.vo.UserVo;
import com.zzqedu.blogbackend.vo.param.CommentParam;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;




@Service
public class CommentServiceImpl implements CommentService {

    @Resource
    CommentMapper commentMapper;

    @Resource
    SysUserService sysUserService;

    @Resource
    ArticleMapper articleMapper;

    @Override
    public Result getArticleComments(String id) {
        /**
         * 1.根据文章id 查询 评论列表 从comment表中查询 level 为1 ,按照时间排序
         * 2. 根据作者的id 查询作者的信息
         * 3. 判断 level 为 1 要去查询他有没有子评论
         * 4. 如果有 根据评论id 进行查询 parent_id
         */
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getArticleId, id)
                        .eq(Comment::getLevel, 1)
                                .orderByDesc(Comment::getCreateDate);
        List<Comment> comments = commentMapper.selectList(queryWrapper);
        List<CommentVo> commentVoList = copyList(comments);
        return Result.success(commentVoList);
    }

    @Override
    public Result createComment(CommentParam commentParam) {
        SysUser sysUser = UserThreadLocal.get();
        Comment comment = new Comment();

        comment.setContent(commentParam.getContent());
        comment.setCreateDate(System.currentTimeMillis());
        comment.setArticleId(commentParam.getArticleId());
        comment.setAuthorId(sysUser.getId());
        comment.setContent(commentParam.getContent());
        Long parent = commentParam.getParent();
        if(parent == null || parent == 0) {
            comment.setLevel(1);
        } else {
            comment.setLevel(2);
        }
        comment.setParentId(parent == null ? 0 : parent);
        Long toUid = commentParam.getToUserId();
        comment.setToUid(toUid == null ? 0 : toUid);
        commentMapper.insert(comment);

        // 更新评论数量
        UpdateWrapper<Article> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", comment.getArticleId());
        updateWrapper.setSql(true, "comment_counts=comment_counts+1");
        articleMapper.update(null,updateWrapper);

        return Result.success(copy(comment));
    }

    private List<CommentVo> copyList(List<Comment> comments) {
        List<CommentVo> commentVoList = new ArrayList<>();
        for (Comment comment : comments) {
            commentVoList.add(copy(comment));
        }
        return commentVoList;
    }

    private CommentVo copy(Comment comment) {
        CommentVo commentVo = new CommentVo();
        BeanUtils.copyProperties(comment,commentVo);
        commentVo.setId(comment.getId().toString());
        commentVo.setCreateDate(new DateTime(comment.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        // 作者信息
        UserVo userVo = sysUserService.getUserVoById(comment.getAuthorId());
        commentVo.setAuthor(userVo);
        // 子评论
        Integer level = comment.getLevel();
        if(1 == level) {
            Long id = comment.getId();
            List<CommentVo> commentVoList = findCommentsByParentId(id);
            commentVo.setChildrens(commentVoList);
        }
        // to User 给谁评论
        if(level > 1) {
            Long toUid = comment.getToUid();
            UserVo toUserVo = sysUserService.getUserVoById(toUid);
            commentVo.setToUser(toUserVo);
        }
        return commentVo;
    }

    private List<CommentVo> findCommentsByParentId(Long id) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getParentId, id)
                        .eq(Comment::getLevel, 2);
        return copyList(commentMapper.selectList(queryWrapper));
    }
}
