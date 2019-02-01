package com.kaikeba.beans;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Employee {
       private Integer empNo;
       private String  ename;
       private String  job;
       private Double  sal;
       //表示当前职员隶属于的部门信息
       private Dept dept;
	public Integer getEmpNo() {
		return empNo;
	}
	public void setEmpNo(Integer empNo) {
		this.empNo = empNo;
	}
	public String getEname() {
		return ename;
	}
	public void setEname(String ename) {
		this.ename = ename;
	}
	public String getJob() {
		return job;
	}
	public void setJob(String job) {
		this.job = job;
	}
	public Double getSal() {
		return sal;
	}
	public void setSal(Double sal) {
		this.sal = sal;
	}
	public Dept getDept() {
		return dept;
	}
	public void setDept(Dept dept) {
		this.dept = dept;
	}
       
       
}
