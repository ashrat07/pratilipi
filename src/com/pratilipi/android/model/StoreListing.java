package com.pratilipi.android.model;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class StoreListing implements Parcelable {

	public String id;
	public String name;
	public List<Book> content;

	public StoreListing(String id, String name, List<Book> content) {
		this.id = id;
		this.name = name;
		this.content = content;
	}

	protected StoreListing(Parcel in) {
		id = in.readString();
		name = in.readString();
		if (in.readByte() == 0x01) {
			content = new ArrayList<Book>();
			in.readList(content, Book.class.getClassLoader());
		} else {
			content = null;
		}
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(name);
		if (content == null) {
			dest.writeByte((byte) (0x00));
		} else {
			dest.writeByte((byte) (0x01));
			dest.writeList(content);
		}
	}

	public static final Parcelable.Creator<StoreListing> CREATOR = new Parcelable.Creator<StoreListing>() {
		@Override
		public StoreListing createFromParcel(Parcel in) {
			return new StoreListing(in);
		}

		@Override
		public StoreListing[] newArray(int size) {
			return new StoreListing[size];
		}
	};
}
