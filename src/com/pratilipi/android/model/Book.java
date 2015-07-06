package com.pratilipi.android.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class Book implements Parcelable {

	private static final String ID = "id";
	private static final String TYPE = "type";
	private static final String PAGE_URL = "pageUrl";
	private static final String PAGE_URL_ALIAS = "pageUrlAlias";
	private static final String COVER_IMAGE_URL = "coverImageUrl";
	private static final String READER_PAGE_URL = "readerPageUrl";
	private static final String WRITER_PAGE_URL = "writerPageUrl";
	private static final String TITLE = "title";
	private static final String TITLE_EN = "titleEn";
	private static final String LANGUAGE_ID = "languageId";
	private static final String LANGUAGE = "language";
	private static final String AUTHOR_ID = "authorId";
	private static final String AUTHOR = "author";
	private static final String SUMMARY = "summary";
	private static final String INDEX = "index";
	private static final String PAGE_COUNT = "pageCount";
	private static final String CONTENT_TYPE = "contentType";
	private static final String STATE = "state";
	private static final String GENRE_ID_LIST = "genreIdList";
	private static final String GENRE_NAME_LIST = "genreNameList";
	private static final String READ_COUNT = "readCount";
	private static final String RATING_COUNT = "ratingCount";
	private static final String STAR_COUNT = "starCount";
	private static final String RELEVANCE = "relevance";

	public long id;
	public String type;
	public String pageUrl;
	public String pageUrlAlias;
	public String coverImageUrl;
	public String readerPageUrl;
	public String writerPageUrl;
	public String title;
	public String titleEn;
	public long languageId;
	public Language language;
	public long authorId;
	public Author author;
	public String summary;
	public List<Chapter> index;
	public int pageCount;
	public String contentType;
	public String state;
	public List<Long> genreIdList;
	public List<String> genreNameList;
	public int readCount;
	public int ratingCount;
	public int starCount;
	public double relevance;

	public Book(JSONObject obj) {
		try {
			if (obj.has(ID)) {
				this.id = obj.getLong(ID);
			}
			if (obj.has(TYPE)) {
				this.type = obj.getString(TYPE);
			}
			if (obj.has(PAGE_URL)) {
				this.pageUrl = obj.getString(PAGE_URL);
			}
			if (obj.has(PAGE_URL_ALIAS)) {
				this.pageUrlAlias = obj.getString(PAGE_URL_ALIAS);
			}
			if (obj.has(COVER_IMAGE_URL)) {
				String url = obj.getString(COVER_IMAGE_URL);
				this.coverImageUrl = url.replace("//", "http://");
			}
			if (obj.has(READER_PAGE_URL)) {
				this.readerPageUrl = obj.getString(READER_PAGE_URL);
			}
			if (obj.has(WRITER_PAGE_URL)) {
				this.writerPageUrl = obj.getString(WRITER_PAGE_URL);
			}
			if (obj.has(TITLE)) {
				this.title = obj.getString(TITLE);
			}
			if (obj.has(TITLE_EN)) {
				this.titleEn = obj.getString(TITLE_EN);
			}
			if (obj.has(LANGUAGE_ID)) {
				this.languageId = obj.getLong(LANGUAGE_ID);
			}
			if (obj.has(LANGUAGE)) {
				this.language = new Language(obj.getJSONObject(LANGUAGE));
			}
			if (obj.has(AUTHOR_ID)) {
				this.authorId = obj.getLong(AUTHOR_ID);
			}
			if (obj.has(AUTHOR)) {
				this.author = new Author(obj.getJSONObject(AUTHOR));
			}
			if (obj.has(SUMMARY)) {
				this.summary = obj.getString(SUMMARY);
			}
			if (obj.has(INDEX)) {
				String indexString = obj.getString(INDEX);
				JSONArray array = new JSONArray(indexString);
				if (array != null) {
					this.index = new ArrayList<>();
					for (int i = 0; i < array.length(); i++) {
						JSONObject chapterObj = array.getJSONObject(i);
						this.index.add(new Chapter(chapterObj));
					}
				}
			}
			if (obj.has(PAGE_COUNT)) {
				this.pageCount = obj.getInt(PAGE_COUNT);
			}
			if (obj.has(CONTENT_TYPE)) {
				this.contentType = obj.getString(CONTENT_TYPE);
			}
			if (obj.has(STATE)) {
				this.state = obj.getString(STATE);
			}
			if (obj.has(GENRE_ID_LIST)) {
				JSONArray array = obj.getJSONArray(GENRE_ID_LIST);
				if (array != null) {
					this.genreIdList = new ArrayList<>();
					for (int i = 0; i < array.length(); i++) {
						this.genreIdList.add(array.getLong(i));
					}
				}
			}
			if (obj.has(GENRE_NAME_LIST)) {
				JSONArray array = obj.getJSONArray(GENRE_NAME_LIST);
				if (array != null) {
					this.genreNameList = new ArrayList<>();
					for (int i = 0; i < array.length(); i++) {
						this.genreNameList.add(array.getString(i));
					}
				}
			}
			if (obj.has(READ_COUNT)) {
				this.readCount = obj.getInt(READ_COUNT);
			}
			if (obj.has(RATING_COUNT)) {
				this.ratingCount = obj.getInt(RATING_COUNT);
			}
			if (obj.has(STAR_COUNT)) {
				this.starCount = obj.getInt(STAR_COUNT);
			}
			if (obj.has(RELEVANCE)) {
				this.relevance = obj.getDouble(RELEVANCE);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	protected Book(Parcel in) {
		id = in.readLong();
		type = in.readString();
		pageUrl = in.readString();
		pageUrlAlias = in.readString();
		coverImageUrl = in.readString();
		readerPageUrl = in.readString();
		writerPageUrl = in.readString();
		title = in.readString();
		titleEn = in.readString();
		languageId = in.readLong();
		language = (Language) in.readValue(Language.class.getClassLoader());
		authorId = in.readLong();
		author = (Author) in.readValue(Author.class.getClassLoader());
		summary = in.readString();
		if (in.readByte() == 0x01) {
			index = new ArrayList<Chapter>();
			in.readList(index, Chapter.class.getClassLoader());
		} else {
			index = null;
		}
		pageCount = in.readInt();
		contentType = in.readString();
		state = in.readString();
		if (in.readByte() == 0x01) {
			genreIdList = new ArrayList<Long>();
			in.readList(genreIdList, Long.class.getClassLoader());
		} else {
			genreIdList = null;
		}
		if (in.readByte() == 0x01) {
			genreNameList = new ArrayList<String>();
			in.readList(genreNameList, String.class.getClassLoader());
		} else {
			genreNameList = null;
		}
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
		dest.writeString(pageUrl);
		dest.writeString(pageUrlAlias);
		dest.writeString(coverImageUrl);
		dest.writeString(readerPageUrl);
		dest.writeString(writerPageUrl);
		dest.writeString(title);
		dest.writeString(titleEn);
		dest.writeLong(languageId);
		dest.writeValue(language);
		dest.writeLong(authorId);
		dest.writeValue(author);
		dest.writeString(summary);
		if (index == null) {
			dest.writeByte((byte) (0x00));
		} else {
			dest.writeByte((byte) (0x01));
			dest.writeList(index);
		}
		dest.writeInt(pageCount);
		dest.writeString(contentType);
		dest.writeString(state);
		if (genreIdList == null) {
			dest.writeByte((byte) (0x00));
		} else {
			dest.writeByte((byte) (0x01));
			dest.writeList(genreIdList);
		}
		if (genreNameList == null) {
			dest.writeByte((byte) (0x00));
		} else {
			dest.writeByte((byte) (0x01));
			dest.writeList(genreNameList);
		}
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
