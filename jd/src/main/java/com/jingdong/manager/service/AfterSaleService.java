package com.jingdong.manager.service;

import com.jingdong.manager.model.entity.Brand;
import com.jingdong.manager.model.entity.Service;

import java.util.Map;

public interface AfterSaleService {

    public void add(Service service);
    public void edit(Long serviceId,Integer serviceStatus);

    public void delete(Long serviceId);

    public Map<String, Object> paging(String serviceId, Integer pageNum, Integer pageSize);
}
