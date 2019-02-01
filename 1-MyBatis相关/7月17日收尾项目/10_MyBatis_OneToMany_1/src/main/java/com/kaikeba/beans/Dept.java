package com.kaikeba.beans;

import java.util.List;
import java.util.Map;

public class Dept {
       private Integer deptNo;
       private String dname;
       private String loc;
       //隶属于当前部门下的所有的职员集合
       private List<Employee> empList;
     
	public List<Employee> getEmpList() {
		return empList;
	}
	public void setEmpList(List<Employee> empList) {
		this.empList = empList;
	}
	public Integer getDeptNo() {
		return deptNo;
	}
	public void setDeptNo(Integer deptNo) {
		this.deptNo = deptNo;
	}
	public String getDname() {
		return dname;
	}
	public void setDname(String dname) {
		this.dname = dname;
	}
	public String getLoc() {
		return loc;
	}
	public void setLoc(String loc) {
		this.loc = loc;
	}
  
}
