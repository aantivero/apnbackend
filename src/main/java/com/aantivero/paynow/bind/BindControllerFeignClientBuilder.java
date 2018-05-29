package com.aantivero.paynow.bind;

import feign.Feign;
import feign.Logger;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import lombok.Getter;

@Getter
public class BindControllerFeignClientBuilder {

    private LoginClient loginClient = createClient(LoginClient.class, "https://sandbox.bind.com.ar/v1");

    private static <T> T createClient(Class<T> type, String uri) {
        /**
         * si necesito poner un proxy
         *
         * Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("pfcajavip.cajval.sba.com.ar", 3128));
         okhttp3.OkHttpClient okHttpClient = new okhttp3.OkHttpClient.Builder().proxy(proxy).build();
         */
        return Feign.builder()
            .client(new OkHttpClient())
            .encoder(new GsonEncoder())
            .decoder(new GsonDecoder())
            .logger(new Slf4jLogger(type))
            .logLevel(Logger.Level.FULL)
            .target(type, uri);
    }
}
