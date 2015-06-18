package com.pratilipi.android.model;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class Category implements Parcelable {

	private static final String ID = "id";
	private static final String NAME = "name";
	private static final String TYPE = "type";

	public long id;
	public String name;
	public String type;

	public Category(JSONObject obj) {
		try {
			if (obj.has(ID)) {
				this.id = obj.getLong(ID);
			}
			if (obj.has(NAME)) {
				this.name = obj.getString(NAME);
			}
			if (obj.has(TYPE)) {
				this.type = obj.getString(TYPE);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	protected Category(Parcel in) {
		id = in.readLong();
		name = in.readString();
		type = in.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(id);
		dest.writeString(name);
		dest.writeString(type);
	}

	public static final Parcelable.Creator<Category> CREATOR = new Parcelable.Creator<Category>() {
		@Override
		public Category createFromParcel(Parcel in) {
			return new Category(in);
		}

		@Override
		public Category[] newArray(int size) {
			return new Category[size];
		}
	};
}
