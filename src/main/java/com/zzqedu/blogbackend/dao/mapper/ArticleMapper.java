package com.zzqedu.blogbackend.dao.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzqedu.blogbackend.dao.dos.Archives;
import com.zzqedu.blogbackend.dao.pojo.Article;

import java.util.List;

public interface ArticleMapper extends BaseMapper<Article> {
    List<Archives> listArchives();

    Page<Article> listArticleWithDate(Page<Article> page, Long categoryId, Long tagId, String year, String month);

}
