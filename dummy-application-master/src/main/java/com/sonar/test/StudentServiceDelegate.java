package com.sonar.test;


import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Service 
public class StudentServiceDelegate {
	@Autowired
	
	@HystrixCommand(fallbackMethod="callStudentServiceAndGetData_Fallbackx") 
	public String callStudentServiceAndGetData(String schoolname) {
		System.out.println("Getting School details ");
		return "NORMAL FLOW !!! - School Name -  " + schoolname + " :::  Student Detail";
	}
	
	@SuppressWarnings("unused")
	private String callStudentServiceAndGetData_Fallback(String schoolname) { 
		System.out.println("Student Service is down!!! fallback route enabled...");
		return "CIRCUIT BREAKER ENABLED!!!No Response From Student Service at this moment. Service will be back shortly - " + new Date();
	}
	
}
