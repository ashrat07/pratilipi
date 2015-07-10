package com.pratilipi.android.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ShelfContent implements Parcelable {

	public long id;
	public long pratilipiId;
	public String content;
	public String language;

	public ShelfContent(long id, long pratilipiId, String content,
			String language) {
		this.id = id;
		this.pratilipiId = pratilipiId;
		this.content = content;
		this.language = language;
	}

	protected ShelfContent(Parcel in) {
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

	public static final Parcelable.Creator<ShelfContent> CREATOR = new Parcelable.Creator<ShelfContent>() {
		@Override
		public ShelfContent createFromParcel(Parcel in) {
			return new ShelfContent(in);
		}

		@Override
		public ShelfContent[] newArray(int size) {
			return new ShelfContent[size];
		}
	};
}
