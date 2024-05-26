package com.example.gabi.administrador;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;

import java.util.Map;

public class InputStreamVolleyRequest extends Request<byte[]> {
    private final Response.Listener<byte[]> mListener;
    private final Map<String, String> mParams;

    // Create a static map for directly accessing headers
    private Map<String, String> responseHeaders;

    public InputStreamVolleyRequest(int method, String url, Response.Listener<byte[]> listener,
                                    Response.ErrorListener errorListener, Map<String, String> params) {
        super(method, url, errorListener);
        // This request would never use cache.
        setShouldCache(false);
        mListener = listener;
        mParams = params;
    }

    @Override
    public Map<String, String> getParams() throws AuthFailureError {
        return mParams;
    }

    @Override
    protected void deliverResponse(byte[] response) {
        mListener.onResponse(response);
    }

    @Override
    protected Response<byte[]> parseNetworkResponse(NetworkResponse response) {
        // Initialize local responseHeaders map with response headers received
        responseHeaders = response.headers;

        // Pass the response data here
        return Response.success(response.data, HttpHeaderParser.parseCacheHeaders(response));
    }

    public Map<String, String> getResponseHeaders() {
        return responseHeaders;
    }
}
