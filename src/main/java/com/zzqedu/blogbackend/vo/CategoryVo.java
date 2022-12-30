package com.zzqedu.blogbackend.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @TableName ms_category
 */

@Data
public class CategoryVo implements Serializable {
    private String id;

    private String avatar;

    private String categoryName;

    private String description;
}