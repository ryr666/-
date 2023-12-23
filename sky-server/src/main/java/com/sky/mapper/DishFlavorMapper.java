package com.sky.mapper;/*
 *  @author 阮艳瑞
 *  @version 2.0

 */

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@SuppressWarnings("all")
@Mapper
public interface DishFlavorMapper {

    void insertBatch(List<DishFlavor> flavors);

    //根据菜品Id删除对应的口味数据
    @Delete("delete from dish_flavor where dish_id = #{dishId}")
    void deleteByDishId(Long dishId);

    //项目优化
//    void deleteByDishIds(List<Long> dishId);

    // 根据菜品id查询口味数据
    @Select("select * from dish_flavor where dish_id = #{dishId}")
    List<DishFlavor> getByDishId(Long dishId);
}
