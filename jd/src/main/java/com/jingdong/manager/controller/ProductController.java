package com.jingdong.manager.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jingdong.manager.common.ApiRestResponse;
import com.jingdong.manager.common.Constant;
import com.jingdong.manager.exception.BusinessException;
import com.jingdong.manager.exception.BusinessExceptionEnum;
import com.jingdong.manager.model.entity.Product;
import com.jingdong.manager.model.entity.User;
import com.jingdong.manager.model.vo.BrandKeyValueVo;
import com.jingdong.manager.model.vo.CategoryKeyValueVo;
import com.jingdong.manager.model.vo.UserMenuTreeVo;
import com.jingdong.manager.service.*;
import com.jingdong.manager.service.impl.*;
import com.jingdong.manager.utils.MyIOUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@WebServlet(name = "productController", value = "/store/product/*")

public class ProductController extends HttpServlet {

    private CategoryService categoryService = new CategoryServiceImpl();
    private ProductService productService = new ProductServiceImpl();
    private BrandService brandService = new BrandServiceImpl();
    private UserService userService = new UserServiceImpl();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        String requestURI = req.getRequestURI();
        String substring = requestURI.substring(requestURI.indexOf("/jd/store/product") + "/jd/store/product".length());
        if (substring.equals("")) {
            this.showProduct(req, resp);
        } else if (substring.equals("/list")) {
            ApiRestResponse apiRestResponse = this.productList(req, resp);
            out.println(JSON.toJSONString(apiRestResponse));
        }else if (substring.equals("/add")) {
            ApiRestResponse add = this.add(req, resp);
            out.println(JSON.toJSONString(add));
        } else if (substring.equals("/edit")) {
            ApiRestResponse edit = this.edit(req, resp);
            out.println(JSON.toJSONString(edit));
        } else if (substring.equals("/delete")) {
            ApiRestResponse delete = this.delete(req, resp);
            out.println(JSON.toJSONString(delete));
        } else if (substring.equals("/upload/image")) {
            ApiRestResponse apiRestResponse = this.uploadImage(req, resp);
            out.println(JSON.toJSONString(apiRestResponse));
        } else {
            req.getRequestDispatcher("/WEB-INF/404.jsp").forward(req, resp);
        }
    }

    public void showProduct(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<CategoryKeyValueVo> categoryKeyValueVos = categoryService.selectCategoryKeyValue();
        List<BrandKeyValueVo> brandKeyValueVoList = brandService.selectBrandKeyValue();
        req.setAttribute("categoryKeyValueVos", categoryKeyValueVos);
        req.setAttribute("brandKeyValueVos", brandKeyValueVoList);
        req.setAttribute("categoryKeyValueStr", JSON.toJSONString(categoryKeyValueVos));
        req.setAttribute("brandKeyValueStr", JSON.toJSONString(brandKeyValueVoList) );
        HttpSession session = req.getSession();
        User user = (User)session.getAttribute(Constant.CURRENT_USER);
        List<UserMenuTreeVo> userMenuTreeVoSet = userService.buttonPermission(user.getUserId(), "商品列表");
        req.setAttribute("buttons", userMenuTreeVoSet);
        req.getRequestDispatcher("/WEB-INF/jsp/product/product.jsp").forward(req, resp);

    }

    public ApiRestResponse productList(HttpServletRequest req, HttpServletResponse resp) {
        Map<String, Object> paging = new LinkedHashMap<>();
        try {
            String productName = req.getParameter("productName");
            String categoryId = req.getParameter("categoryId");
            String brandId  = req.getParameter("brandId");
            String  productState = req.getParameter("productState");
            Integer pageNum = Integer.valueOf(req.getParameter("page"));
            Integer pageSize = Integer.valueOf(req.getParameter("limit"));
            paging = productService.paging(productName, categoryId, brandId,  productState, pageNum, pageSize);
        } catch (BusinessException e) {
            return ApiRestResponse.error(e.getCode(), e.getMsg());
        } catch (Exception e) {
            return ApiRestResponse.error(e.getMessage());
        }
        return ApiRestResponse.success(paging);
    }

    public ApiRestResponse add(HttpServletRequest req, HttpServletResponse resp) {
        JSONObject json = null;
        try {
            json = MyIOUtils.getJson(req);
           Product product = JSONObject.parseObject(JSON.toJSONString(json), Product.class);
            productService.add(product);
        } catch (BusinessException e) {
            return ApiRestResponse.error(e.getCode(), e.getMsg());
        } catch (Exception e) {
            return ApiRestResponse.error(e.getMessage());
        }
        return ApiRestResponse.success();
    }


    public ApiRestResponse edit(HttpServletRequest req, HttpServletResponse resp) {
        JSONObject json = null;
        try {
            json = MyIOUtils.getJson(req);
            Product product = JSONObject.parseObject(JSON.toJSONString(json), Product.class);
            productService.edit(product);
        } catch (BusinessException e) {
            return ApiRestResponse.error(e.getCode(), e.getMsg());
        } catch (IOException e) {
            return ApiRestResponse.error(e.getMessage());
        }

        return ApiRestResponse.success();
    }

    public ApiRestResponse delete(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String productId = req.getParameter("productId");
            productService.delete(Long.valueOf(productId));
        } catch (BusinessException e) {
            return ApiRestResponse.error(e.getCode(), e.getMsg());
        } catch (Exception e) {
            return ApiRestResponse.error(e.getMessage());
        }
        return ApiRestResponse.success();
    }

    public ApiRestResponse uploadImage(HttpServletRequest req, HttpServletResponse resp) {
        FileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload sf = new ServletFileUpload(factory);
        String filename = null;
        String path = "";
        try {
            List<FileItem> formData = sf.parseRequest(req);
            for (FileItem fi : formData) {
                if (!fi.isFormField()) {
                	path = req.getServletContext().getRealPath("/upload");
                    filename = UUID.randomUUID().toString();
                    filename += fi.getName().substring(fi.getName().lastIndexOf("."));
                    try {
                        fi.write(new File(path, filename));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (FileUploadException e) {
            e.printStackTrace();
        }
        if (filename == null) {
            return ApiRestResponse.error(BusinessExceptionEnum.ERROR_USER_NAME_OR_PASSWORD);
        }
        return new ApiRestResponse(0, "/jd/upload/" + filename);
    }

}
