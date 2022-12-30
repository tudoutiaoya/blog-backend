package com.zzqedu.blogbackend.mapper;

import com.zzqedu.blogbackend.dao.pojo.Tag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author zzq12
* @description 针对表【ms_tag】的数据库操作Mapper
* @createDate 2022-12-30 11:24:49
* @Entity com.zzqedu.blogbackend.dao.pojo.Tag
*/
public interface TagMapper extends BaseMapper<Tag> {

    List<Tag> findTagsByArticleId(Long id);

    List<Long> findHotTagIds(int limit);

    List<Tag> findTagsByIds(List<Long> tagIds);


}




