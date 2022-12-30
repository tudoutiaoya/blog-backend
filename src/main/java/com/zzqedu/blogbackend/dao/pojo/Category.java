package com.zzqedu.blogbackend.dao.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * @TableName ms_category
 */

@Data
public class Category implements Serializable {
    private Long id;

    private String avatar;

    private String categoryName;

    private String description;

    private static final long serialVersionUID = 1L;
}