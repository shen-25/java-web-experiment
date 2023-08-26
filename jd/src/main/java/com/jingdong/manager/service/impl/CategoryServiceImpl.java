package com.jingdong.manager.service.impl;

import com.jingdong.manager.command.CategoryCommand;
import com.jingdong.manager.exception.BusinessException;
import com.jingdong.manager.exception.BusinessExceptionEnum;
import com.jingdong.manager.model.entity.Category;
import com.jingdong.manager.model.vo.CategoryKeyValueVo;
import com.jingdong.manager.service.CategoryService;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class CategoryServiceImpl implements CategoryService {
    private CategoryCommand categoryCommand = new CategoryCommand();
    @Override
    public void add(Category category) {
        Category category1 = categoryCommand.selectByCategoryName(category.getCategoryName());
        if (category1 != null) {
            throw new BusinessException(BusinessExceptionEnum.CATEGORY_NAME_EXIST);
        }
        category.setCreateTime(new Date());
        categoryCommand.insert(category);

    }

    @Override
    public void edit(Category category) {
        Category category1 = categoryCommand.selectById(category.getCategoryId());
        if (category1 == null) {
            throw new BusinessException(BusinessExceptionEnum.CATEGORY_NOT_EXIST);
        }
        Category category2 = categoryCommand.selectByCategoryName(category.getCategoryName());
        if (category2 != null && !category2.getCategoryId().equals(category.getCategoryId())) {
            throw new BusinessException(BusinessExceptionEnum.CATEGORY_NAME_EXIST);
        }
        category1.setCategoryName(category.getCategoryName());
        category1.setCategoryState(category.getCategoryState());
        category1.setCategoryOrder(category.getCategoryOrder());
        category1.setUpdateTime(new Date());
        categoryCommand.edit(category1);
    }

    @Override
    public void delete(Long categoryId) {
        Category category1 = categoryCommand.selectById(categoryId);
        if (category1 == null) {
            throw new BusinessException(BusinessExceptionEnum.CATEGORY_NOT_EXIST);
        }
        categoryCommand.deleteById(categoryId);

    }

    @Override
    public Map<String, Object> paging(String categoryName, Integer pageNum, Integer pageSize) {
        Map<String, Object> stringObjectMap = categoryCommand.selectPage(categoryName, pageNum, pageSize);
        return stringObjectMap;
    }

    @Override
    public List<CategoryKeyValueVo> selectCategoryKeyValue() {
        return categoryCommand.selectCategoryKeyValue();
    }
}
