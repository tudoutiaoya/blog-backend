package com.zzqedu.blogbackend.dao.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @TableName ms_article
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Article implements Serializable {

    private static final int Article_TOP = 1;

    private static final int Article_Common = 0;

    private Long id;

    private Integer commentCounts;

    private Long createDate;

    private String summary;

    private String title;

    private Integer viewCounts;

    private Integer weight = Article_Common;

    private Long authorId;

    private Long bodyId;

    private Integer categoryId;

}