package com.oocode;

import okhttp3.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static com.oocode.Main.width;

public class Windows {
    private String[] windowsOptions;
    private String serverMessage;
    private String largeOrderEndPoint;
    private String smallOrderEndPoint;
    private String account;

    public Windows(String largeOrderEndPoint, String smallOrderEndPoint, String... windowsOptions) {
        initConfigurations("", largeOrderEndPoint, smallOrderEndPoint, windowsOptions);
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

    private void initConfigurations(String account, String largeOrderEndPoint, String smallOrderEndPoint, String... windowsOptions) {
        this.windowsOptions = windowsOptions;
        this.account = account;
        this.largeOrderEndPoint = largeOrderEndPoint;
        this.smallOrderEndPoint = smallOrderEndPoint;
    }

    public void invoke() throws IOException {
        OkHttpClient client = new OkHttpClient();

        // the thickness of the frame depends on the model of window
        // the glass pane is the size of the window minus allowance for
        // the thickness of the frame

        RequestBody requestBody = BodyBuilder.bodyBuilder(Integer.parseInt(windowsOptions[0]), Integer.parseInt(windowsOptions[1]), Integer.parseInt(windowsOptions[2]), width(windowsOptions[3], true), width(windowsOptions[3], false), getAccount());
        if (Integer.parseInt(windowsOptions[1]) > 120 || getTotalGlassArea() > 3000) {
            requestBody = BodyBuilder.bodyBuilder2(Integer.parseInt(windowsOptions[0]), Integer.parseInt(windowsOptions[1]), Integer.parseInt(windowsOptions[2]), width(windowsOptions[3], true), width(windowsOptions[3], false), getAccount());
        }

        String endpoint;
        if (getTotalGlassArea() > 20000 ||
                (Integer.parseInt(windowsOptions[1]) > 120 && getTotalGlassArea() > 18000)) {
            endpoint = getLargeOrderEndPoint();
        } else {
            endpoint = getSmallOrderEndPoint();
        }
        placeOrder(client, requestBody, endpoint);
    }

    private int getTotalGlassArea() {
        return (Integer.parseInt(windowsOptions[0]) - width(windowsOptions[3], true)) * (Integer.parseInt(windowsOptions[1]) - width(windowsOptions[3], false)) * Integer.parseInt(windowsOptions[2]);
    }

    private void placeOrder(OkHttpClient client, RequestBody requestBody, String endPoint) throws IOException {
        Request request = new Request.Builder()
                .url(endPoint)
                .method("POST", RequestBody.create(null, new byte[0]))
                .post(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            try (ResponseBody body = response.body()) {
                assert body != null;
                serverMessage = body.string();
            }
        }
    }

    String getSmallOrderEndPoint() {
        return smallOrderEndPoint;
    }

    String getLargeOrderEndPoint() {
        return largeOrderEndPoint;
    }

    String getServerMessage() {
        return serverMessage;
    }

    String getAccount() {
        return account;
    }
}
