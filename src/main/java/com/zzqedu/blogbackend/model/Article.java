package com.zzqedu.blogbackend.model;

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
    private Long id;

    private Integer commentCounts;

    private Long createDate;

    private String summary;

    private String title;

    private Integer viewCounts;

    private Integer weight;

    private Long authorId;

    private Long bodyId;

    private Integer categoryId;

}