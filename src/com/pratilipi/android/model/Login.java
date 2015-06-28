package com.pratilipi.android.model;

public class Login {
	public String loginName;
	public String loginPassword;
	public String loginType;
 
	public Login(String loginName,String loginPassword, String loginType)
	{
		this.loginName= loginName;
		this.loginPassword= loginPassword;
		this.loginType= loginType;
	}
	public Login(){}
	
	
	
}
