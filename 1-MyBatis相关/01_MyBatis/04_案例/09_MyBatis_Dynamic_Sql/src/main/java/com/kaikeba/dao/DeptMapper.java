package com.kaikeba.dao;



import java.util.List;

import com.kaikeba.beans.Dept;

public interface DeptMapper {
  
  public List<Dept> dept_1(Dept dept);
  public List<Dept> dept_2(Dept dept);
  public List<Dept> dept_3(Dept dept);
  public int deptUpdate(Dept dept);
}
