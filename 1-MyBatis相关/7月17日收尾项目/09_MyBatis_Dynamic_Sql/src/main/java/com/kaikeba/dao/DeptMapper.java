package com.kaikeba.dao;



import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.kaikeba.beans.Dept;

public interface DeptMapper {
  
  public List<Dept> dept_1(Dept dept);
  public List<Dept> dept_2(Dept dept);
  public List<Dept> dept_3(Dept dept);
  public int deptUpdate(Dept dept);
  public int deptSave(List<Dept> deptList);
  public List deptFindByList(List deptNoList);
  public List deptFindByArray(int []deptArray);
  public List deptFindByMap(@Param("myMap")Map amp);
}
