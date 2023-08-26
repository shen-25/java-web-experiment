package com.jingdong.manager.service;

import com.jingdong.manager.model.entity.Advert;

import java.util.Map;

public interface AdvertService {
    public Map<String, Object> paging(String advertName, Integer pageNum, Integer pageSize);

    public void add(Advert advert);
    public void edit(Advert advert);
    public void delete(Long advertId);
}
