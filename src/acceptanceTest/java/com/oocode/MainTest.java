package com.oocode;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

public class MainTest {
    // do not run without changing "Ivan's Windows" in com.oocode.BodyBuilder to "test"
    // otherwise you have to pay for the order
    @Test
    public void testLargeOrder() throws Exception {
        Windows windows = new Windows(new String[]{"123", "456", "789", "Churchill"});
        windows.invoke();
        assertThat(windows.getServerMessage(), equalTo("Thank you \"test\" for your large order (q=789, w=119, h=453, toughened). Order not really placed - nothing to pay"));
    }

    @Test
    public void testSmallOrder() throws Exception {
        Windows windows = new Windows(new String[]{"48", "36", "1", "Victoria"});
        windows.invoke();
        assertThat(windows.getServerMessage(), equalTo("Thank you \"test\" for your order (q=1, w=46, h=33, plain). Order not really placed - nothing to pay"));
    }

        /* EXPECTED OUTPUT

Thank you "test" for your large order (q=789, w=119, h=453, toughened). Order not really placed - nothing to pay
Thank you "test" for your order (q=1, w=46, h=33, plain). Order not really placed - nothing to pay

         */
}
