package br.com.wasys.gn.usuario.endpoint;

import android.content.Context;
import android.os.Build;

import br.com.wasys.gn.usuario.Application;
import br.com.wasys.gn.usuario.BuildConfig;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

import br.com.elp.library.enumerator.DeviceHeader;
import br.com.elp.library.enumerator.MediaType;
import br.com.elp.library.utils.AndroidUtils;

/**
 * Created by pascke on 03/08/16.
 */
public class Endpoint {

    public static final String BASE_URL = BuildConfig.BASE_URL;

    public static <T> T create(Class<T> clazz) {
        Map<String, String> headers = getHeaders();
        return br.com.elp.library.http.Endpoint.create(clazz, BASE_URL, headers);
    }

    public static <T> T create(Class<T> clazz, MediaType mediaType) {
        Map<String, String> headers = getHeaders();
        return br.com.elp.library.http.Endpoint.create(clazz, BASE_URL, mediaType, headers);
    }

    public static Map<String, String> getHeaders() {
        Context context = Application.getContext();
        Map<String, String> headers = new HashMap<>();
        headers.put(DeviceHeader.DEVICE_SO.key, "Android");
        headers.put(DeviceHeader.DEVICE_SO_VERSION.key, Build.VERSION.RELEASE);
        headers.put(DeviceHeader.DEVICE_MODEL.key, Build.MODEL);
        headers.put(DeviceHeader.DEVICE_IMEI.key, AndroidUtils.getIMEI(context));
        headers.put(DeviceHeader.DEVICE_WIDTH.key, String.valueOf(AndroidUtils.getWidthPixels(context)));
        headers.put(DeviceHeader.DEVICE_HEIGHT.key, String.valueOf(AndroidUtils.getHeightPixels(context)));
        headers.put(DeviceHeader.DEVICE_APP_VERSION.key, String.valueOf(AndroidUtils.getVersionCode(context)));
        String authorization = Application.getAuthorization();
        if (StringUtils.isNotBlank(authorization)) {
            headers.put(DeviceHeader.AUTHORIZATION.key, authorization);
        }
        return headers;
    }
}
