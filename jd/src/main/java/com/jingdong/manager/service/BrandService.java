package com.jingdong.manager.service;

import com.jingdong.manager.model.entity.Brand;
import com.jingdong.manager.model.vo.BrandKeyValueVo;

import java.util.List;
import java.util.Map;

public interface BrandService {
    public void add(Brand brand);
    public void edit(Brand brand);

    public void delete(Long brandId);

    public Map<String, Object> paging(String brandId, String brandName, Integer pageNum, Integer pageSize);
    public List<BrandKeyValueVo> selectBrandKeyValue();

}
