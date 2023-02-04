package com.example.famiorg.assets;

import android.content.Context;
import android.content.Intent;

public class IntentUtils {
    private static IntentUtils instance;

    public static IntentUtils getInstance() {
        return instance;
    }

    public static void initHelper() {
        if (instance == null)
            instance = new IntentUtils();
    }

    public void openPage(Context from, Class to) {
        Intent intent = new Intent(from, to);
        from.startActivity(intent);
    }

    public void openPageWithFlag(Context from, Class to, int flag) {
        Intent intent = new Intent(from, to).setFlags(flag);
        from.startActivity(intent);
    }
}
