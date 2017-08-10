package com.oocode;

import okhttp3.*;

import java.io.IOException;

import static com.oocode.Main.*;

class Windows {
    private String[] args;

    private String serverMessage;

    public String getServerMessage() {
        return serverMessage;
    }

    public Windows(String... args) {
        this.args = args;
    }

    public void invoke() throws IOException {
        int n = Integer.parseInt(args[2]);  // the number of windows of this size
        int w = Integer.parseInt(args[0]);  // the width of the window
        int h = Integer.parseInt(args[1]);  // the height of the window
        String r = args[3];                 // the model name of these windows
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
                    .url("https://immense-fortress-19979.herokuapp.com/large-order")
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
                .url("https://immense-fortress-19979.herokuapp.com/order")
                .method("POST", RequestBody.create(null, new byte[0]))
                .post(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            try (ResponseBody body = response.body()) {
                assert body != null; serverMessage=body.string();
            }
        }
    }
}
