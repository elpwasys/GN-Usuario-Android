package br.com.elp.library.utils;

import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by pascke on 04/08/16.
 */
public class BitmapUtils {

    public static String toBase64(Bitmap bitmap) throws IOException {
        String base64 = null;
        if (bitmap != null) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
            outputStream.flush();
            outputStream.close();
            byte[] bytes = outputStream.toByteArray();
            base64 = Base64.encodeToString(bytes, Base64.DEFAULT);
        }
        return base64;
    }
}
