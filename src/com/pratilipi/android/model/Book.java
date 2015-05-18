package com.pratilipi.android.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Book implements Parcelable {

	public String imageURL;
	public String title;
	public String name;
	public String artist;
	public String summary;
	public String category;

	public Book(String imageURL, String title, String name, String artist,
			String summary, String category) {
		this.imageURL = imageURL;
		this.title = title;
		this.name = name;
		this.artist = artist;
		this.summary = summary;
		this.category = category;
	}

	protected Book(Parcel in) {
		imageURL = in.readString();
		title = in.readString();
		name = in.readString();
		artist = in.readString();
		summary = in.readString();
		category = in.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(imageURL);
		dest.writeString(title);
		dest.writeString(name);
		dest.writeString(artist);
		dest.writeString(summary);
		dest.writeString(category);
	}

	public static final Parcelable.Creator<Book> CREATOR = new Parcelable.Creator<Book>() {
		@Override
		public Book createFromParcel(Parcel in) {
			return new Book(in);
		}

		@Override
		public Book[] newArray(int size) {
			return new Book[size];
		}
	};
}
