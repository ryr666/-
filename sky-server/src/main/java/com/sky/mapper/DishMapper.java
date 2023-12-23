package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
@SuppressWarnings("all")
public interface DishMapper {

    /**
     * 根据分类id查询菜品数量
     * @param categoryId
     * @return
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    //插入菜品数据
    @AutoFill(value = OperationType.INSERT)
    void insert(Dish dish);

    //利用分页插件获取当前页码,和每页记录数
    Page<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);

   // 查询菜品
    @Select("select * from dish where id = #{id}")
    Dish getById(Long id);
    //根据主键删除菜品数据
    @Delete("delete from dish where id = #{id}")
    void deleteById(Long id);

    //项目优化
    //根据菜品id集合批量删除菜品数据工dishMapper.deleteByIdo;
//    void deleteByIds(List<Long> ids);
    //修改菜品表基本信息
    @AutoFill(OperationType.UPDATE)
    void update(Dish dish);
}
