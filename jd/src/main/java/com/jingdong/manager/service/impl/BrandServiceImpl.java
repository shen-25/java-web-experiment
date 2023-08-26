package com.jingdong.manager.service.impl;

import com.jingdong.manager.command.BrandCommand;
import com.jingdong.manager.exception.BusinessException;
import com.jingdong.manager.exception.BusinessExceptionEnum;
import com.jingdong.manager.model.entity.Brand;
import com.jingdong.manager.model.vo.BrandKeyValueVo;
import com.jingdong.manager.service.BrandService;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class BrandServiceImpl implements BrandService {

    private BrandCommand brandCommand = new BrandCommand();
    @Override
    public void add(Brand brand) {
        Brand brand1 = brandCommand.selectById(brand.getBrandId());
        if (brand1 != null) {
            throw new BusinessException(BusinessExceptionEnum.BRAND_NAME_EXISTS);
        }

        brand.setCreateTime(new Date());
        brandCommand.insert(brand);
    }

    @Override
    public void edit(Brand brand) {
        Brand brand1 = brandCommand.selectById(brand.getBrandId());
        if (brand1 == null) {
            throw new BusinessException(BusinessExceptionEnum.BRAND_NOT_EXISTS);
        }
        Brand brand2 = brandCommand.selectByBrandName(brand.getBrandName());
        if (brand2 != null && !brand2.getBrandId().equals(brand.getBrandId())) {
            throw new BusinessException(BusinessExceptionEnum.BRAND_NAME_EXISTS);
        }
        brand1.setBrandName(brand.getBrandName());
        brand1.setBrandDesc(brand.getBrandDesc());
        brand1.setUpdateTime(new Date());
        brand1.setBrandOrder(brand.getBrandOrder());
        brand1.setBrandState(brand.getBrandState());
        brand1.setBrandPicUrl(brand.getBrandPicUrl());
        brandCommand.edit(brand1);

    }

    @Override
    public void delete(Long brandId) {
        brandCommand.deleteById(brandId);

    }

    @Override
    public Map<String, Object> paging(String brandId, String brandName, Integer pageNum, Integer pageSize) {
        Map<String, Object> stringObjectMap = brandCommand.selectPage(brandId, brandName, pageNum, pageSize);
        return stringObjectMap;
    }

    @Override
    public List<BrandKeyValueVo> selectBrandKeyValue() {
        return brandCommand.selectBrandKeyValue();
    }
}
