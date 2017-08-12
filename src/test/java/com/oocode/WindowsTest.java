package com.oocode;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.stringContainsInOrder;
import static org.junit.Assert.assertThat;

public class WindowsTest {
    MockWebServer webServer;

    @Before
    public void setUp() throws Exception {
        webServer = new MockWebServer();
        webServer.start();
    }

    @Test
    public void testPlaceSmallOrder() throws Exception {
        HttpUrl url = webServer.url("/order");
        webServer.enqueue(new MockResponse().setBody(""));
        String[] args = {"48", "36", "1", "Victoria"};
        Windows windows = new Windows("", url.toString(), args);
        windows.invoke();
        RecordedRequest request = webServer.takeRequest();
        String body = request.getBody().readUtf8();
        assertThat(body, stringContainsInOrder(Arrays.asList("width", "46", "height", "33", "type", "plain")));
    }

    @Test
    public void testPlaceLargeOrder() throws Exception {
        HttpUrl url = webServer.url("/order");
        webServer.enqueue(new MockResponse().setBody(""));
        String[] args = {"123", "456", "789", "Churchill"};
        Windows windows = new Windows(url.toString(), "", args);
        windows.invoke();
        RecordedRequest request = webServer.takeRequest();
        String body = request.getBody().readUtf8();
        assertThat(body, stringContainsInOrder(Arrays.asList("width", "119", "height", "453", "type", "toughened")));
    }

    @Test
    public void testLoadingAccountAndEndpointsFromConfigurationFile() throws Exception {
        Windows windows = new Windows();
        assertThat(windows.getLargeOrderEndPoint(), equalTo("/test-largeorder"));
        assertThat(windows.getSmallOrderEndPoint(), equalTo("/test-smallorder"));
        assertThat(windows.getAccount(), equalTo("unittest"));
    }

    @After
    public void tearDown() throws Exception {
        webServer.shutdown();
    }
}
