package com.zzqedu.blogbackend.service;

import com.zzqedu.blogbackend.vo.ArticleVo;
import com.zzqedu.blogbackend.vo.param.PageParams;

import java.util.List;

public interface ArticleService {
    List<ArticleVo> listArticlePage(PageParams pageParams);

}
