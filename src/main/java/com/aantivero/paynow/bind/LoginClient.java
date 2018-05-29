package com.aantivero.paynow.bind;

import com.aantivero.paynow.bind.model.Token;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

public interface LoginClient {

    @RequestLine("POST /login/jwt")
    @Headers("Content-Type: application/json")
    Token login(@Param("username")String username, @Param("password") String password);
}
