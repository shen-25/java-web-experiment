package com.jingdong.manager.service.impl;

import com.jingdong.manager.command.DeptCommand;
import com.jingdong.manager.exception.BusinessException;
import com.jingdong.manager.exception.BusinessExceptionEnum;
import com.jingdong.manager.model.entity.Department;
import com.jingdong.manager.model.vo.DepartmentNameVo;
import com.jingdong.manager.service.DepartmentService;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author word
 */
public class DepartmentServiceImpl implements DepartmentService {


    private DeptCommand deptCommand = new DeptCommand();

    @Override
    public List<DepartmentNameVo> selectDepartmentName(){
        List<DepartmentNameVo> departmentNameVos = deptCommand.selectDepartmentName();
        return departmentNameVos;
    }


    @Override
    public void add(Department department) {
        Department department1 = deptCommand.selectByName(department.getDeptName());
        if (department1 != null) {
            throw new BusinessException(BusinessExceptionEnum.DEPT_NAME_EXIST);
        }
        department.setCreateTime(new Date());
        deptCommand.insert(department);

    }

    @Override
    public void edit(Department department) {
        Department department1 = deptCommand.selectById(department.getDeptId());
        if (department1 == null) {
            throw new BusinessException(BusinessExceptionEnum.DEPT_NOT_EXIST);
        }
        Department department2 = deptCommand.selectByName(department.getDeptName());
        if (department2 != null && !department2.getDeptId().equals(department.getDeptId())) {
            throw new BusinessException(BusinessExceptionEnum.DEPT_NAME_EXIST);
        }
        department1.setDeptName(department.getDeptName());
        department1.setUpdateTime(new Date());
        deptCommand.edit(department1);
    }

    @Override
    public void delete(Long deptId) {
        Department department = deptCommand.selectById(deptId);
        if (department == null) {
            throw new BusinessException(BusinessExceptionEnum.DEPT_NOT_EXIST);
        }
        deptCommand.deleteById(deptId);

    }

    @Override
    public Map<String, Object> paging(String deptName, Integer pageNum, Integer pageSize) {
        Map<String, Object> stringObjectMap = deptCommand.selectPage(deptName, pageNum, pageSize);
        return stringObjectMap;
    }

}
