package com.pratilipi.android.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Shelf implements Parcelable {

	public long id;
	public long pratilipiId;
	public String content;
	public String language;

	public Shelf(long id, long pratilipiId, String content, String language) {
		this.id = id;
		this.pratilipiId = pratilipiId;
		this.content = content;
		this.language = language;
	}

	protected Shelf(Parcel in) {
		id = in.readLong();
		pratilipiId = in.readLong();
		content = in.readString();
		language = in.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(id);
		dest.writeLong(pratilipiId);
		dest.writeString(content);
		dest.writeString(language);
	}

	public static final Parcelable.Creator<Shelf> CREATOR = new Parcelable.Creator<Shelf>() {
		@Override
		public Shelf createFromParcel(Parcel in) {
			return new Shelf(in);
		}

		@Override
		public Shelf[] newArray(int size) {
			return new Shelf[size];
		}
	};
}
