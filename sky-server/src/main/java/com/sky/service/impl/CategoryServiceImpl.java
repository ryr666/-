package com.sky.service.impl;/*
 *  @author 阮艳瑞
 *  @version 2.0

 */

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
@Slf4j
@SuppressWarnings("all")
public class CategoryServiceImpl implements CategoryService {

    @Autowired
   private CategoryMapper categoryMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;
    /*
    新增分类
     */

    @Override
    public void save(CategoryDTO categoryDTO) {
         Category category = new Category();
         //属性拷贝
        BeanUtils.copyProperties(categoryDTO,category);
        //设置账号的状态，默认禁用状态0
        category.setStatus(StatusConstant.DISABLE);
        //设置创造时间、修改时间、创建人、修改人
//        category.setCreateTime(LocalDateTime.now());
//        category.setUpdateTime(LocalDateTime.now());
//        category.setCreateUser(BaseContext.getCurrentId());
//        category.setUpdateUser(BaseContext.getCurrentId());
        categoryMapper.insert(category);
    }
     /*
    分类分页查询
     */

    @Override
    public PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO) {
        //利用分页插件获取当前页码,和每页记录数
        PageHelper.startPage(categoryPageQueryDTO.getPage(),categoryPageQueryDTO.getPageSize());
        //下一条sql进行分页，自动加入limit关键字分页
        Page<Category> page = categoryMapper.pageQuery(categoryPageQueryDTO);

         return new PageResult(page.getTotal(),page.getResult());
    }

    /*
    删除分类
     */

    @Override
    public void deleteById(Long id) {
        Integer count = dishMapper.countByCategoryId(id);
        if(count > 0){
            //当前分类下有菜品，不能删除
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_DISH);
        }

        //查询当前分类是否关联了套餐，如果关联了就抛出业务异常
        count = setmealMapper.countByCategoryId(id);
        if(count > 0){
            //当前分类下有菜品，不能删除
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_SETMEAL);
        }

        //删除分类数据
        categoryMapper.deleteById(id);
    }

        //根据id查询菜品

    @Override
    public Category getById(Long id) {

        return categoryMapper.getById(id);
    }


    /*
     * 根据id修改分类
     */

    @Override
    public void update(CategoryDTO categoryDTO) {

         Category category = new Category();
         //信息拷贝
        BeanUtils.copyProperties(categoryDTO,category);
//        category.setUpdateTime(LocalDateTime.now());
//        category.setUpdateUser(BaseContext.getCurrentId());
        categoryMapper.update(category);


    }
    //启用禁用分类

    @Override
    public void startOrStop(Integer status, Long id) {
         Category category = Category.builder()
                 .id(id)
                 .status(status)
                 .updateTime(LocalDateTime.now())
                 .updateUser(BaseContext.getCurrentId())
                 .build();
         categoryMapper.update(category);
    }
    //根据类型查询分类

    @Override
    public List<Category> list(Integer type) {

        return categoryMapper.list(type);
    }
}
