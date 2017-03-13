package com.example.yodgor777.themoviedb;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 * Created by yodgor777 on 2017-02-02.
 */

public class NetworkHelper {
    private static NetworkHelper instance;
    private Context context;
    private RequestQueue requestQueue;

    public NetworkHelper(Context context) {
        this.context = context;
    }

    public static synchronized NetworkHelper getInstance(Context context) {
        if (instance == null) {
            instance = new NetworkHelper(context);
        }
        return instance;
    }

    public void doNetworkRequestFor(String url, final NetworkCallBack callBack) {
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callBack.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callBack.onError(error);
            }
        });

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
        }
        requestQueue.add(request);
    }

    public interface NetworkCallBack {
        void onSuccess(String response);

        void onError(VolleyError error);
    }

}
