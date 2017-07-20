package br.com.elp.library.http;

import com.google.gson.Gson;

import org.apache.commons.collections4.MapUtils;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import br.com.elp.library.enumerator.MediaType;
import br.com.elp.library.utils.GsonUtils;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by pascke on 03/08/16.
 */
public class Endpoint {

    public static <T> T create(Class<T> clazz, String baseUrl, final Map<String, String> headers) {
        return create(clazz, baseUrl, MediaType.APPLICATION_JSON, headers);
    }

    public static <T> T create(Class<T> clazz, String baseUrl, final MediaType mediaType, final Map<String, String> headers) {
        Interceptor interceptor = new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request.Builder builder = chain.request()
                        .newBuilder()
                        .addHeader("Content-Type", mediaType.value);
                if (MapUtils.isNotEmpty(headers)) {
                    Set<Map.Entry<String, String>> entries = headers.entrySet();
                    for (Map.Entry<String, String> entry : entries) {
                        builder.addHeader(entry.getKey(), entry.getValue());
                    }
                }
                Request request = builder.build();
                return chain.proceed(request);
            }
        };
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.interceptors().add(interceptor);
        OkHttpClient client = builder.build();
        Retrofit.Builder restrofitBuilder = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client);
        if (MediaType.APPLICATION_JSON.equals(mediaType)) {
            Gson gson = GsonUtils.create();
            GsonConverterFactory factory = GsonConverterFactory.create(gson);
            restrofitBuilder.addConverterFactory(factory);
        }
        Retrofit retrofit = restrofitBuilder
                .build();
        T endpoint = retrofit.create(clazz);
        return endpoint;
    }
}