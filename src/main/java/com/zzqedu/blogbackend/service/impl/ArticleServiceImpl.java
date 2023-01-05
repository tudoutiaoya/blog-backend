package com.zzqedu.blogbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzqedu.blogbackend.dao.dos.Archives;
import com.zzqedu.blogbackend.dao.mapper.ArticleBodyMapper;
import com.zzqedu.blogbackend.dao.mapper.ArticleTagMapper;
import com.zzqedu.blogbackend.dao.pojo.*;
import com.zzqedu.blogbackend.dao.mapper.ArticleMapper;
import com.zzqedu.blogbackend.service.*;
import com.zzqedu.blogbackend.util.UserThreadLocal;
import com.zzqedu.blogbackend.vo.*;
import com.zzqedu.blogbackend.vo.param.ArticleBodyParam;
import com.zzqedu.blogbackend.vo.param.ArticleParam;
import com.zzqedu.blogbackend.vo.param.PageParams;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ArticleServiceImpl implements ArticleService {

    @Resource
    ArticleMapper articleMapper;

    @Resource
    SysUserService sysUserService;

    @Resource
    TageService tageService;

    @Resource
    ArticleBodyMapper articleBodyMapper;

    @Resource
    CategoryService categoryService;

    @Resource
    ThreadService threadService;

    @Resource
    ArticleTagMapper articleTagMapper;

    /**
     * 查询文章列表  查文章、作者信息、tags信息  因为Mybatis-plus 不支持from_unixtime 函数，所以重写这个sql
     * @param pageParams
     * @return
     */
    @Override
    public List<ArticleVo> listArticlePage(PageParams pageParams) {
        Page<Article> page = new Page<>(pageParams.getPage(),pageParams.getPageSize());
        // 加入 查询条件 按照分类id  标签 id
        // 加入 文章 属于 特定分类
        page = articleMapper.listArticleWithDate(page,
                pageParams.getCategoryId(),
                pageParams.getTagId(),
                pageParams.getYear(),
                pageParams.getMonth());
        return copyList(page.getRecords(),true, true);
    }


    /**
     * 查询文章列表  查文章、作者信息、tags信息
     */
    // @Override
    // public List<ArticleVo> listArticlePage(PageParams pageParams) {
    //     log.info("查询条件 分类id 是否为空 {}", (pageParams.getCategoryId() == null));
    //     log.info("查询条件 标签id 是否为空 {}", (pageParams.getTagId() == null));
    //     Page<Article> page = new Page<>(pageParams.getPage(),pageParams.getPageSize());
    //     // 加入 查询条件 按照分类id  标签 id
    //     // 加入 文章 属于 特定分类
    //     LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
    //     if(pageParams.getCategoryId() != null) {
    //         queryWrapper.eq(Article::getCategoryId, pageParams.getCategoryId());
    //     }
    //     // 加入 文章 属于 特定标签
    //     if(pageParams.getTagId() != null) {
    //         LambdaQueryWrapper<ArticleTag> articleTagQueryWrapper = new LambdaQueryWrapper<>();
    //         articleTagQueryWrapper.eq(ArticleTag::getTagId, pageParams.getTagId());
    //         List<ArticleTag> articleTags = articleTagMapper.selectList(articleTagQueryWrapper);
    //         List<Long> articlesWithSpecialTag = new ArrayList<>();
    //         for (ArticleTag articleTag : articleTags) {
    //             articlesWithSpecialTag.add(articleTag.getArticleId());
    //         }
    //         if(articlesWithSpecialTag.size() > 0) {
    //             // and id in (1,2,3)
    //             queryWrapper.in(Article::getId, articlesWithSpecialTag);
    //         }
    //     }
    //
    //     page = articleMapper.selectPage(page,queryWrapper);
    //     return copyList(page.getRecords(),true, true);
    // }

    @Override
    public Result hotArticles(int limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getViewCounts);
        queryWrapper.select(Article::getId,Article::getTitle);
        queryWrapper.last("limit " + limit);
        List<Article> articles = articleMapper.selectList(queryWrapper);
        return Result.success(copyList(articles,false,false));
    }

    @Override
    public Result newArticles(int limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getCreateDate);
        queryWrapper.select(Article::getId, Article::getTitle);
        queryWrapper.last("limit " + limit);
        List<Article> articles = articleMapper.selectList(queryWrapper);
        return Result.success(copyList(articles,false,false));
    }

    @Override
    public Result listArchives() {
        List<Archives> articles = articleMapper.listArchives();
        return Result.success(articles);
    }

    @Override
    public Result getArticleById(String id) {
        Article article = articleMapper.selectById(id);
        ArticleVo articleVo = copy(article, true, true, true, true);
        // 更新阅读数量
        //查看完文章了，新增阅读数，有没有问题呢？
        //查看完文章之后，本应该直接返回数据了，这时候做了一个更新操作，更新时加写锁，阻塞其他的读操作，性能就会比较低
        // 更新 增加了此次接口的 耗时 如果一旦更新出问题，不能影响 查看文章的操作
        //线程池  可以把更新操作 扔到线程池中去执行，和主线程就不相关了
        threadService.updateArticleViewCount(articleMapper, article);
        return Result.success(articleVo);
    }

    @Override
    public Result findArticleById(Long articleId) {
        Article article = articleMapper.selectById(articleId);
        ArticleVo articleVo = copy(article, true, true, true, true);
        return Result.success(articleVo);
    }


    /**
     * 发表文章
     * @return 文章id
     */
    // @Transactional // 加上事务
    // @Override
    // public Result publishArticle(ArticleParam articleParam) {
    //     SysUser sysUser = UserThreadLocal.get();
    //     // 插入文章
    //     Article article = new Article();
    //     article.setCommentCounts(0);
    //     article.setCreateDate(System.currentTimeMillis());
    //     article.setSummary(articleParam.getSummary());
    //     article.setTitle(articleParam.getTitle());
    //     article.setViewCounts(0);
    //     article.setWeight(Article.Article_Common);
    //     article.setAuthorId(sysUser.getId());
    //     article.setCategoryId(Long.parseLong(articleParam.getCategory().getId()));
    //     articleMapper.insert(article);
    //
    //     // 还有 文章内容表 文章-标签表
    //     ArticleBodyParam body = articleParam.getBody();
    //     // 文章内容
    //     ArticleBody articleBody = new ArticleBody();
    //     articleBody.setContent(body.getContent());
    //     articleBody.setContentHtml(body.getContentHtml());
    //     articleBody.setArticleId(article.getId());
    //     articleBodyMapper.insert(articleBody);
    //
    //     // 文章-标签表
    //     List<TagVo> tags = articleParam.getTags();
    //     if(tags != null) {
    //         for (TagVo tagVo : tags) {
    //             ArticleTag articleTag = new ArticleTag();
    //             articleTag.setArticleId(article.getId());
    //             articleTag.setTagId(Long.parseLong(tagVo.getId()));
    //             articleTagMapper.insert(articleTag);
    //         }
    //     }
    //
    //     // 更新操作 设置文章内容id 分类id
    //     article.setBodyId(articleBody.getId());
    //     articleMapper.updateById(article);
    //
    //     Map<String,String> map = new HashMap<>();
    //     map.put("id",article.getId().toString());
    //     return Result.success(map);
    // }


    @Transactional // 加上事务
    @Override
    public Result publishArticle(ArticleParam articleParam) {
        //此接口 要加入到登录拦截当中
        SysUser sysUser = UserThreadLocal.get();
        /**
         * 1. 发布文章 目的 构建Article对象
         * 2. 作者id  当前的登录用户
         * 3. 标签  要将标签加入到 关联列表当中
         * 4. body 内容存储 article bodyId
         */
        // 插入文章
        Article article = new Article();
        boolean isEdit = false;
        if(articleParam.getId() != null) {
            // 是编辑 设置内容
            isEdit = true;
            article.setId(articleParam.getId());
            article.setTitle(articleParam.getTitle());
            article.setSummary(articleParam.getSummary());
            article.setCategoryId(Long.parseLong(articleParam.getCategory().getId()));
            articleMapper.updateById(article);
        } else {
            article.setAuthorId(sysUser.getId());
            article.setTitle(articleParam.getTitle());
            article.setSummary(articleParam.getSummary());
            article.setCategoryId(Long.parseLong(articleParam.getCategory().getId()));

            article.setCreateDate(System.currentTimeMillis());
            article.setCommentCounts(0);
            article.setViewCounts(0);
            article.setWeight(Article.Article_Common);
            // 插入之后 会生成一个文章id
            articleMapper.insert(article);
        }

        // 文章内容
        if(isEdit) {
            ArticleBodyParam body = articleParam.getBody();

            ArticleBody articleBody = new ArticleBody();
            articleBody.setArticleId(article.getId());
            articleBody.setContent(body.getContent());
            articleBody.setContentHtml(body.getContentHtml());
            LambdaQueryWrapper<ArticleBody> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(ArticleBody::getArticleId,article.getId());
            articleBodyMapper.update(articleBody, queryWrapper);
        } else {
            ArticleBodyParam body = articleParam.getBody();

            ArticleBody articleBody = new ArticleBody();
            articleBody.setArticleId(article.getId());
            articleBody.setContent(body.getContent());
            articleBody.setContentHtml(body.getContentHtml());
            articleBodyMapper.insert(articleBody);

            // 更新操作 设置文章内容id 分类id
            article.setBodyId(articleBody.getId());
            articleMapper.updateById(article);
        }


        // 文章-标签表
        List<TagVo> tags = articleParam.getTags();
        if(tags != null) {
            for (TagVo tagVo : tags) {
                Long articleId = article.getId();
                if(isEdit) {
                    // 先删除
                    LambdaQueryWrapper<ArticleTag> queryWrapper = Wrappers.lambdaQuery();
                    queryWrapper.eq(ArticleTag::getArticleId, articleId);
                    articleTagMapper.delete(queryWrapper);
                }
                ArticleTag articleTag = new ArticleTag();
                articleTag.setArticleId(articleId);
                articleTag.setTagId(Long.parseLong(tagVo.getId()));
                articleTagMapper.insert(articleTag);
            }
        }

        // 发送消息到mq中
        if(isEdit) {
            // 发送一条消息到mq, 文章更新了，先更后删解决缓存不一致
        }

        // 返回结果
        Map<String,String> map = new HashMap<>();
        map.put("id",article.getId().toString());
        return Result.success(map);
    }


    private List<ArticleVo> copyList(List<Article> records, boolean isAuthor, boolean isTags) {
        List<ArticleVo> articleVoList =  new ArrayList<>();
        for (Article record : records) {
            ArticleVo articleVo = copy(record,isAuthor, isTags);
            articleVoList.add(articleVo);
        }
        return articleVoList;
    }

    private ArticleVo copy(Article article, boolean isAuthor, boolean isTags) {
        ArticleVo articleVo = new ArticleVo();
        BeanUtils.copyProperties(article,articleVo);
        articleVo.setId(article.getId().toString());
        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        // 作者信息 标签信息不一定所有接口都需要
        if(isAuthor) {
            SysUser sysUser = sysUserService.findSysUserById(article.getAuthorId());
            UserVo userVo = new UserVo();
            userVo.setNickname(sysUser.getNickname());
            userVo.setAvatar(sysUser.getAvatar());
            userVo.setId(sysUser.getId().toString());
            articleVo.setAuthor(userVo);
        }
        if(isTags) {
            List<TagVo> tagVos = tageService.findTagsByArticleId(article.getId());
            articleVo.setTags(tagVos);
        }
        return articleVo;
    }

    private ArticleVo copy(Article article, boolean isAuthor, boolean isTags, boolean isBody, boolean isCategory) {
        ArticleVo articleVo = new ArticleVo();
        BeanUtils.copyProperties(article,articleVo);
        articleVo.setId(article.getId().toString());
        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        // 作者信息 标签信息不一定所有接口都需要
        if(isAuthor) {
            SysUser sysUser = sysUserService.findSysUserById(article.getAuthorId());
            UserVo userVo = new UserVo();
            userVo.setNickname(sysUser.getNickname());
            userVo.setAvatar(sysUser.getAvatar());
            userVo.setId(sysUser.getId().toString());
            articleVo.setAuthor(userVo);
        }
        if(isTags) {
            List<TagVo> tagVos = tageService.findTagsByArticleId(article.getId());
            articleVo.setTags(tagVos);
        }
        if(isBody) {
            ArticleBodyVo articleBodyVo = findArticleBodyById(article.getBodyId());
            articleVo.setBody(articleBodyVo);
        }
        if(isCategory) {
          Category category = categoryService.findCategoryById(article.getCategoryId());
          CategoryVo categoryVo = new CategoryVo();
          BeanUtils.copyProperties(category,categoryVo);
          categoryVo.setId(category.getId().toString());
          articleVo.setCategory(categoryVo);
        }
        return articleVo;
    }



    private ArticleBodyVo findArticleBodyById(Long id) {
        ArticleBody articleBody = articleBodyMapper.selectById(id);
        ArticleBodyVo articleBodyVo = new ArticleBodyVo();
        articleBodyVo.setContent(articleBody.getContent());
        return articleBodyVo;
    }

}
