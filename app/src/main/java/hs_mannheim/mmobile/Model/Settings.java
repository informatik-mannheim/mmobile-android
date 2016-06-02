package hs_mannheim.mmobile.Model;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.provider.Settings.SettingNotFoundException;
import android.util.Log;

public class Settings {
    private Context _context;
    private ApplicationInfo _ai;

    public Settings(Context context) {
        _context = context;

        try {
            _ai = _context.getPackageManager().getApplicationInfo(_context.getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            // ignore
        }

    }

    public String getString(String key, String defaultValue) {
        Object o =  _ai.metaData.get(key);
        return o != null ? (String) o : defaultValue;

    }

    public float getFloat(String key, float defaultValue) {
        Object o =  _ai.metaData.get(key);
        return o != null ? (float) o : defaultValue;
    }
}
