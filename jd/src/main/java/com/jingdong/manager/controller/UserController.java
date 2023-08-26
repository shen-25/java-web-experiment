package com.jingdong.manager.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mysql.cj.util.StringUtils;
import com.jingdong.manager.common.ApiRestResponse;
import com.jingdong.manager.common.Constant;
import com.jingdong.manager.exception.BusinessException;
import com.jingdong.manager.exception.BusinessExceptionEnum;
import com.jingdong.manager.model.entity.User;
import com.jingdong.manager.model.request.UserFormReq;
import com.jingdong.manager.model.vo.DepartmentNameVo;
import com.jingdong.manager.model.vo.UserMenuTreeVo;
import com.jingdong.manager.service.DepartmentService;
import com.jingdong.manager.service.RoleUserService;
import com.jingdong.manager.service.UserService;
import com.jingdong.manager.service.impl.DepartmentServiceImpl;
import com.jingdong.manager.service.impl.RoleUserServiceImpl;
import com.jingdong.manager.service.impl.UserServiceImpl;
import com.jingdong.manager.utils.MyIOUtils;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.*;


@WebServlet(name = "userController", value = "/sys/user/*")
public class UserController extends HttpServlet {


    private UserService userService = new UserServiceImpl();

    private DepartmentService departmentService = new DepartmentServiceImpl();


