package com.rapidftr.task;

import com.rapidftr.CustomTestRunner;
import com.rapidftr.R;
import com.rapidftr.RapidFtrApplication;
import com.rapidftr.activity.ChangePasswordActivity;
import com.rapidftr.service.ChangePasswordService;
import com.rapidftr.utils.http.FluentResponse;
import com.xtremelabs.robolectric.shadows.ShadowHandler;
import com.xtremelabs.robolectric.shadows.ShadowToast;
import com.xtremelabs.robolectric.tester.org.apache.http.TestHttpResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(CustomTestRunner.class)
public class ChangePasswordTaskTest {

    private ChangePasswordTask changePasswordTask;
    @Mock
    private ChangePasswordService changePasswordService;
    private RapidFtrApplication application;


    @Before
    public void setUp() throws Exception {
        application = spy(RapidFtrApplication.getApplicationInstance());
        changePasswordService = mock(ChangePasswordService.class);
        changePasswordTask = spy(new ChangePasswordTask(changePasswordService, application));
    }

    @Test
    public void shouldCallUpdatePassword() throws IOException {
        doReturn(new FluentResponse(new TestHttpResponse(201, "created"))).when(changePasswordService).updatePassword("param1", "param2", "param3");
        changePasswordTask.doInBackground("param1", "param2", "param3");
        verify(changePasswordService).updatePassword("param1", "param2", "param3");
    }

    @Test
    public void shouldShowToastMessageIfPasswordIsChanged() {
        changePasswordTask.setActivity(new ChangePasswordActivity());
        changePasswordTask.onPostExecute(true);
        ShadowHandler.idleMainLooper();
        assertThat(ShadowToast.getTextOfLatestToast(), equalTo(application.getString(R.string.password_change_success)));
    }
}
