package com.zzqedu.blogbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzqedu.blogbackend.dao.dos.Archives;
import com.zzqedu.blogbackend.dao.pojo.Article;
import com.zzqedu.blogbackend.dao.pojo.SysUser;
import com.zzqedu.blogbackend.dao.mapper.ArticleMapper;
import com.zzqedu.blogbackend.service.ArticleService;
import com.zzqedu.blogbackend.service.SysUserService;
import com.zzqedu.blogbackend.service.TageService;
import com.zzqedu.blogbackend.vo.ArticleVo;
import com.zzqedu.blogbackend.vo.Result;
import com.zzqedu.blogbackend.vo.TagVo;
import com.zzqedu.blogbackend.vo.param.PageParams;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Resource
    ArticleMapper articleMapper;

    @Resource
    SysUserService sysUserService;

    @Resource
    TageService tageService;

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
            SysUser sysUser = sysUserService.findSysUserById(article.getId());
            articleVo.setAuthor(sysUser.getNickname());
        }
        if(isTags) {
            List<TagVo> tagVos = tageService.findTagsByArticleId(article.getId());
            articleVo.setTags(tagVos);
        }
        return articleVo;
    }
}
