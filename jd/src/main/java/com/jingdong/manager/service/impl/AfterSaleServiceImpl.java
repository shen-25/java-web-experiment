package com.jingdong.manager.service.impl;

import com.jingdong.manager.command.ServiceCommand;
import com.jingdong.manager.exception.BusinessException;
import com.jingdong.manager.exception.BusinessExceptionEnum;
import com.jingdong.manager.model.entity.Service;
import com.jingdong.manager.service.AfterSaleService;

import java.net.SecureCacheResponse;
import java.util.Date;
import java.util.Map;

public class AfterSaleServiceImpl implements AfterSaleService {
    private ServiceCommand serviceCommand = new ServiceCommand();

    @Override
    public void add(Service service){
        serviceCommand.insert(service);

    }
    @Override
    public void edit(Long serviceId, Integer serviceStatus){
       Service service =  serviceCommand.selectById(serviceId);
        service.setServiceStatus(serviceStatus);
        service.setProcessTime(new Date());

        serviceCommand.updateById(service);
    }
    @Override
    public void delete(Long serviceId){
        if (serviceCommand.selectById(serviceId) == null) {
                throw new BusinessException(BusinessExceptionEnum.AFTER_SALES_ID_NOT_EXIST);
        }

        serviceCommand.deleteById(serviceId);
    }
    @Override
    public Map<String, Object> paging(String serviceId, Integer pageNum, Integer pageSize){
        return   serviceCommand.selectPage(serviceId, pageNum, pageSize);

    }
}
