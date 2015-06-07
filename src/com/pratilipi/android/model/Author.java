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
	private static final String HAS_LANGUAGE_ID = "hasLanguageId";
	private static final String FIRST_NAME = "firstName";
	private static final String HAS_FIRST_NAME = "hasFirstName";
	private static final String LAST_NAME = "lastName";
	private static final String HAS_LAST_NAME = "hasLastName";
	// private static final String PEN_NAME = "penName";
	// private static final String HAS_PEN_NAME = "hasPenName";
	private static final String NAME = "name";
	private static final String FULL_NAME = "fullName";
	private static final String FIRST_NAME_EN = "firstNameEn";
	private static final String HAS_FIRST_NAME_EN = "hasFirstNameEn";
	private static final String LAST_NAME_EN = "lastNameEn";
	private static final String HAS_LAST_NAME_EN = "hasLastNameEn";
	// private static final String PEN_NAME_EN = "penNameEn";
	// private static final String HAS_PEN_NAME_EN = "hasPenNameEn";
	private static final String NAME_EN = "nameEn";
	private static final String FULL_NAME_EN = "fullNameEn";
	private static final String HAS_SUMMARY = "hasSummary";
	private static final String EMAIL = "email";
	private static final String HAS_EMAIL = "hasEmail";
	private static final String REGISTRATION_DATE = "registrationDate";
	private static final String CONTENT_PUBLISHED = "contentPublished";

	public long id;
	public String pageUrl;
	public String pageUrlAlias;
	public String authorImageUrl;
	public long languageId;
	public boolean hasLanguageId;
	public String firstName;
	public boolean hasFirstName;
	public String lastName;
	public boolean hasLastName;
	// public String penName;
	// public boolean hasPenName;
	public String name;
	public String fullName;
	public String firstNameEn;
	public boolean hasFirstNameEn;
	public String lastNameEn;
	public boolean hasLastNameEn;
	// public String penNameEn;
	// public boolean hasPenNameEn;
	public String nameEn;
	public String fullNameEn;
	public boolean hasSummary;
	public String email;
	public boolean hasEmail;
	public long registrationDate;
	public int contentPublished;

	public Author(JSONObject obj) {
		try {
			this.id = obj.getLong(ID);
			this.pageUrl = obj.getString(PAGE_URL);
			this.pageUrlAlias = obj.getString(PAGE_URL_ALIAS);
			this.authorImageUrl = obj.getString(AUTHOR_IMAGE_URL);
			this.languageId = obj.getLong(LANGUAGE_ID);
			this.hasLanguageId = obj.getBoolean(HAS_LANGUAGE_ID);
			this.firstName = obj.getString(FIRST_NAME);
			this.hasFirstName = obj.getBoolean(HAS_FIRST_NAME);
			this.lastName = obj.getString(LAST_NAME);
			this.hasLastName = obj.getBoolean(HAS_LAST_NAME);
			this.name = obj.getString(NAME);
			this.fullName = obj.getString(FULL_NAME);
			this.firstNameEn = obj.getString(FIRST_NAME_EN);
			this.hasFirstNameEn = obj.getBoolean(HAS_FIRST_NAME_EN);
			this.lastNameEn = obj.getString(LAST_NAME_EN);
			this.hasLastNameEn = obj.getBoolean(HAS_LAST_NAME_EN);
			this.nameEn = obj.getString(NAME_EN);
			this.fullNameEn = obj.getString(FULL_NAME_EN);
			this.hasSummary = obj.getBoolean(HAS_SUMMARY);
			this.email = obj.getString(EMAIL);
			this.hasEmail = obj.getBoolean(HAS_EMAIL);
			this.registrationDate = obj.getLong(REGISTRATION_DATE);
			this.contentPublished = obj.getInt(CONTENT_PUBLISHED);
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
		hasLanguageId = in.readByte() != 0x00;
		firstName = in.readString();
		hasFirstName = in.readByte() != 0x00;
		lastName = in.readString();
		hasLastName = in.readByte() != 0x00;
		name = in.readString();
		fullName = in.readString();
		firstNameEn = in.readString();
		hasFirstNameEn = in.readByte() != 0x00;
		lastNameEn = in.readString();
		hasLastNameEn = in.readByte() != 0x00;
		nameEn = in.readString();
		fullNameEn = in.readString();
		hasSummary = in.readByte() != 0x00;
		email = in.readString();
		hasEmail = in.readByte() != 0x00;
		registrationDate = in.readLong();
		contentPublished = in.readInt();
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
		dest.writeByte((byte) (hasLanguageId ? 0x01 : 0x00));
		dest.writeString(firstName);
		dest.writeByte((byte) (hasFirstName ? 0x01 : 0x00));
		dest.writeString(lastName);
		dest.writeByte((byte) (hasLastName ? 0x01 : 0x00));
		dest.writeString(name);
		dest.writeString(fullName);
		dest.writeString(firstNameEn);
		dest.writeByte((byte) (hasFirstNameEn ? 0x01 : 0x00));
		dest.writeString(lastNameEn);
		dest.writeByte((byte) (hasLastNameEn ? 0x01 : 0x00));
		dest.writeString(nameEn);
		dest.writeString(fullNameEn);
		dest.writeByte((byte) (hasSummary ? 0x01 : 0x00));
		dest.writeString(email);
		dest.writeByte((byte) (hasEmail ? 0x01 : 0x00));
		dest.writeLong(registrationDate);
		dest.writeInt(contentPublished);
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
