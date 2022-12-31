package com.zzqedu.blogbackend.vo.param;

import com.zzqedu.blogbackend.vo.CategoryVo;
import com.zzqedu.blogbackend.vo.TagVo;
import lombok.Data;

import java.util.List;

@Data
public class ArticleParam {

    private Long id;

    private String title;

    private String summary;

    private ArticleBodyParam body;

    private CategoryVo category;

    private List<TagVo> tags;

}
