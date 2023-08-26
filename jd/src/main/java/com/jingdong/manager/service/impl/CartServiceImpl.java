package com.jingdong.manager.service.impl;

import com.jingdong.manager.command.CartCommand;
import com.jingdong.manager.command.ProductCommand;
import com.jingdong.manager.common.Constant;
import com.jingdong.manager.exception.BusinessException;
import com.jingdong.manager.exception.BusinessExceptionEnum;
import com.jingdong.manager.model.entity.Cart;
import com.jingdong.manager.service.CartService;
import com.jingdong.manager.service.OrderService;

import java.util.Date;
import java.util.Map;

/**
 * @author word
 */
public class CartServiceImpl implements CartService {
    private OrderService orderService = new OrderServiceImpl();
    private ProductCommand productCommand =   new ProductCommand();
    private CartCommand cartCommand = new CartCommand();

    @Override
    public void add(Long userId, Long productId, Integer quantity) {
        orderService.validProduct(productId, quantity);

        Cart cart1 = cartCommand.selectCartByUserIdAndProductId(userId, productId);
        if(cart1 == null) {
            Cart cart = new Cart();
            cart.setCreateTime(new Date());
            cart.setSelected(Constant.CartState.SELECTED);
            cart.setUserId(userId);
            cart.setProductId(productId);
            cart.setQuantity(quantity);
            cartCommand.insert(cart);
        } else{
            quantity += cart1.getQuantity();
            cart1.setQuantity(quantity);
            cart1.setUpdateTime(new Date());
            cart1.setSelected(Constant.CartState.SELECTED);
            cartCommand.edit(cart1);
        }

    }

    @Override
    public void update(Long userId, Long productId, Integer count) {
        orderService.validProduct(productId, count);
        Cart cart = cartCommand.selectCartByUserIdAndProductId(userId, productId);
        if (cart == null) {
            throw new BusinessException(BusinessExceptionEnum.CART_NOT_EXITS);
        } else {
            cart.setQuantity(count);
            cart.setUpdateTime(new Date());
            cart.setSelected(Constant.CartState.SELECTED);
            cartCommand.edit(cart);
        }

    }

    @Override
    public void delete(Long userId, Long productId) {
        Cart cart = cartCommand.selectCartByUserIdAndProductId(userId, productId);
        if (cart == null) {
            throw new BusinessException(BusinessExceptionEnum.CART_NOT_EXITS);
        }
        cartCommand.deleteByUserIdAndProductId(userId, productId);

    }

    @Override
    public void delete(Long cartId) {
        Cart cart = cartCommand.selectById(cartId);
        if (cart == null) {
            throw new BusinessException(BusinessExceptionEnum.CART_NOT_EXITS);
        }
        cartCommand.deleteById(cartId);

    }

    @Override
    public Map<String, Object> paging(String userId, Integer pageNum, Integer pageSize) {
        return cartCommand.selectPage(userId, pageNum, pageSize);
    }

}
