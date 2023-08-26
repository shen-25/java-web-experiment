package com.jingdong.manager.service;

import com.jingdong.manager.model.entity.Department;
import com.jingdong.manager.model.vo.DepartmentNameVo;

import java.util.List;
import java.util.Map;

/**
 * @author word
 */
public interface DepartmentService {
    public List<DepartmentNameVo> selectDepartmentName();
    public void add(Department department);
    public void edit(Department department);

    public void delete(Long  categoryId);

    public Map<String, Object> paging(String deptName, Integer pageNum, Integer pageSize);
}
