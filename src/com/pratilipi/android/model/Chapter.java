package com.pratilipi.android.model;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class Chapter implements Parcelable {

	private static final String TITLE = "title";
	private static final String PAGE_NO = "pageNo";
	private static final String LEVEL = "level";

	public String title;
	public int pageNo;
	public int level;

	public Chapter(JSONObject obj) {
		try {
			if (obj.has(TITLE)) {
				this.title = obj.getString(title);
			}
			if (obj.has(PAGE_NO)) {
				this.pageNo = obj.getInt(PAGE_NO);
			}
			if (obj.has(LEVEL)) {
				this.level = obj.getInt(LEVEL);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	protected Chapter(Parcel in) {
		title = in.readString();
		pageNo = in.readInt();
		level = in.readInt();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(title);
		dest.writeInt(pageNo);
		dest.writeInt(level);
	}

	public static final Parcelable.Creator<Chapter> CREATOR = new Parcelable.Creator<Chapter>() {
		@Override
		public Chapter createFromParcel(Parcel in) {
			return new Chapter(in);
		}

		@Override
		public Chapter[] newArray(int size) {
			return new Chapter[size];
		}
	};

}
