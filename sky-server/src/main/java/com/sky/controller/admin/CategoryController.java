package com.sky.controller.admin;/*
 *  @author 阮艳瑞
 *  @version 2.0

 */

//import com.github.pagehelper.Page;
//import com.sky.dto.CategoryDTO;
//import com.sky.dto.CategoryPageQueryDTO;
//import com.sky.result.PageResult;
//import com.sky.result.Result;
//import com.sky.service.CategoryService;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;


import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SuppressWarnings("all")
@RestController
@RequestMapping("/admin/category")
@Api(tags = "分类相关接口")
@Slf4j
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /*
    新增员工
     */
    @PostMapping
    @ApiOperation(value = "新增分类")
    public Result save(@RequestBody CategoryDTO categoryDTO) {
        log.info("新增分类:{}", categoryDTO);
        categoryService.save(categoryDTO);
        return Result.success();


    }
    /*
    分类分页查询
     */

    @GetMapping("/page")
    @ApiOperation(value = "分类分页查询")
    public Result<PageResult> page(CategoryPageQueryDTO categoryPageQueryDTO) {
        log.info("菜单分页:{}", categoryPageQueryDTO);
        PageResult pageResult = categoryService.pageQuery(categoryPageQueryDTO);
        return Result.success(pageResult);

    }

    /*
    根据id删除员工
     */
    @DeleteMapping
    @ApiOperation(value = "删除分类")
    public Result deleteById(Long id) {
        log.info("删除分类:{}", id);
        categoryService.deleteById(id);
        return Result.success();
    }
    /*
    根据id查询分类
     */
    @GetMapping("/{id}")
    public Result<Category> getById(@PathVariable Long id){
        log.info("根据id查询分类,{}",id);
        Category category = categoryService.getById(id);
        return Result.success(category);
    }

    /*
     * 根据id修改分类
     */
    @PutMapping
    @ApiOperation("修改分类")
    public Result update(@RequestBody CategoryDTO categoryDTO) {
        log.info("修改分类,{}", categoryDTO);
        categoryService.update(categoryDTO);
        return Result.success();
    }

    //启用禁用分类
    @PostMapping("/status/{status}")
    @ApiOperation("启用禁用分类")
    public Result<String> startOrStop(@PathVariable("status") Integer status, Long id) {
        log.info("启用禁用分类:{},{}", status, id);
        categoryService.startOrStop(status, id);
        return Result.success();

    }
    //根据类型查询
    @GetMapping("/list")
    @ApiOperation("根据类型查询分类")
    public Result<List<Category>> list(Integer type){
        log.info("根据类型查询分类",type);
        List<Category> categoryList = categoryService.list(type);
        return Result.success(categoryList);


    }


}

