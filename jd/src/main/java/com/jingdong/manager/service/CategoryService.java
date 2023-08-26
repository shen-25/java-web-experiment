package com.jingdong.manager.service;

import com.jingdong.manager.model.entity.Category;
import com.jingdong.manager.model.vo.CategoryKeyValueVo;

import java.util.List;
import java.util.Map;

public interface CategoryService {

    public void add(Category category);
    public void edit(Category category);

    public void delete(Long  categoryId);

    public Map<String, Object> paging(String categoryName, Integer pageNum, Integer pageSize);

    public List<CategoryKeyValueVo> selectCategoryKeyValue();
}
