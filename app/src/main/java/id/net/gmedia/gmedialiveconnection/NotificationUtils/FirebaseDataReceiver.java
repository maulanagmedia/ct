package id.net.gmedia.gmedialiveconnection.NotificationUtils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.maulana.custommodul.ItemValidation;

import id.net.gmedia.gmedialiveconnection.MainActivity;
import id.net.gmedia.gmedialiveconnection.MainLiveCart.MainLiveChart;

public class FirebaseDataReceiver extends WakefulBroadcastReceiver {

    private final String TAG = "FirebaseDataReceiver";
    private ItemValidation iv = new ItemValidation();

    public void onReceive(Context context, Intent intent) {

        Bundle dataBundle = intent.getExtras();
        if(dataBundle != null) {

            String trDownload = dataBundle.getString("tr_download");
            String trUpload = dataBundle.getString("tr_upload");
            //Log.d(TAG, "onReceive: ");

            if(MainLiveChart.isActive){

                MainLiveChart.addDownloadEntry(iv.parseNullDouble(trDownload), iv.parseNullDouble(trUpload));
                //MainLiveChart.addUploadEntry(iv.parseNullDouble(trUpload));
            }
        }
    }
}