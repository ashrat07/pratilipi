package com.pratilipi.android.model;

import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import com.pratilipi.android.util.PConstants;

import android.os.Parcel;
import android.os.Parcelable;

public class Book implements Parcelable {

	private static final String ID = "id";
	private static final String TYPE = "type";
	private static final String HAS_TYPE = "hasType";
	private static final String PAGE_URL = "pageUrl";
	private static final String PAGE_URL_ALIAS = "pageUrlAlias";
	private static final String COVER_IMAGE_URL = "coverImageUrl";
	private static final String READER_PAGE_URL = "readerPageUrl";
	private static final String WRITER_PAGE_URL = "writerPageUrl";
	private static final String TITLE = "title";
	private static final String HAS_TITLE = "hasTitle";
	private static final String TITLE_EN = "titleEn";
	private static final String HAS_TITLE_EN = "hasTitleEn";
	private static final String LANGUAGE_ID = "languageId";
	private static final String HAS_LANGUAGE_ID = "hasLanguageId";
	private static final String LANGUAGE = "language";
	private static final String AUTHOR_ID = "authorId";
	private static final String HAS_AUTHOR_ID = "hasAuthorId";
	private static final String AUTHOR = "author";
	private static final String HAS_PUBLICATION_YEAR = "hasPublicationYear";
	private static final String LISTING_DATE = "listingDate";
	private static final String LAST_UPDATED = "lastUpdated";
	private static final String SUMMARY = "summary";
	private static final String HAS_SUMMARY = "hasSummary";
	private static final String HAS_INDEX = "hasIndex";
	private static final String HAS_WORD_COUNT = "hasWordCount";
	private static final String PAGE_COUNT = "pageCount";
	private static final String HAS_PAGE_COUNT = "hasPageCount";
	private static final String CONTENT_TYPE = "contentType";
	private static final String HAS_CONTENT_TYPE = "hasContentType";
	private static final String STATE = "state";
	private static final String HAS_STATE = "hasState";
	private static final String READ_COUNT = "readCount";
	private static final String RATING_COUNT = "ratingCount";
	private static final String STAR_COUNT = "starCount";
	private static final String RELEVANCE = "relevance";

	public long id;
	public String type;
	public boolean hasType;
	public String pageUrl;
	public String pageUrlAlias;
	public String coverImageUrl;
	public String readerPageUrl;
	public String writerPageUrl;
	public String title;
	public boolean hasTitle;
	public String titleEn;
	public boolean hasTitleEn;
	public long languageId;
	public boolean hasLanguageId;
	public Language language;
	public long authorId;
	public boolean hasAuthorId;
	public Author author;
	public boolean hasPublicationYear;
	public String listingDate;
	public String lastUpdated;
	public String summary;
	public boolean hasSummary;
	public boolean hasIndex;
	public boolean hasWordCount;
	public int pageCount;
	public boolean hasPageCount;
	public String contentType;
	public boolean hasContentType;
	public String state;
	public boolean hasState;
	public int readCount;
	public int ratingCount;
	public int starCount;
	public double relevance;

