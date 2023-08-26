package com.jingdong.manager.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jingdong.manager.common.ApiRestResponse;
import com.jingdong.manager.common.Constant;
import com.jingdong.manager.exception.BusinessException;
import com.jingdong.manager.exception.BusinessExceptionEnum;
import com.jingdong.manager.model.entity.Advert;
import com.jingdong.manager.model.entity.User;
import com.jingdong.manager.model.vo.UserMenuTreeVo;
import com.jingdong.manager.service.AdvertService;

import com.jingdong.manager.service.UserService;
import com.jingdong.manager.service.impl.AdvertServiceImpl;

import com.jingdong.manager.service.impl.UserServiceImpl;
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

@WebServlet(name = "advertController", value = "/advert/*")

public class AdvertController extends HttpServlet {

    private AdvertService advertService = new AdvertServiceImpl();
    private UserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        String requestURI = req.getRequestURI();
        String substring = requestURI.substring(requestURI.indexOf("/jd/advert") + "/jd/advert".length());
        if (substring.equals("")) {
            this.showAdvert(req, resp);
        } else if (substring.equals("/list")) {
            ApiRestResponse apiRestResponse = this.advertList(req, resp);
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

    public void showAdvert(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User)session.getAttribute(Constant.CURRENT_USER);
        List<UserMenuTreeVo> userMenuTreeVoSet = userService.buttonPermission(user.getUserId(), "广告管理");
        req.setAttribute("buttons", userMenuTreeVoSet);

        req.getRequestDispatcher("/WEB-INF/jsp/advert/advert.jsp").forward(req, resp);

    }

    public ApiRestResponse advertList(HttpServletRequest req, HttpServletResponse resp) {
        Map<String, Object> paging = new LinkedHashMap<>();
        try {
            String advertName = req.getParameter("advertName");
            Integer pageNum = Integer.valueOf(req.getParameter("page"));
            Integer pageSize = Integer.valueOf(req.getParameter("limit"));
            paging = advertService.paging(advertName, pageNum, pageSize);
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
            Advert advert = JSONObject.parseObject(JSON.toJSONString(json), Advert.class);
            advertService.add(advert);
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
            Advert advert = JSONObject.parseObject(JSON.toJSONString(json), Advert.class);
            advertService.edit(advert);
        } catch (BusinessException e) {
            return ApiRestResponse.error(e.getCode(), e.getMsg());
        } catch (IOException e) {
            return ApiRestResponse.error(e.getMessage());
        }

        return ApiRestResponse.success();
    }

    public ApiRestResponse delete(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String advertId = req.getParameter("advertId");
            advertService.delete(Long.valueOf(advertId));
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
        System.out.println(filename);
        return new ApiRestResponse(0, "/jd/upload/" + filename);
    }



}
