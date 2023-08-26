package com.jingdong.manager.service.impl;

import com.jingdong.manager.command.ProductCommand;
import com.jingdong.manager.common.Constant;
import com.jingdong.manager.exception.BusinessException;
import com.jingdong.manager.exception.BusinessExceptionEnum;
import com.jingdong.manager.model.entity.Product;
import com.jingdong.manager.service.ProductService;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class ProductServiceImpl implements ProductService {
    private ProductCommand productCommand = new ProductCommand();
    @Override
    public Map<String, Object> paging(String productName, String categoryId, String brandId, String productState, Integer pageNum, Integer pageSize) {
        Map<String, Object> stringObjectMap = productCommand.selectPage(productName, categoryId, brandId, productState, pageNum, pageSize);
        return stringObjectMap;
    }

    @Override
    public Long selectByUpState() {
        return productCommand.selectByUpState();
    }

    @Override
    public Long selectByDownState() {
        return productCommand.selectByDownState();
    }

    @Override
    public Long selectAllProduct() {
        return productCommand.selectAllProduct();
    }

    @Override
    public Long selectByTightProduct() {
        return productCommand.selectByTightProduct();
    }

    @Override
    public void add(Product product) {
        Product product1 = productCommand.selectByName(product.getProductName());
        if (product1 != null) {
            throw new BusinessException(BusinessExceptionEnum.PRODUCT_NAME_EXIST);
        }
        product.setProductPrice(product.getProductPrice() * 1000);
        if (product.getProductOrder() == null) {
            product.setProductOrder(Constant.DEFAULT_VALUE);
        }
        product.setCreateTime(new Date());
        productCommand.insert(product);
    }

    @Override
    public void edit(Product product) {

        Product product1 = productCommand.selectById(product.getProductId());
        if (product1 == null) {
            throw new BusinessException(BusinessExceptionEnum.PRODUCT_NOT_EXIST);
        }
        Product product2 = productCommand.selectByName(product.getProductName());
        if (product2 != null && !product2.getProductId().equals(product.getProductId())) {
            throw new BusinessException(BusinessExceptionEnum.PRODUCT_NAME_EXIST);
        }
        product1.setProductName(product.getProductName());
        product1.setProductDesc(product.getProductDesc());
        product1.setProductPicUrl(product.getProductPicUrl());
        product1.setBrandId(product.getBrandId());
        product1.setCategoryId(product.getCategoryId());
        product1.setProductPrice(product.getProductPrice() * 1000);
        product1.setProductStock(product.getProductStock());
        product1.setProductState(product.getProductState());
        product1.setProductOrder(product.getProductOrder());
        product1.setUpdateTime(new Date());
        productCommand.updateById(product1);
    }

    @Override
    public void delete(Long productId) {
        Product product = productCommand.selectById(productId);
        if ( product == null) {
            throw new BusinessException(BusinessExceptionEnum.PRODUCT_NOT_EXIST);
        }
        productCommand.deleteById(productId);
    }

    @Override
    public List<Long> productLikeProductNameList(String productName) {
        List<Long> longs = productCommand.productLikeProductNameList(productName);

        return longs;
    }

    public static void main(String[] args) {
        ProductServiceImpl productService = new ProductServiceImpl();
        productService.paging(null, "-1", "-1", "1",1, 10);
    }
}
