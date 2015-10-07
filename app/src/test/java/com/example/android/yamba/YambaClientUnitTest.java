package com.example.android.yamba;

import android.text.TextUtils;

import com.thenewcircle.yamba.client.YambaClient;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(TextUtils.class)
public class YambaClientUnitTest {
    @Rule
    public ExpectedException expected = ExpectedException.none();

    @Before
    public void setUpMocks() {
        mockStatic(TextUtils.class);

        when(TextUtils.isEmpty(null)).thenReturn(true);
        when(TextUtils.isEmpty("")).thenReturn(true);
    }

    @Test
    public void emptyUsernameThrowsIllegalArgumentException() {
        //set up the expectation result
        expected.expect(IllegalArgumentException.class);

        String username = null;
        String password = "password";
        YambaClient.getClient(username, password);
    }

    @Test
    public void emptyPasswordThrowsIllegalArgumentException() {
        //set up the expectation result
        expected.expect(IllegalArgumentException.class);

        String username = "student";
        String password = null;
        YambaClient.getClient(username, password);
    }
}
