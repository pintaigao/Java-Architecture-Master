package com.kaikeba.dao;



import java.util.List;

import com.kaikeba.beans.Dept;

public interface DeptMapper {
   public void deptSave(Dept dept);
   public List<Dept> deptFind();
}
