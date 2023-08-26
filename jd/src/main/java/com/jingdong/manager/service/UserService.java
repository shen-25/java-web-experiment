package com.jingdong.manager.service;

import com.jingdong.manager.model.entity.User;
import com.jingdong.manager.model.request.UserFormReq;
import com.jingdong.manager.model.vo.UserMenuTreeVo;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface UserService {
    public User login(String userName, String password);
    public Map<String, Object> paging(String userId, String userName, Integer state, Integer pageNum, Integer pageSize) throws SQLException;

    Integer batchDeleteUser(List<Long> userIds);

    public void createUser(UserFormReq userFormReq);

    public void update(UserFormReq userFormReq);

    public List<UserMenuTreeVo> buttonPermission(Long userId, String secondMenuName);

    public List<Long> userLikeUserNameList(String userName);
    public User personEdit(User user, String userName, String userEmail, String password, String newPassword, String confirmPassword);
}
