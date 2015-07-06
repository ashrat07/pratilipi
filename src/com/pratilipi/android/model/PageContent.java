package com.pratilipi.android.model;

import android.os.Parcel;
import android.os.Parcelable;

public class PageContent implements Parcelable {

	public long id;
	public long pratilipiId;
	public int pageNo;
	public String content;
	public String language;

	public PageContent(long id, long pratilipiId, int pageNo, String content,
			String language) {
		this.id = id;
		this.pratilipiId = pratilipiId;
		this.pageNo = pageNo;
		this.content = content;
		this.language = language;
	}

	protected PageContent(Parcel in) {
		id = in.readLong();
		pratilipiId = in.readLong();
		pageNo = in.readInt();
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
		dest.writeInt(pageNo);
		dest.writeString(content);
		dest.writeString(language);
	}

	public static final Parcelable.Creator<PageContent> CREATOR = new Parcelable.Creator<PageContent>() {
		@Override
		public PageContent createFromParcel(Parcel in) {
			return new PageContent(in);
		}

		@Override
		public PageContent[] newArray(int size) {
			return new PageContent[size];
		}
	};
}
