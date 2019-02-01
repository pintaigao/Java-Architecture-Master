package com.kaikeba.dao;



import java.util.List;

import com.kaikeba.beans.Dept;

public interface DeptMapper {
  
   public List<Dept> deptFind(String param);
   public List<Dept> deptFind2(String param);
}
