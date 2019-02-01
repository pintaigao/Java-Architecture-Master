package com.kaikeba.dao;



import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.kaikeba.beans.Dept;

public interface DeptMapper {
  
 public Dept deptFindById(int deptno);
}
