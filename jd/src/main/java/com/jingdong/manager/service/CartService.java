package com.jingdong.manager.service;

import java.util.Map;

public interface CartService {

    public void add(Long userId, Long productId, Integer quantity);


    public void update(Long userId, Long productId, Integer count);

    public void delete(Long userId, Long productId);
    public void delete(Long cartId);


    public Map<String, Object> paging(String userId, Integer pageNum, Integer pageSize);


}
