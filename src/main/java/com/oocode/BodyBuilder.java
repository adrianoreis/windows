package com.oocode;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class BodyBuilder {
    public static RequestBody bodyBuilder(int w, int h, int n, int width, int height, String account) {
        return new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("account", account)
                .addFormDataPart("quantity", Integer.toString(n))
                .addFormDataPart("width", Integer.toString(w - width))
                .addFormDataPart("height", Integer.toString(h - height))
                .addFormDataPart("type", "plain")
                .build();
    }

    public static RequestBody bodyBuilder2(int w, int h, int n, int width, int height, String account) {
        return new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("account", account)
                .addFormDataPart("quantity", Integer.toString(n))
                .addFormDataPart("width", Integer.toString(w - width))
                .addFormDataPart("height", Integer.toString(h - height))
                .addFormDataPart("type", "toughened")
                .build();
    }
}
