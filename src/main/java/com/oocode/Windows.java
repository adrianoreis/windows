package com.oocode;

import okhttp3.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static com.oocode.Main.*;

public class Windows {
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
        int windowWidth = Integer.parseInt(windowsOptions[0]);  // the width of the window
        int windowHeight = Integer.parseInt(windowsOptions[1]);  // the height of the window
        int numberOfWindows = Integer.parseInt(windowsOptions[2]);  // the number of windows of this size
        String windowModelName = windowsOptions[3];                 // the model name of these windows
        OkHttpClient client = new OkHttpClient();

        // the thickness of the frame depends on the model of window
        int width = width(windowModelName, true);
        int height = width(windowModelName, false);

        RequestBody requestBody = BodyBuilder.bodyBuilder(windowWidth, windowHeight, numberOfWindows, width, height, getAccount());
        if (windowHeight > 120) requestBody = BodyBuilder.bodyBuilder2(windowWidth, windowHeight, numberOfWindows, width, height, getAccount());

        // the glass pane is the size of the window minus allowance for
        // the thickness of the frame
        int totalGlassArea = (windowWidth - width) * (windowHeight - height) * numberOfWindows;
        if (totalGlassArea > 20000 ||
                (windowHeight > 120 && totalGlassArea > 18000)){
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
