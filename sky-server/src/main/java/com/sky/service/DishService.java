package com.sky.service;/*
 *  @author 阮艳瑞
 *  @version 2.0

 */

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

@SuppressWarnings("all")
public interface DishService {
    //新增菜品和对应的口味
    void saveWithFlavor(DishDTO dishDTO);

    //分页查询
    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);
    //批量删除菜品
    void deleteBatch(List<Long> ids);

    //根据id查询菜品
    DishVO getByIdWithFlavor(Long id);



    //修改菜品
    void updateWithFlavor(DishDTO dishDTO);
    //
    void startOrStop(Integer status, Long id);
}
