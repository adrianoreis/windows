package com.oocode;

import okhttp3.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static com.oocode.Main.*;

class Windows {
    private String[] windowsOptions;
    private String serverMessage;
    private String largeOrderEndPoint;
    private String smallOrderEndPoint;
    private String account;

    public Windows(String largeOrderEndPoint, String smallOrderEndPoint, String ...windowsOptions) {
        initConfigurations("", largeOrderEndPoint, smallOrderEndPoint, windowsOptions);
    }
    private void initConfigurations(String account, String largeOrderEndPoint, String smallOrderEndPoint, String ...windowsOptions) {
        this.windowsOptions = windowsOptions;
        this.account = account;
        this.largeOrderEndPoint = largeOrderEndPoint;
        this.smallOrderEndPoint = smallOrderEndPoint;
    }

    public Windows(String... args) throws IOException {
        Properties properties = new Properties();
        try (InputStream is = this.getClass().getClassLoader().
                getResourceAsStream("config.properties")) {
            properties.load(is);
            initConfigurations(
                    properties.getProperty("account"),
                    properties.getProperty("largeorderurl"),
                    properties.getProperty("smallorderurl"), args);
        }
    }

    public void invoke() throws IOException {
        int n = Integer.parseInt(windowsOptions[2]);  // the number of windows of this size
        int w = Integer.parseInt(windowsOptions[0]);  // the width of the window
        int h = Integer.parseInt(windowsOptions[1]);  // the height of the window
        String r = windowsOptions[3];                 // the model name of these windows
        OkHttpClient client = new OkHttpClient();

        // the thickness of the frame depends on the model of window
        int width = width(r, true);
        int height = width(r, false);

        RequestBody requestBody = BodyBuilder.bodyBuilder(w, h, n, width, height);
        if (h > 120) requestBody = BodyBuilder.bodyBuilder2(w, h, n, width, height);

        // the glass pane is the size of the window minus allowance for
        // the thickness of the frame
        if ((w-width) * (h-height) * n > 20000) {
            Request request = new Request.Builder()
                    .url(getLargeOrderEndPoint())
                    .method("POST", RequestBody.create(null, new byte[0]))
                    .post(requestBody)
                    .build();

        try (Response response = client.newCall(request).execute()) {
            try (ResponseBody body = response.body()) {
                assert body != null;
                serverMessage=body.string();
            }
        }
        return;
        }

        Request request = new Request.Builder()
                .url(getSmallOrderEndPoint())
                .method("POST", RequestBody.create(null, new byte[0]))
                .post(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            try (ResponseBody body = response.body()) {
                assert body != null; serverMessage=body.string();
            }
        }
    }

    protected String getSmallOrderEndPoint() {
        return smallOrderEndPoint;
    }

    protected String getLargeOrderEndPoint() {
        return largeOrderEndPoint;
    }

    public String getServerMessage() {
        return serverMessage;
    }

    public String getAccount(){
        return account;
    }
}