    private RoleUserService roleUserService = new RoleUserServiceImpl();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    /**
     * Called by the server (via the <code>service</code> method)
     * to allow a servlet to handle a POST request.
     * <p>
     * The HTTP POST method allows the client to send
     * data of unlimited length to the Web server a single time
     * and is useful when posting information such as
     * credit card numbers.
     *
     * <p>When overriding this method, read the request data,
     * write the response headers, get the response's writer or output
     * stream object, and finally, write the response data. It's best
     * to include content type and encoding. When using a
     * <code>PrintWriter</code> object to return the response, set the
     * content type before accessing the <code>PrintWriter</code> object.
     *
     * <p>The servlet container must write the headers before committing the
     * response, because in HTTP the headers must be sent before the
     * response body.
     *
     * <p>Where possible, set the Content-Length header (with the
     * {@link ServletResponse#setContentLength} method),
     * to allow the servlet container to use a persistent connection
     * to return its response to the client, improving performance.
     * The content length is automatically set if the entire response fits
     * inside the response buffer.
     *
     * <p>When using HTTP 1.1 chunked encoding (which means that the response
     * has a Transfer-Encoding header), do not set the Content-Length header.
     *
     * <p>This method does not need to be either safe or idempotent.
     * Operations requested through POST can have side effects for
     * which the user can be held accountable, for example,
     * updating stored data or buying items online.
     *
     * <p>If the HTTP POST request is incorrectly formatted,
     * <code>doPost</code> returns an HTTP "Bad Request" message.
     *
     * @param req  an {@link HttpServletRequest} object that
     *             contains the request the client has made
     *             of the servlet
     * @param resp an {@link HttpServletResponse} object that
     *             contains the response the servlet sends
     *             to the client
     * @throws IOException      if an input or output error is
     *                          detected when the servlet handles
     *                          the request
     * @throws ServletException if the request for the POST
     *                          could not be handled
     * @see ServletOutputStream
     * @see ServletResponse#setContentType
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        String requestURI = req.getRequestURI();
        String substring = requestURI.substring(requestURI.indexOf("/jd/sys/user") + "/jd/sys/user".length());
        if(substring.equals("/login.html")){
            this.showLogin(req, resp);
        } else if(substring.equals("")){
            this.userMenu(req, resp);
        } else if (substring.equals("/showAdd")) {
            this.userShowAdd(req, resp);
        } else if(substring.equals("/add")){
            ApiRestResponse user = this.createUser(req, resp);
            out.println(JSON.toJSONString(user));
        } else if (substring.equals("/showEdit")) {
            this.userShowEdit(req, resp);
        } else if (substring.equals("/check_login")) {
            ApiRestResponse apiRestResponse = this.checkLogin(req, resp);
            out.println(JSON.toJSONString(apiRestResponse));

        } else if (substring.equals("/logout")) {
            this.logout(req, resp);
        } else if (substring.equals("/list")) {
            ApiRestResponse apiRestResponse = this.userList(req, resp);
            out.println(JSON.toJSONString(apiRestResponse));
        } else if (substring.equals("/delete")) {
            ApiRestResponse apiRestResponse = this.userDelete(req, resp);
            out.println(JSON.toJSONString(apiRestResponse));
        } else if (substring.equals("/edit")) {
            ApiRestResponse apiRestResponse = this.updateUser(req, resp);
            out.println(JSON.toJSONString(apiRestResponse));

        } else if (substring.equals("/buttonPermission")) {
            ApiRestResponse apiRestResponse = this.ButtonPermission(req.getSession());
            out.println(JSON.toJSONString(apiRestResponse));
        } else if(substring.equals("/noAuth.html")){
            this.noAuth(req);
        } else if (substring.equals("/person")) {
            this.person(req, resp);
        } else if (substring.equals("/personEdit")) {
            ApiRestResponse apiRestResponse = this.personEdit(req, resp);
            out.println(JSON.toJSONString(apiRestResponse));
        } else {
            req.getRequestDispatcher("/WEB-INF/404.jsp").forward(req, resp);

        }
    }

    private ApiRestResponse personEdit(HttpServletRequest req, HttpServletResponse resp) {
        try {

            String userName = req.getParameter("userName");
            String userEmail = req.getParameter("userEmail");
            String password = req.getParameter("password");
            String newPassword = req.getParameter("newPassword");
            String confirmPassword = req.getParameter("confirmPassword");
            User user = (User) req.getSession().getAttribute(Constant.CURRENT_USER);
            User newUser =  userService.personEdit(user, userName, userEmail, password, newPassword, confirmPassword);
            HttpSession session = req.getSession();
            session.setAttribute(Constant.CURRENT_USER, newUser);
        }
        catch (BusinessException e) {
            return ApiRestResponse.error(e.getCode(), e.getMsg());
        } catch (Exception e) {
            return ApiRestResponse.error(e.getMessage());
        }

        return ApiRestResponse.success();
    }

    public void person(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        req.setAttribute("person", session.getAttribute(Constant.CURRENT_USER));

        req.getRequestDispatcher("/WEB-INF/jsp/user/person.jsp").forward(req, resp);
    }


    public void showLogin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/login.jsp").forward(req, resp);
    }


    public void userMenu(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        List<DepartmentNameVo> departmentNameVoList = departmentService.selectDepartmentName();
        req.setAttribute("deptKeyValues", departmentNameVoList);
        User user = (User)session.getAttribute(Constant.CURRENT_USER);
        List<UserMenuTreeVo> userMenuTreeVoSet = userService.buttonPermission(user.getUserId(), "用户管理");
        req.setAttribute("buttons", userMenuTreeVoSet);
        req.getRequestDispatcher("/WEB-INF/jsp/user/user.jsp").forward(req, resp);
    }

    public void userShowAdd(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<DepartmentNameVo> departmentNameVoList = departmentService.selectDepartmentName();
        req.setAttribute("deptKeyValues", departmentNameVoList);
        req.getRequestDispatcher("/WEB-INF/jsp/user/userAdd.jsp").forward(req, resp);
    }


    public ApiRestResponse createUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        JSONObject json = MyIOUtils.getJson(req);
        UserFormReq userFormReq = JSONObject.parseObject(JSON.toJSONString(json), UserFormReq.class);
        try {
            userService.createUser(userFormReq);
        }catch (BusinessException e) {
            return ApiRestResponse.error(e.getCode(), e.getMsg());
        } catch (Exception e) {
            return ApiRestResponse.error(e.getMessage());
        }
        return ApiRestResponse.success();
    }

    public void userShowEdit(HttpServletRequest req, HttpServletResponse resp) {
        List<DepartmentNameVo> departmentNameVoList = departmentService.selectDepartmentName();
        req.setAttribute("deptKeyValues", departmentNameVoList);
        try {
            req.getRequestDispatcher("/WEB-INF/jsp/user/userEdit.jsp").forward(req, resp);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public ApiRestResponse checkLogin(HttpServletRequest request, HttpServletResponse resp) {
        String userName = request.getParameter("userName");
        HttpSession session = request.getSession();
        String password = request.getParameter("password");
        String vc = request.getParameter("vc");
        String verifyCode = (String)session.getAttribute("code");
        System.out.println("验证码: " + verifyCode);
        if (vc == null || verifyCode == null) {
            return ApiRestResponse.error(BusinessExceptionEnum.NEED_CODE);
        }
        if(!vc.toLowerCase(Locale.ROOT).equals(verifyCode)){

            return ApiRestResponse.error(BusinessExceptionEnum.ERROR_VERIFY_CODE);
        }
        if(StringUtils.isNullOrEmpty(userName)){
            return ApiRestResponse.error(BusinessExceptionEnum.NEED_USER_NAME);
        }
        if (StringUtils.isNullOrEmpty(password)) {
            return ApiRestResponse.error(BusinessExceptionEnum.NEED_PASSWORD);
        }

        try {
            User user = userService.login(userName, password);
            user.setPassword(null);
            session.setAttribute(Constant.CURRENT_USER, user);
        } catch (BusinessException e) {
            e.printStackTrace();
            return ApiRestResponse.error(e.getCode(), e.getMsg());
        }
        return ApiRestResponse.success();
    }


    public void logout(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        session.removeAttribute(Constant.CURRENT_USER);
        resp.sendRedirect("/jd/sys/user/login.html");
    }


    public ApiRestResponse userList(HttpServletRequest request, HttpServletResponse response){
        Map<String, String> map = new LinkedHashMap<>();
        String userId = request.getParameter("userId");
        String userName = request.getParameter("userName");
        String state = request.getParameter("state");
        Integer stateTemp = null;
        if (StringUtils.isNullOrEmpty(state)) {
            stateTemp = null;
        } else{
            stateTemp = Integer.valueOf(state);
        }
        Integer page = Integer.valueOf(request.getParameter("page"));
        Integer limit = Integer.valueOf(request.getParameter("limit"));
        Map<String, Object> paging = null;
        try {
            paging = userService.paging(userId, userName, stateTemp, page, limit);
        } catch (SQLException e) {
            return ApiRestResponse.error(BusinessExceptionEnum.SYSTEM_ERROR);
        } catch (BusinessException e) {
            return ApiRestResponse.error(e.getCode(), e.getMsg());
        }
        return ApiRestResponse.success(paging);
    }

    public ApiRestResponse userDelete(HttpServletRequest request, HttpServletResponse response) {
        Integer cnt = null;
        try {
            String userIdsStr = request.getParameter("userIds");
            String[] userIdsArray = userIdsStr.split(",");
            List<Long> userIds = new ArrayList<>();
            for (String str : userIdsArray) {
                userIds.add(Long.valueOf(str));
            }
            cnt = userService.batchDeleteUser(userIds);
        } catch (BusinessException e) {
            return ApiRestResponse.error(e.getCode(), e.getMsg());
        } catch (RuntimeException e) {
            return ApiRestResponse.error(500, e.getMessage());
        }
        return ApiRestResponse.success(cnt);
    }

    public ApiRestResponse updateUser(HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = null;
        try {
            json = MyIOUtils.getJson(request);
            UserFormReq userFormReq = JSONObject.parseObject(JSON.toJSONString(json), UserFormReq.class);
            userService.update(userFormReq);
        } catch (IOException e) {
            return ApiRestResponse.error(600, e.getMessage());
        }catch (BusinessException e) {
            return ApiRestResponse.error(e.getCode(), e.getMsg());
        } catch (RuntimeException e) {
            return ApiRestResponse.error(500, e.getMessage());
        }
        return ApiRestResponse.success();
    }


    public ApiRestResponse ButtonPermission(HttpSession session) {
        User user = (User)session.getAttribute(Constant.CURRENT_USER);
        List<UserMenuTreeVo> userMenuTreeVoSet = new ArrayList<>();
        try {
            userMenuTreeVoSet = userService.buttonPermission(user.getUserId(), "用户管理");
        }
        catch (BusinessException e) {
            return ApiRestResponse.error(e.getCode(), e.getMsg());
        } catch (Exception e) {
            return ApiRestResponse.error(e.getMessage());
        }
        return ApiRestResponse.success(userMenuTreeVoSet);
    }

    public void noAuth(HttpServletRequest request) {
        try {
            request.getRequestDispatcher("/WEB-INF/noAuth.jsp").forward(request, null);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
