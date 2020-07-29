package com.anahitavakoli.ketabjooyan.utility;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class Util extends Application
{

	public static final String TAG = Util.class.getSimpleName();

	private RequestQueue mRequestQueue;

	private static Util mInstance;

	@Override
	public void onCreate()
	{
		super.onCreate();
		mInstance = this;
	}

	public static synchronized Util getInstance()
	{
		return mInstance;
	}

	public RequestQueue getRequestQueue()
	{
		if (mRequestQueue == null)
		{
			mRequestQueue = Volley.newRequestQueue(getApplicationContext());
		}

		return mRequestQueue;
	}

	public <T> void addToRequestQueue(Request<T> req, String tag)
	{
		// set the default tag if tag is empty
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
		getRequestQueue().add(req);
	}

	public <T> void addToRequestQueue(Request<T> req)
	{
		req.setTag(TAG);
		getRequestQueue().add(req);
	}

	public void cancelPendingRequests(Object tag)
	{
		if (mRequestQueue != null)
		{
			mRequestQueue.cancelAll(tag);
		}
	}

	public void cancelPendingRequests()
	{
		if (mRequestQueue != null)
		{
			mRequestQueue.cancelAll(TAG);
		}
	}
}