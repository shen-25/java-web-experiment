package com.jingdong.manager.service.impl;

import com.jingdong.manager.command.AdvertCommand;
import com.jingdong.manager.common.Constant;
import com.jingdong.manager.exception.BusinessException;
import com.jingdong.manager.exception.BusinessExceptionEnum;
import com.jingdong.manager.model.entity.Advert;
import com.jingdong.manager.service.AdvertService;

import java.util.Date;
import java.util.Map;

public class AdvertServiceImpl implements AdvertService {
    private AdvertCommand advertCommand = new AdvertCommand();
    @Override
    public Map<String, Object> paging(String advertName, Integer pageNum, Integer pageSize) {
        return advertCommand.selectPage(advertName, pageNum, pageSize);
    }

    @Override
    public void add(Advert advert) {
        Advert advert1 = advertCommand.selectByName(advert.getName());
        if (advert1 != null) {
            throw new BusinessException(BusinessExceptionEnum.ADVERT_NAME_EXIST);
        }
        if (advert.getAdvertOrder() == null) {
            advert.setAdvertOrder(Constant.DEFAULT_VALUE);
        }
        advert.setCreateTime(new Date());
        advertCommand.insert(advert);
    }

    @Override
    public void edit(Advert advert) {
        Advert advert1 = advertCommand.selectById(advert.getId());
        if (advert1 == null) {
            throw new BusinessException(BusinessExceptionEnum.ADVERT_NOT_EXIST);
        }
        Advert advert2 = advertCommand.selectByName(advert.getName());
        if (advert2 != null && !advert2.getId().equals(advert.getId())) {
            throw new BusinessException(BusinessExceptionEnum.ADVERT_NAME_EXIST);
        }
        advert1.setName(advert.getName());
        advert1.setAdvertDesc(advert.getAdvertDesc());
        advert1.setStatus(advert.getStatus());
        advert1.setAdvertOrder(advert.getAdvertOrder());
        advert1.setPicUrl(advert.getPicUrl());
        advert1.setUrl(advert.getUrl());
        advert1.setStartTime(advert.getStartTime());
        advert1.setUpdateTime(new Date());
        advert1.setEndTime(advert.getEndTime());
        advertCommand.updateById(advert1);
    }

    @Override
    public void delete(Long advertId) {
        Advert advert = advertCommand.selectById(advertId);
        if ( advert == null) {
            throw new BusinessException(BusinessExceptionEnum.ADVERT_NOT_EXIST);
        }
        advertCommand.deleteById(advertId);
    }
}
