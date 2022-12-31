package com.zzqedu.blogbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
     * 查询文章列表  查文章、作者信息、tags信息
     * @param pageParams
     * @return
     */
    @Override
    public List<ArticleVo> listArticlePage(PageParams pageParams) {
        Page<Article> page = new Page<>(pageParams.getPage(),pageParams.getPageSize());
        page = articleMapper.selectPage(page,null);
        return copyList(page.getRecords(),true, true);
    }

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

    /**
     * 发表文章
     * @return 文章id
     */
    @Transactional // 加上事务
    @Override
    public Result publishArticle(ArticleParam articleParam) {
        SysUser sysUser = UserThreadLocal.get();
        // 插入文章
        Article article = new Article();
        article.setCommentCounts(0);
        article.setCreateDate(System.currentTimeMillis());
        article.setSummary(articleParam.getSummary());
        article.setTitle(articleParam.getTitle());
        article.setViewCounts(0);
        article.setWeight(Article.Article_Common);
        article.setAuthorId(sysUser.getId());
        article.setCategoryId(Long.parseLong(articleParam.getCategory().getId()));
        articleMapper.insert(article);

        // 还有 文章内容表 文章-标签表
        ArticleBodyParam body = articleParam.getBody();
        // 文章内容
        ArticleBody articleBody = new ArticleBody();
        articleBody.setContent(body.getContent());
        articleBody.setContentHtml(body.getContentHtml());
        articleBody.setArticleId(article.getId());
        articleBodyMapper.insert(articleBody);

        // 文章-标签表
        List<TagVo> tags = articleParam.getTags();
        if(tags != null) {
            for (TagVo tagVo : tags) {
                ArticleTag articleTag = new ArticleTag();
                articleTag.setArticleId(article.getId());
                articleTag.setTagId(Long.parseLong(tagVo.getId()));
                articleTagMapper.insert(articleTag);
            }
        }

        // 更新操作 设置文章内容id 分类id
        article.setBodyId(articleBody.getId());
        articleMapper.updateById(article);

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
