package com.jingdong.manager.service;

import com.jingdong.manager.model.entity.Product;

import java.util.List;
import java.util.Map;

public interface ProductService {
    public Map<String, Object> paging(String productName, String categoryId, String brandId, String  productState, Integer pageNum, Integer pageSize);
    public Long selectByUpState();
    public Long selectByDownState();
    public Long selectAllProduct();
    public Long selectByTightProduct();
    public void add(Product product);
    public void edit(Product product);
    public void delete(Long  productId);
    List<Long> productLikeProductNameList(String productName);
}