	public Book(JSONObject obj) {
		try {
			this.id = obj.getLong(ID);
			this.type = obj.getString(TYPE);
			this.hasType = obj.getBoolean(HAS_TYPE);
			this.pageUrl = obj.getString(PAGE_URL);
			this.pageUrlAlias = obj.getString(PAGE_URL_ALIAS);
			// this.coverImageUrl = obj.getString(COVER_IMAGE_URL);
			this.coverImageUrl = PConstants.COVER_IMAGE_URL.replace(
					PConstants.PLACEHOLDER_PRATILIPI_ID, "" + this.id);
			this.readerPageUrl = obj.getString(READER_PAGE_URL);
			this.writerPageUrl = obj.getString(WRITER_PAGE_URL);
			this.title = obj.getString(TITLE);
			this.hasTitle = obj.getBoolean(HAS_TITLE);
			this.titleEn = obj.getString(TITLE_EN);
			this.hasTitleEn = obj.getBoolean(HAS_TITLE_EN);
			this.languageId = obj.getLong(LANGUAGE_ID);
			this.hasLanguageId = obj.getBoolean(HAS_LANGUAGE_ID);
			this.language = new Language(obj.getJSONObject(LANGUAGE));
			this.authorId = obj.getLong(AUTHOR_ID);
			this.hasAuthorId = obj.getBoolean(HAS_AUTHOR_ID);
			this.author = new Author(obj.getJSONObject(AUTHOR));
			this.hasPublicationYear = obj.getBoolean(HAS_PUBLICATION_YEAR);
			this.listingDate = obj.getString(LISTING_DATE);
			this.lastUpdated = obj.getString(LAST_UPDATED);
			this.summary = obj.getString(SUMMARY);
			this.hasSummary = obj.getBoolean(HAS_SUMMARY);
			this.hasIndex = obj.getBoolean(HAS_INDEX);
			this.hasWordCount = obj.getBoolean(HAS_WORD_COUNT);
			this.pageCount = obj.getInt(PAGE_COUNT);
			this.hasPageCount = obj.getBoolean(HAS_PAGE_COUNT);
			this.contentType = obj.getString(CONTENT_TYPE);
			this.hasContentType = obj.getBoolean(HAS_CONTENT_TYPE);
			this.state = obj.getString(STATE);
			this.hasState = obj.getBoolean(HAS_STATE);
			this.readCount = obj.getInt(READ_COUNT);
			this.ratingCount = obj.getInt(RATING_COUNT);
			this.starCount = obj.getInt(STAR_COUNT);
			this.relevance = obj.getDouble(RELEVANCE);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	protected Book(Parcel in) {
		id = in.readLong();
		type = in.readString();
		hasType = in.readByte() != 0x00;
		pageUrl = in.readString();
		pageUrlAlias = in.readString();
		coverImageUrl = in.readString();
		readerPageUrl = in.readString();
		writerPageUrl = in.readString();
		title = in.readString();
		hasTitle = in.readByte() != 0x00;
		titleEn = in.readString();
		hasTitleEn = in.readByte() != 0x00;
		languageId = in.readLong();
		hasLanguageId = in.readByte() != 0x00;
		language = (Language) in.readValue(Language.class.getClassLoader());
		authorId = in.readLong();
		hasAuthorId = in.readByte() != 0x00;
		author = (Author) in.readValue(Author.class.getClassLoader());
		hasPublicationYear = in.readByte() != 0x00;
		listingDate = in.readString();
		lastUpdated = in.readString();
		hasSummary = in.readByte() != 0x00;
		hasIndex = in.readByte() != 0x00;
		hasWordCount = in.readByte() != 0x00;
		pageCount = in.readInt();
		hasPageCount = in.readByte() != 0x00;
		contentType = in.readString();
		hasContentType = in.readByte() != 0x00;
		state = in.readString();
		hasState = in.readByte() != 0x00;
		readCount = in.readInt();
		ratingCount = in.readInt();
		starCount = in.readInt();
		relevance = in.readDouble();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(id);
		dest.writeString(type);
		dest.writeByte((byte) (hasType ? 0x01 : 0x00));
		dest.writeString(pageUrl);
		dest.writeString(pageUrlAlias);
		dest.writeString(coverImageUrl);
		dest.writeString(readerPageUrl);
		dest.writeString(writerPageUrl);
		dest.writeString(title);
		dest.writeByte((byte) (hasTitle ? 0x01 : 0x00));
		dest.writeString(titleEn);
		dest.writeByte((byte) (hasTitleEn ? 0x01 : 0x00));
		dest.writeLong(languageId);
		dest.writeByte((byte) (hasLanguageId ? 0x01 : 0x00));
		dest.writeValue(language);
		dest.writeLong(authorId);
		dest.writeByte((byte) (hasAuthorId ? 0x01 : 0x00));
		dest.writeValue(author);
		dest.writeByte((byte) (hasPublicationYear ? 0x01 : 0x00));
		dest.writeString(listingDate);
		dest.writeString(lastUpdated);
		dest.writeByte((byte) (hasSummary ? 0x01 : 0x00));
		dest.writeByte((byte) (hasIndex ? 0x01 : 0x00));
		dest.writeByte((byte) (hasWordCount ? 0x01 : 0x00));
		dest.writeInt(pageCount);
		dest.writeByte((byte) (hasPageCount ? 0x01 : 0x00));
		dest.writeString(contentType);
		dest.writeByte((byte) (hasContentType ? 0x01 : 0x00));
		dest.writeString(state);
		dest.writeByte((byte) (hasState ? 0x01 : 0x00));
		dest.writeInt(readCount);
		dest.writeInt(ratingCount);
		dest.writeInt(starCount);
		dest.writeDouble(relevance);
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
