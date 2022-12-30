package com.zzqedu.blogbackend.dao.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * @TableName ms_sys_user
 */

@Data
public class SysUser implements Serializable {
    private Long id;

    private String account;

    private Boolean admin;

    private String avatar;

    private Long createDate;

    private Boolean deleted;

    private String email;

    private Long lastLogin;

    private String mobilePhoneNumber;

    private String nickname;

    private String password;

    private String salt;

    private String status;

    private static final long serialVersionUID = 1L;
}