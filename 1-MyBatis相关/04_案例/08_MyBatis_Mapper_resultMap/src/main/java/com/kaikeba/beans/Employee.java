package com.kaikeba.beans;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Employee {
       private Integer empNo;
       private String  ename;
       private String  job;
       private Double  sal;
       private Date    hireDate;
       //职员工作年限
       private int workAge;
       
       //构造函数
       public Employee(Date tempDate){
    	    this.hireDate = tempDate;
    	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
    	    this.workAge = Integer.valueOf(sdf.format(new Date())) -  Integer.valueOf(sdf.format(tempDate));
    	    
       }
       
       
       
       
	public int getWorkAge() {
		return workAge;
	}
	public void setWorkAge(int workAge) {
		this.workAge = workAge;
	}
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
	public Date getHireDate() {
		return hireDate;
	}
	public void setHireDate(Date hireDate) {
		this.hireDate = hireDate;
	}
       
       
}
