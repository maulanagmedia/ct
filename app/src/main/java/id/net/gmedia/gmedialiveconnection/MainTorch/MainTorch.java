package id.net.gmedia.gmedialiveconnection.MainTorch;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.maulana.custommodul.ApiVolley;
import com.maulana.custommodul.CustomItem;
import com.maulana.custommodul.ItemValidation;
import com.maulana.custommodul.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import id.net.gmedia.gmedialiveconnection.MainTorch.Adapter.ListTorchAdapter;
import id.net.gmedia.gmedialiveconnection.NotificationUtils.InitFirebaseSetting;
import id.net.gmedia.gmedialiveconnection.R;
import id.net.gmedia.gmedialiveconnection.utils.ServerUrl;

public class MainTorch extends Fragment {

    private static Context context;
    private static SessionManager session;
    private View layout;
    private ListView lvTorch;
    private Button btnProses, btnStop;
    private List<CustomItem> listTorch = new ArrayList<>();
    private ListTorchAdapter adapter;
    public static boolean isActive = false;
    private static final String TAG = "TORCH";
    private ProgressBar pbLoading;

    public MainTorch() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        layout = inflater.inflate(R.layout.fragment_main_torch, container, false);
        context = getContext();
        session = new SessionManager(context);

        initUI();
        initEvent();

        return layout;
    }

    private void initUI() {

        lvTorch = (ListView) layout.findViewById(R.id.lv_torch);
        btnProses = (Button) layout.findViewById(R.id.btn_proses);
        btnStop = (Button) layout.findViewById(R.id.btn_stop);
        pbLoading = (ProgressBar) layout.findViewById(R.id.pb_loading);

        adapter = new ListTorchAdapter((Activity) context, listTorch);
        lvTorch.setAdapter(adapter);

        /*listTorch.add(new CustomItem(
                "1"
                ,"101.101.101.12"
                ,"Rumah"
                ,"101.101.101.17"
                ,"12 Mb"
                ,"10 Mb"
        ));

        listTorch.add(new CustomItem(
                "1"
                ,"101.101.101.13"
                ,"Rumah Tetangga"
                ,"101.101.101.16"
                ,"11 Mb"
                ,"10 Mb"
        ));

        adapter.notifyDataSetChanged();*/


    }

    private void initEvent(){

        btnProses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!isActive){

                    pbLoading.setVisibility(View.VISIBLE);
                    isActive = true;
                    getDataTorch();
                }
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isActive){

                    disconnectLive();
                }
            }
        });
    }

    private void getDataTorch() {

        JSONObject jBody = new JSONObject();
        try {
            jBody.put("id_site", session.getIdSite());
            jBody.put("to", InitFirebaseSetting.token);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String idSite = session.getIdSite();
        ApiVolley request = new ApiVolley(context, jBody, "GET", ServerUrl.getTorchTraffic + idSite, 0, new ApiVolley.VolleyCallback() {
            @Override
            public void onSuccess(String result) {

                pbLoading.setVisibility(View.GONE);
                Log.d(TAG, "onSuccess: " + result);
                listTorch.clear();

                try {
                    JSONObject response = new JSONObject(result);
                    String status = response.getJSONObject("metadata").getString("status");
                    if(status.equals("200")){

                        JSONArray ja = response.getJSONObject("response").getJSONArray("devices");

                        for(int i = 0; i < ja.length(); i++){

                            JSONObject jo = ja.getJSONObject(i);

                            listTorch.add(new CustomItem(
                                    String.valueOf(i)
                                    ,jo.getString("ip_device")
                                    ,jo.getString("name_device")
                                    ,jo.getString("ip_dst")
                                    ,jo.getString("tr_download") + " Mb"
                                    ,jo.getString("tr_upload") + " Mb"
                                    ,jo.getString("tr_download")
                            ));
                        }

                        Collections.sort(listTorch, new Comparator<CustomItem>() {
                            @Override
                            public int compare(CustomItem lhs, CustomItem rhs) {
                                // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending

                                ItemValidation iv = new ItemValidation();

                                double downloadL = iv.parseNullDouble(lhs.getItem7());
                                double downloadR = iv.parseNullDouble(rhs.getItem7());

                                return downloadL > downloadR ? -1 : (downloadL < downloadR) ? 1 : 0;
                            }
                        });
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                adapter.notifyDataSetChanged();

                if(isActive){
                    getDataTorch();
                }
            }

            @Override
            public void onError(String result) {

                Log.d(TAG, "onError: " + result);
                pbLoading.setVisibility(View.GONE);
                if(isActive){
                    getDataTorch();
                }
            }
        });

    }

    public static void disconnectLive(){

        try {

            ApiVolley.cancelALL();
            isActive = false;
            stopTraffict();
        }catch (Exception e) {}

    }

    private static void stopTraffict() {

        JSONObject jBody = new JSONObject();
        String idSite = session.getIdSite();
        ApiVolley request = new ApiVolley(context, jBody, "GET", ServerUrl.stopTorchConnection+idSite, 0, new ApiVolley.VolleyCallback() {
            @Override
            public void onSuccess(String result) {

                //disconnectLive();
                Log.d(TAG, "onSuccess: " + result);
            }

            @Override
            public void onError(String result) {

                //disconnectLive();
                Log.d(TAG, "onError: " + result);
            }
        });
    }
}
