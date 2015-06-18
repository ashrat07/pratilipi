package com.pratilipi.android.model;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class Language implements Parcelable {

	private static final String ID = "id";
	private static final String NAME = "name";
	private static final String NAME_EN = "nameEn";

	public long id;
	public String name;
	public String nameEn;

	public Language(JSONObject obj) {
		try {
			if (obj.has(ID)) {
				this.id = obj.getLong(ID);
			}
			if (obj.has(NAME)) {
				this.name = obj.getString(NAME);
			}
			if (obj.has(NAME_EN)) {
				this.nameEn = obj.getString(NAME_EN);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	protected Language(Parcel in) {
		id = in.readLong();
		name = in.readString();
		nameEn = in.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(id);
		dest.writeString(name);
		dest.writeString(nameEn);
	}

	public static final Parcelable.Creator<Language> CREATOR = new Parcelable.Creator<Language>() {
		@Override
		public Language createFromParcel(Parcel in) {
			return new Language(in);
		}

		@Override
		public Language[] newArray(int size) {
			return new Language[size];
		}
	};
}
