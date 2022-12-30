package generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzqedu.blogbackend.dao.pojo.Category;
import generator.service.CategoryService;
import generator.mapper.CategoryMapper;
import org.springframework.stereotype.Service;

/**
* @author zzq12
* @description 针对表【ms_category】的数据库操作Service实现
* @createDate 2022-12-30 11:47:55
*/
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
    implements CategoryService{

}




