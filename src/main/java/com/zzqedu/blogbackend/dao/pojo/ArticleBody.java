package com.zzqedu.blogbackend.dao.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * @TableName ms_article_body
 */

@Data
public class ArticleBody implements Serializable {
    private Long id;

    private String content;

    private String contentHtml;

    private Long articleId;

    private static final long serialVersionUID = 1L;
}