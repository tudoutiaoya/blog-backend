package com.zzqedu.blogbackend.service;

import com.zzqedu.blogbackend.vo.Result;
import com.zzqedu.blogbackend.vo.param.CommentParam;

public interface CommentService {
    Result getArticleComments(String id);

    Result createComment(CommentParam commentParam);

}
