package com.sky.service.impl;/*
 *  @author 阮艳瑞
 *  @version 2.0

 */

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.Setmeal;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
@Service
@Slf4j
@Transactional
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    @Override
    @Transactional
    public void saveWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        //向菜品表插入一条数据
        dishMapper.insert(dish);
        //获取Insert语句生成的主键值
        Long dishId = dish.getId();

        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && flavors.size() > 0) {
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishId);
            });
            dishFlavorMapper.insertBatch(flavors);

        }
        //向口味表插入一条数据

    }
    //分类分页查询

    @Override
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        //利用分页插件获取当前页码,和每页记录数
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        //下一条sql进行分页，自动加入limit关键字分页
        Page<DishVO> page = dishMapper.pageQuery(dishPageQueryDTO);
        PageResult pageResult = new PageResult(page.getTotal(), page.getResult());
        return pageResult;
    }

    //批量删除菜品
    @Override
    public void deleteBatch(List<Long> ids) {
        //判断当前菜品是否能够删除---是否存在起售中的菜品??
        for (Long id : ids) {
           Dish dish = dishMapper.getById(id);
            if (dish.getStatus() == StatusConstant.ENABLE) {
                //当前菜品处于起售中，不能删除
                throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_DISH);
            }
        }
        //判断当前菜品是否能够删除---是否被套餐关联了??
        List<Long> setmealIds = setmealDishMapper.getSetmealIdsByDishIds(ids);
        if (setmealIds != null && setmealIds.size() > 0) {
            //当前菜品被套餐关联，不能删除
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }

        //删除菜品表中的菜品数据
        for (Long id : ids) {
            dishMapper.deleteById(id);
            //删除菜品关联的口味数据
            dishFlavorMapper.deleteByDishId(id);
        }
        //项目优化

//        //根据菜品id集合批量删除菜品数据工dishMapper.deleteByIdo;
//        // sql: delete from dish where id in (?,? , ?)
//        dishMapper.deleteByIds(ids);
//        //根据菜品id集合批量删除关联的口味数据
//        // sql: delete from dish_flavor where dish_id in (?,? , ?)
//        dishFlavorMapper.deleteByDishIds(ids);

    }
    //根据id查询菜品

    @Override
    public DishVO getByIdWithFlavor(Long id) {
        //根据id查询菜品数据
         Dish dish = dishMapper.getById(id);
        // 根据菜品id查询口味数据
        List<DishFlavor> dishFlavors = dishFlavorMapper.getByDishId(id);
        // 将查询到的数据封装到vo
         DishVO dishVO = new DishVO();
         BeanUtils.copyProperties(dish,dishVO);
         dishVO.setFlavors(dishFlavors);
         return dishVO;
    }
    //修改菜品


    @Override
    @Transactional
    public void updateWithFlavor(DishDTO dishDTO) {
         Dish dish = new Dish();
         BeanUtils.copyProperties(dishDTO,dish);
        //修改菜品表基本信息,只修改基本信息，不修改菜品信息，用dish
        dishMapper.update(dish);


        ////删除原有的口味数据
        dishFlavorMapper.deleteByDishId(dishDTO.getId());
        //重新插入口味数据
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && flavors.size() > 0) {
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishDTO.getId());
            });
            dishFlavorMapper.insertBatch(flavors);

        }

    }
    //启用禁用分类

    @Override
    public void startOrStop(Integer status, Long id) {
       Dish dish = Dish.builder()
               .id(id)
               .status(status)
               .build();
       dishMapper.update(dish);
        if (status == StatusConstant.DISABLE) {
            // 如果是停售操作，还需要将包含当前菜品的套餐也停售

            List<Long> dishIds = new ArrayList<>();
            dishIds.add(id);
            // select setmeal_id from setmeal_dish where dish_id in (?,?,?)
            List<Long> setmealIds = setmealDishMapper.getSetmealIdsByDishIds(dishIds);
            if (setmealIds != null && setmealIds.size() > 0) {
                for (Long setmealId : setmealIds) {
                    Setmeal setmeal = Setmeal.builder()
                            .id(setmealId)
                            .status(StatusConstant.DISABLE)
                            .build();
                    setmealMapper.update(setmeal);
                }
            }

        }
    }
}
