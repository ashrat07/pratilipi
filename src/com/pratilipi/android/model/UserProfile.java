package com.pratilipi.android.model;

import java.util.Date;

import android.R.bool;

public class UserProfile {
	public String id;
	public boolean hasPassword;
	public String firstName;
	public boolean hasFirstName;
	public String name;
	public boolean hasLastName;
	public boolean hasName;
	public String email;
	public boolean hasEmail;
	public boolean hasDOB;
	public boolean hasGender;
	public boolean hasProfilePicUrl;
	public boolean hasFBRefreshToken;
	public boolean hasGoogleRefreshToken;
	public String status;
	public String userImgUrl="http://lorempixel.com/200/200/people/";
	public int shelfBookCount;
	public Date memberDOJ;

}
