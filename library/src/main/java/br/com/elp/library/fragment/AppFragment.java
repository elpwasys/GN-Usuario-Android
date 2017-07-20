package br.com.elp.library.fragment;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;

/**
 * Created by pascke on 03/08/16.
 */
public abstract class AppFragment extends Fragment implements ActivityCompat.OnRequestPermissionsResultCallback {

    protected boolean grantedResults(int[] grantResults) {
        boolean granted = true;
        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                granted = false;
                break;
            }
        }
        return granted;
    }

    protected boolean checkedSelfPermission(String[] permissions) {
        boolean checked = true;
        Context context = getContext();
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                checked = false;
                break;
            }
        }
        return checked;
    }
}