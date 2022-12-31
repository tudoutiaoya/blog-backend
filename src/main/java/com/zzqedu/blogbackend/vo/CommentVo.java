package com.zzqedu.blogbackend.vo;

import lombok.Data;

import java.util.List;

@Data
public class CommentVo {
    // id
    private String id;

    private String content;

    // createDate
    private String createDate;

    private UserVo author;

    private List<CommentVo> childrens;

    private UserVo toUser;

    private Integer level;
}
