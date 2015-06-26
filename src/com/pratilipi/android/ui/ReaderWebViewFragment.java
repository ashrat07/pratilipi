package com.pratilipi.android.ui;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.pratilipi.android.R;
import com.pratilipi.android.http.HttpGet;
import com.pratilipi.android.http.HttpResponseListener;
import com.pratilipi.android.model.Shelf;
import com.pratilipi.android.util.PConstants;

public class ReaderWebViewFragment extends Fragment implements
		HttpResponseListener {

	public static final String TAG_NAME = "Reader Web View";

	private View mRootView;
	private WebView mWebView;
	private Shelf mShelf;

	public String getCustomTag() {
		return TAG_NAME;
	}

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_reader_web_view,
				container, false);
		mWebView = (WebView) mRootView.findViewById(R.id.web_view);

		Bundle bundle = getArguments();
		if (bundle != null) {
			Shelf shelf = (Shelf) bundle.getParcelable("SHELF");
			if (shelf != null) {
				mShelf = shelf;
				requestContent();
			}
		}

		return mRootView;
	}

	private void requestContent() {
		HttpGet contentRequest = new HttpGet(this, PConstants.CONTENT_URL);

		HashMap<String, String> requestHashMap = new HashMap<>();
		requestHashMap.put(PConstants.URL, PConstants.CONTENT_URL);
		requestHashMap.put("pratilipiId", "" + mShelf.pratilipiId);

		contentRequest.run(requestHashMap);
	}

	@Override
	public Boolean setGetStatus(JSONObject finalResult, String getUrl,
			int responseCode) {
		if (PConstants.CONTENT_URL.equals(getUrl)) {
			try {
				String pageContent = finalResult.getString("pageContent");
				String font;
				if (PConstants.CONTENT_LANGUAGE.HINDI.toString().equals(
						mShelf.language)) {
					font = "Mangal.ttf";
				} else if (PConstants.CONTENT_LANGUAGE.TAMIL.toString().equals(
						mShelf.language)) {
					font = "Tamil.ttf";
				} else if (PConstants.CONTENT_LANGUAGE.GUJARATI.toString()
						.equals(mShelf.language)) {
					font = "Gujarati.ttf";
				} else {
					font = "Montserrat-Regular.ttf";
				}

				this.copyFile(this.getActivity(), font);
				mWebView.loadDataWithBaseURL(null,
						getHtmlData(this.getActivity(), font, pageContent),
						"text/html", "utf-8", "about:blank");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private boolean copyFile(Context context, String font) {
		boolean status = false;
		try {
			FileOutputStream out = context.openFileOutput(font,
					Context.MODE_PRIVATE);
			InputStream in = context.getAssets().open("fonts/" + font);
			// Transfer bytes from the input file to the output file
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			// Close the streams
			out.close();
			in.close();
			status = true;
		} catch (Exception e) {
			status = false;
			e.printStackTrace();
		}
		return status;
	}

	private String getHtmlData(Context context, String font, String data) {
		String head = "<head><style>@font-face { font-family: 'customFont'; src: url('file://"
				+ context.getFilesDir().getAbsolutePath()
				+ "/"
				+ font
				+ "');}body {font-family: 'customFont';}</style></head>";
		String htmlData = "<html>" + head + "<body>" + data + "</body></html>";
		return htmlData;
	}

	@Override
	public Boolean setPostStatus(JSONObject finalResult, String postUrl,
			int responseCode) {
		return null;
	}

}
