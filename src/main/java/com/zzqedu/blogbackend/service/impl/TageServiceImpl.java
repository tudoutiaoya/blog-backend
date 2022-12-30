package com.zzqedu.blogbackend.service.impl;

import com.zzqedu.blogbackend.dao.pojo.Tag;
import com.zzqedu.blogbackend.dao.mapper.TagMapper;
import com.zzqedu.blogbackend.service.TageService;
import com.zzqedu.blogbackend.vo.TagVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class TageServiceImpl implements TageService {

    @Resource
    TagMapper tagMapper;

    @Override
    public List<TagVo> findTagsByArticleId(Long id) {
        List<Tag>  tags = tagMapper.findTagsByArticleId(id);
        return copyList(tags);
    }

    @Override
    public List<TagVo> hot(int limit) {
        List<Long> hotTags = tagMapper.findHotTagIds(limit);
        if(CollectionUtils.isEmpty(hotTags)) {
            return Collections.emptyList();
        }
        List<Tag> tagList = tagMapper.findTagsByIds(hotTags);
        return copyList(tagList);
    }

    private List<TagVo> copyList(List<Tag> tags) {
        List<TagVo> tagVos = new ArrayList<>();
        for (Tag tag : tags) {
            TagVo tagVo = copy(tag);
            tagVos.add(tagVo);
        }
        return tagVos;
    }

    private TagVo copy(Tag tag) {
        TagVo tagVo = new TagVo();
        BeanUtils.copyProperties(tag,tagVo);
        tagVo.setId(tag.getId().toString());
        return tagVo;
    }
}
