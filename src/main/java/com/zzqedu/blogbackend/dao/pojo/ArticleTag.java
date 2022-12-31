package com.zzqedu.blogbackend.dao.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * @TableName ms_article_tag
 */
@Data
public class ArticleTag implements Serializable {
    private Long id;

    private Long articleId;

    private Long tagId;

    private static final long serialVersionUID = 1L;
}