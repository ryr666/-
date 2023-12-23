package com.sky.service;/*
 *  @author 阮艳瑞
 *  @version 2.0

 */

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;

import java.util.List;

@SuppressWarnings("all")
public interface CategoryService {
    /*
    1.
    新增分类
     */
    void save(CategoryDTO categoryDTO);

     /*
     2.
    分类分页查询
     */

    PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    /*
    删除分类
     */
    void deleteById(Long id);

    /*
     * 根据id修改分类
     */
    void update(CategoryDTO categoryDTO);
    //启用禁用分类
    void startOrStop(Integer status, Long id);


    Category getById(Long id);
    //
    List<Category> list(Integer type);


}
