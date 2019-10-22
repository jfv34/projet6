package com.vincler.jf.projet6.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

public class IntentUtils {

    public static void callNumber(Context context, String phoneNumber) {

        if(phoneNumber!=null&&!phoneNumber.isEmpty()){
            Log.i("phoneNumber_call",phoneNumber);
        Intent callPhone = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+ phoneNumber));
        context.startActivity(callPhone);
    }else {Log.i("phoneNumber_call","no phoneNumber !");}
}}
