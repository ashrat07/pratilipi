package com.pratilipi.android.model;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class Author implements Parcelable {

	private static final String ID = "id";
	private static final String PAGE_URL = "pageUrl";
	private static final String PAGE_URL_ALIAS = "pageUrlAlias";
	private static final String AUTHOR_IMAGE_URL = "authorImageUrl";
	private static final String LANGUAGE_ID = "languageId";
	private static final String FIRST_NAME = "firstName";
	private static final String LAST_NAME = "lastName";
	private static final String PEN_NAME = "penName";
	private static final String NAME = "name";
	private static final String FULL_NAME = "fullName";
	private static final String FIRST_NAME_EN = "firstNameEn";
	private static final String LAST_NAME_EN = "lastNameEn";
	private static final String PEN_NAME_EN = "penNameEn";
	private static final String NAME_EN = "nameEn";
	private static final String FULL_NAME_EN = "fullNameEn";
	private static final String SUMMARY = "summary";
	private static final String EMAIL = "email";

	public long id;
	public String pageUrl;
	public String pageUrlAlias;
	public String authorImageUrl;
	public long languageId;
	public String firstName;
	public String lastName;
	public String penName;
	public String name;
	public String fullName;
	public String firstNameEn;
	public String lastNameEn;
	public String penNameEn;
	public String nameEn;
	public String fullNameEn;
	public String summary;
	public String email;

	public Author(JSONObject obj) {
		try {
			if (obj.has(ID)) {
				this.id = obj.getLong(ID);
			}
			if (obj.has(PAGE_URL)) {
				this.pageUrl = obj.getString(PAGE_URL);
			}
			if (obj.has(PAGE_URL_ALIAS)) {
				this.pageUrlAlias = obj.getString(PAGE_URL_ALIAS);
			}
			if (obj.has(AUTHOR_IMAGE_URL)) {
				this.authorImageUrl = obj.getString(AUTHOR_IMAGE_URL);
			}
			if (obj.has(LANGUAGE_ID)) {
				this.languageId = obj.getLong(LANGUAGE_ID);
			}
			if (obj.has(FIRST_NAME)) {
				this.firstName = obj.getString(FIRST_NAME);
			}
			if (obj.has(LAST_NAME)) {
				this.lastName = obj.getString(LAST_NAME);
			}
			if (obj.has(PEN_NAME)) {
				this.penName = obj.getString(PEN_NAME);
			}
			if (obj.has(NAME)) {
				this.name = obj.getString(NAME);
			}
			if (obj.has(FULL_NAME)) {
				this.fullName = obj.getString(FULL_NAME);
			}
			if (obj.has(FIRST_NAME_EN)) {
				this.firstNameEn = obj.getString(FIRST_NAME_EN);
			}
			if (obj.has(LAST_NAME_EN)) {
				this.lastNameEn = obj.getString(LAST_NAME_EN);
			}
			if (obj.has(PEN_NAME_EN)) {
				this.penNameEn = obj.getString(PEN_NAME_EN);
			}
			if (obj.has(NAME_EN)) {
				this.nameEn = obj.getString(NAME_EN);
			}
			if (obj.has(FULL_NAME_EN)) {
				this.fullNameEn = obj.getString(FULL_NAME_EN);
			}
			if (obj.has(SUMMARY)) {
				this.summary = obj.getString(SUMMARY);
			}
			if (obj.has(EMAIL)) {
				this.email = obj.getString(EMAIL);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	protected Author(Parcel in) {
		id = in.readLong();
		pageUrl = in.readString();
		pageUrlAlias = in.readString();
		authorImageUrl = in.readString();
		languageId = in.readLong();
		firstName = in.readString();
		lastName = in.readString();
		penName = in.readString();
		name = in.readString();
		fullName = in.readString();
		firstNameEn = in.readString();
		lastNameEn = in.readString();
		nameEn = in.readString();
		penNameEn = in.readString();
		fullNameEn = in.readString();
		summary = in.readString();
		email = in.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(id);
		dest.writeString(pageUrl);
		dest.writeString(pageUrlAlias);
		dest.writeString(authorImageUrl);
		dest.writeLong(languageId);
		dest.writeString(firstName);
		dest.writeString(lastName);
		dest.writeString(penName);
		dest.writeString(name);
		dest.writeString(fullName);
		dest.writeString(firstNameEn);
		dest.writeString(lastNameEn);
		dest.writeString(penNameEn);
		dest.writeString(nameEn);
		dest.writeString(fullNameEn);
		dest.writeString(summary);
		dest.writeString(email);
	}

	public static final Parcelable.Creator<Author> CREATOR = new Parcelable.Creator<Author>() {
		@Override
		public Author createFromParcel(Parcel in) {
			return new Author(in);
		}

		@Override
		public Author[] newArray(int size) {
			return new Author[size];
		}
	};
}
