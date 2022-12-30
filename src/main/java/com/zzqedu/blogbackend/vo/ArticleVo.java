package com.zzqedu.blogbackend.vo;

import com.zzqedu.blogbackend.dao.pojo.SysUser;
import lombok.Data;

import java.util.List;

@Data
public class ArticleVo {
    private String id;

    private Integer commentCounts;

    // 创建时间
    private String createDate;

    private String summary;

    private String title;

    private Integer viewCounts;

    private Integer weight;

    private String author;

    private Long bodyId;

    private List<TagVo> tags;

    // private List<CategoryVo> categorys;

}
