package com.sky.mapper;/*
 *  @author 阮艳瑞
 *  @version 2.0

 */

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@SuppressWarnings("all")
@Mapper
public interface SetmealDishMapper {
    /*
    根据菜品ID查询套餐Id
     */
    //seLect setmeal_id from setmeal_dish where dish_id in (1,2,3,4)
    List<Long> getSetmealIdsByDishIds(List<Long> dishIds);


}
