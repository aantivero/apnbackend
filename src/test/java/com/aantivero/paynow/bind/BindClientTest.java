package com.aantivero.paynow.bind;

import com.aantivero.paynow.bind.model.Token;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@Slf4j
@RunWith(JUnit4.class)
public class BindClientTest {

    private LoginClient loginClient;

    @Before
    public void setUp() {
        BindControllerFeignClientBuilder builder = new BindControllerFeignClientBuilder();
        loginClient = builder.getLoginClient();
    }

    @Test
    public void testLoginOk(){
        Token token = loginClient.login("alejandro.antivero@gmail.com", "M13YTa225t1UpxA");
        log.debug("TOKEN: " + token.getToken());

    }
}
