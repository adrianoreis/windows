package com.oocode;

import okhttp3.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static com.oocode.Main.width;

public class Windows {
    private String[] options;
    private String serverMessage;
    private String largeOrderEndPoint;
    private String smallOrderEndPoint;
    private String account;

    /**
     *
     * @param largeOrderEndPoint endpoint to place large orders
     * @param smallOrderEndPoint endpoint to place small orders
     * @param windowsOptions options of the order: width, height, quantity, model
     */
    public Windows(String largeOrderEndPoint, String smallOrderEndPoint, String... windowsOptions) {
        initConfigurations("", largeOrderEndPoint, smallOrderEndPoint, windowsOptions);
    }

    /**
     *
     * @param windowsOptions options of the order: width, height, quantity, model
     * @throws IOException
     */
    public Windows(String... windowsOptions) throws IOException {
        Properties properties = new Properties();
        try (InputStream is = this.getClass().getClassLoader().
                getResourceAsStream("config.properties")) {
            properties.load(is);
            initConfigurations(
                    properties.getProperty("account"),
                    properties.getProperty("largeorderurl"),
                    properties.getProperty("smallorderurl"), windowsOptions);
        }
    }

    private void initConfigurations(String account, String largeOrderEndPoint, String smallOrderEndPoint, String... windowsOptions) {
        this.options = windowsOptions;
        this.account = account;
        this.largeOrderEndPoint = largeOrderEndPoint;
        this.smallOrderEndPoint = smallOrderEndPoint;
    }

    public void invoke() throws IOException {
        RequestBody requestBody = getRequestBody();
        String endpoint;

        if (getTotalGlassArea() > 20000 ||
                (Integer.parseInt(options[1]) > 120 && getTotalGlassArea() > 18000)) {
            endpoint = getLargeOrderEndPoint();
        } else {
            endpoint = getSmallOrderEndPoint();
        }

        placeOrder(getOkHttpClient(), requestBody, endpoint);
    }

    protected OkHttpClient getOkHttpClient() {
        return new OkHttpClient.Builder().connectTimeout(25, TimeUnit.SECONDS).readTimeout(25, TimeUnit.SECONDS).writeTimeout(25, TimeUnit.SECONDS).build();
    }

    protected RequestBody getRequestBody() {
        if (Integer.parseInt(options[1]) > 120 || getTotalGlassArea() > 3000) {
            return BodyBuilder.bodyBuilder(Integer.parseInt(options[0]), Integer.parseInt(options[1]),
                    Integer.parseInt(options[2]), width(options[3], true), width(options[3], false), account, "toughened");
        } else {
            return BodyBuilder.bodyBuilder(Integer.parseInt(options[0]), Integer.parseInt(options[1]),
                    Integer.parseInt(options[2]), width(options[3], true), width(options[3], false), account, "plain");
        }
    }

    /**
     * the thickness of the frame depends on the model of window
     * the glass pane is the size of the window minus allowance for
     * the thickness of the frame
     * @return glass area to be ordered
     */
    private int getTotalGlassArea() {
        return (Integer.parseInt(options[0]) - width(options[3], true)) * (Integer.parseInt(options[1]) - width(options[3], false)) * Integer.parseInt(options[2]);
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
