package com.vincler.jf.projet6.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class IntentUtils {

    public static void callNumber(Context context, String phoneNumber) {

        Intent callPhone = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+ phoneNumber));
        context.startActivity(callPhone);
    }
}
