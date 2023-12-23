package com.sky.mapper;/*
 *  @author 阮艳瑞
 *  @version 2.0

 */


import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.enumeration.OperationType;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
@SuppressWarnings("all")
@Mapper
public interface CategoryMapper {
      /*
    新增员工
     */

    @Insert("insert into category(id, type, name, sort, status, create_time, update_time, create_user, update_user) " +
            "VALUES(#{type}, #{name}, #{sort}, #{status}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser}) ")
    @AutoFill(value = OperationType.INSERT)
    void insert(Category category);


    /*
    分类查询
     */
    Page<Category> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    /*
        删除分类
         */
    @Delete("delete from category where id = #{id}")
    void deleteById(Long id);

    @Select("select * from category where id = #{id}")
    Category getById(Long id);

    /*
     * 根据id修改分类
     */
    @AutoFill(value = OperationType.UPDATE)
    void update(Category category);
    //根据类型查询分类
    List<Category> list(Integer type);


    //启用禁用分类

}



