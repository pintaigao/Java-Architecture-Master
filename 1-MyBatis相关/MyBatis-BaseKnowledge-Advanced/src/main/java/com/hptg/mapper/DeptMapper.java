package com.hptg.mapper;


import com.hptg.application.Dept;

import java.util.List;



public interface DeptMapper {
   public void deptSave(Dept dept);
   public Dept deptFindById(Integer deptno);
   public List<Dept> deptFind();
}
