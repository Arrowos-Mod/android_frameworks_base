
/*
 * Copyright (C) 2020 The Pixel Experience Project
 *               2020 The exTHmUI Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.internal.util.custom;

import android.app.Application;
import android.os.Build;
import android.util.Log;

import java.util.Arrays;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class XiaomiPropsUtils {

    private static final String TAG = XiaomiPropsUtils.class.getSimpleName();
    private static final boolean DEBUG = false;

    private static final Map<String, Object> propsToChange;

    private static final String[] packagesToChange = {
        "com.tencent.mm"
    };

    static {
        propsToChange = new HashMap<>();
        propsToChange.put("BRAND", "Xiaomi");
        propsToChange.put("MANUFACTURER", "Xiaomi");
        propsToChange.put("DEVICE", "nabu");
        propsToChange.put("PRODUCT", "nabu");
        propsToChange.put("MODEL", "21051182C");
        propsToChange.put(
            "FINGERPRINT", "Xiaomi/nabu/nabu:12/RKQ1.200826.002/22.8.17:user/release-keys");
    }

    public static void setProps(Application app) {
        final String packageName = app.getPackageName();

        if (packageName == null){
            return;
        }
        if (Arrays.asList(packagesToChange).contains(packageName)){
            if (DEBUG){
                Log.d(TAG, "Defining props for: " + packageName);
            }
            for (Map.Entry<String, Object> prop : propsToChange.entrySet()) {
                String key = prop.getKey();
                Object value = prop.getValue();
                setPropValue(key, value);
            }
        }
    }

    private static void setPropValue(String key, Object value){
        try {
            if (DEBUG){
                Log.d(TAG, "Defining prop " + key + " to " + value.toString());
            }
            Field field = Build.class.getDeclaredField(key);
            field.setAccessible(true);
            field.set(null, value);
            field.setAccessible(false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            Log.e(TAG, "Failed to set prop " + key, e);
        }
    }
}