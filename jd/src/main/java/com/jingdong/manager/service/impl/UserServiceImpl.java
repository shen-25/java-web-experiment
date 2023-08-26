package com.jingdong.manager.service.impl;

import com.jingdong.manager.command.RoleUserCommand;
import com.jingdong.manager.command.UserCommand;
import com.jingdong.manager.common.Constant;
import com.jingdong.manager.exception.BusinessException;
import com.jingdong.manager.model.entity.User;
import com.jingdong.manager.exception.BusinessExceptionEnum;
import com.jingdong.manager.model.request.UserFormReq;
import com.jingdong.manager.model.vo.UserMenuTreeVo;
import com.jingdong.manager.service.RoleUserService;
import com.jingdong.manager.service.UserService;
import com.jingdong.manager.utils.MD5Utils;


import java.sql.Timestamp;
import java.util.*;

/**
 * @author word
 */
public class UserServiceImpl implements UserService {


    private RoleUserService roleUserService = new RoleUserServiceImpl();

    private UserCommand userCommand = new UserCommand();
    private RoleUserCommand roleUserCommand = new RoleUserCommand();


    @Override
    public User login(String userName, String password) {

        User user = userCommand.selectOne(userName);
        if (user == null) {
            throw new BusinessException(BusinessExceptionEnum.ERROR_USER_NAME_OR_PASSWORD);
        } else{
            String md5 = MD5Utils.md5Digest(password, user.getSalt());
            System.out.println(md5);
            if (!md5.equals(user.getPassword())) {
                throw new BusinessException(BusinessExceptionEnum.ERROR_USER_NAME_OR_PASSWORD);
            }
            if (user.getState().equals(Constant.UserState.QUITE)) {
                throw new BusinessException(BusinessExceptionEnum.NEED_PERMISSION_LOGIN);
            }
        }
        user.setLastLoginTime(new Timestamp(System.currentTimeMillis()));
        userCommand.updateById(user);
        return user;
    }

    @Override
    public Map<String, Object> paging(String userId, String userName, Integer state, Integer pageNum, Integer pageSize){

        Map<String, Object> map = new HashMap<>();

        Map<String, Object> stringObjectMap = userCommand.selectPage(userId, userName, state, pageNum, pageSize);
        return stringObjectMap;
    }

    @Override
    public Integer batchDeleteUser(List<Long> userIds){
        for (Long userId : userIds) {
            roleUserCommand.delete(userId);
        }
        int cnt = userCommand.batchDeleteUser(userIds);
        return cnt;
    }

    @Override
    public void createUser(UserFormReq userFormReq) {
        User user = new User();
        if (userCommand.selectOne(userFormReq.getUserName()) != null) {
            throw new BusinessException(BusinessExceptionEnum.USER_EXISTS);
        }
        user.setUserName(userFormReq.getUserName());
        user.setUserEmail(userFormReq.getUserEmail());
        user.setMobile(userFormReq.getMobile());
        user.setDeptId(userFormReq.getDeptId());
        user.setJob(userFormReq.getJob());
        user.setState(userFormReq.getState());
        user.setSex(userFormReq.getSex());
        Integer salt = new Random().nextInt(1000) + 1000;
        String md5 = MD5Utils.md5Digest(Constant.DEFAULT_PASSWORD, salt);
        user.setSalt(salt);
        user.setPassword(md5);
        user.setCreateTime(new Date());
        userCommand.insert(user);
        user = userCommand.selectOne(user.getUserName());
        roleUserCommand.batchInsert(userFormReq.getRoleIds(), user.getUserId());
    }

    @Override
    public void update(UserFormReq userFormReq) {

        User user = userCommand.selectById(userFormReq.getUserId());
        if (user == null) {
            throw new BusinessException(BusinessExceptionEnum.NEED_USER);
        }
        user.setMobile(userFormReq.getMobile());
        user.setSex(userFormReq.getSex());
        user.setJob(userFormReq.getJob());
        user.setState(userFormReq.getState());
        user.setDeptId(userFormReq.getDeptId());
        userCommand.updateById(user);
        List<Long> selectRoles = roleUserService.selectRoleIds(user.getUserId());
        if (selectRoles.size() == userFormReq.getRoleIds().size()) {
            for (Long roleId : userFormReq.getRoleIds()) {
                if (!selectRoles.contains(roleId)) {
                    roleUserService.update(userFormReq.getRoleIds(),userFormReq.getUserId());
                    return;
                }
            }
        } else {
            roleUserService.update(userFormReq.getRoleIds(), userFormReq.getUserId());
        }

    }

    @Override
    public List<UserMenuTreeVo> buttonPermission(Long userId, String secondMenuName) {
        if (userId == null) {
            throw new BusinessException(BusinessExceptionEnum.NEED_USER);
        }
        Set<UserMenuTreeVo> userMenuTreeVos = roleUserService.menuList(userId);
        UserMenuTreeVo ans = new UserMenuTreeVo();
        List<UserMenuTreeVo> res =new ArrayList<>();
        for (UserMenuTreeVo userMenuTreeVo : userMenuTreeVos) {
            if (secondMenuName.equals(userMenuTreeVo.getMenuName())) {
                ans = userMenuTreeVo;
                break;
            }
        }
        if (ans.getMenuId() == null || ans.getMenuName() == null) {
            return null;
        }
        for (UserMenuTreeVo userMenuTreeVo : userMenuTreeVos) {
            if (userMenuTreeVo.getParentId().equals(ans.getMenuId())) {
                res.add(userMenuTreeVo);
            }
        }
         Collections.sort(res, (s1, s2) ->{
             return s1.getMenuOrder().compareTo(s2.getMenuOrder());
         });
        return  res;
    }

    @Override
    public List<Long> userLikeUserNameList(String userName) {
        List<Long> longs = userCommand.userLikeUserNameList(userName);

        return longs;
    }

    @Override
    public User personEdit(User user, String userName, String userEmail, String password, String newPassword, String confirmPassword) {
        if (!newPassword.equals(confirmPassword)) {
            throw new BusinessException(BusinessExceptionEnum.PASSWORD_NOT_EQUALS);
        }
        User user1 = userCommand.selectOne(userName);
        if (!user1.getUserId().equals(user.getUserId())) {
            throw new BusinessException(BusinessExceptionEnum.USE_NOT_EXIST);
        }
        String md5Digest = MD5Utils.md5Digest(password, user1.getSalt());
        if (!md5Digest.equals(user1.getPassword())) {
            throw new BusinessException(BusinessExceptionEnum.ERROR_PASSWORD);
        }
        if (password.equals(newPassword)) {
            throw new BusinessException(BusinessExceptionEnum.OLD_PASSWORD_NOT_EQUALS);
        }
        user1.setUserEmail(userEmail);
        String md5 = MD5Utils.md5Digest(newPassword, user1.getSalt());
        user1.setPassword(md5);
        userCommand.updateById(user1);
        return user1;
    }

}
