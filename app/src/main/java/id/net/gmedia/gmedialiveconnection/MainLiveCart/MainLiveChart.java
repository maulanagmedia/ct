package id.net.gmedia.gmedialiveconnection.MainLiveCart;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.maulana.custommodul.ApiVolley;
import com.maulana.custommodul.ItemValidation;
import com.maulana.custommodul.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import id.net.gmedia.gmedialiveconnection.NotificationUtils.InitFirebaseSetting;
import id.net.gmedia.gmedialiveconnection.R;
import id.net.gmedia.gmedialiveconnection.utils.ServerUrl;

public class MainLiveChart extends Fragment {

    private static Context context;
    private static SessionManager session;
    private View layout;
    private static GraphView gvLiveDownload;
    private static LineGraphSeries<DataPoint> seriesDownload, seriesUpload;
    private static int lastXDownload = 0, lastXUpload = 0;
    private static Viewport viewportDownload;
    private static List<Double> listYDownload = new ArrayList<>(), listYUpload = new ArrayList<>();
    private static int maxList = 10;
    public static boolean isActive = false;
    private static Button btnProses;
    private static String TAG = "LIVECHART";
    private static ItemValidation iv = new ItemValidation();
    private static TextView tvDownload, tvUpload;

    public MainLiveChart() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        layout = inflater.inflate(R.layout.fragment_main_live_chart, container, false);
        context = getContext();
        session = new SessionManager(context);

        initUI();
        initEvent();

        return layout;
    }

    private void initUI() {

        gvLiveDownload = (GraphView) layout.findViewById(R.id.gv_live_download);
        tvDownload = (TextView) layout.findViewById(R.id.tv_download);
        tvUpload = (TextView) layout.findViewById(R.id.tv_upload);
        btnProses = (Button) layout.findViewById(R.id.btn_proses);

        seriesDownload = new LineGraphSeries<DataPoint>();
        seriesDownload.setColor(context.getResources().getColor(R.color.color_trafd));
        seriesUpload = new LineGraphSeries<DataPoint>();
        seriesUpload.setColor(context.getResources().getColor(R.color.color_trafu));

        gvLiveDownload.addSeries(seriesDownload);
        gvLiveDownload.addSeries(seriesUpload);

        // customize a little bit viewport
        viewportDownload = gvLiveDownload.getViewport();
        viewportDownload.setYAxisBoundsManual(true);
        viewportDownload.setMinY(0);
        viewportDownload.setScrollable(true);
        viewportDownload.setXAxisBoundsManual(true);
        viewportDownload.setBorderColor(Color.WHITE);

        /*viewportUpload = gvLiveUpload.getViewport();
        viewportUpload.setYAxisBoundsManual(true);
        viewportUpload.setMinY(0);
        viewportUpload.setScrollable(true);
        viewportUpload.setXAxisBoundsManual(true);*/

        /*new Thread(new Runnable() {

            @Override
            public void run() {
                // we add 100 new entries
                for (int i = 0; i < 100; i++) {
                    ((Activity)context).runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            addEntry();
                        }
                    });

                    // sleep to slow down the add of entries
                    try {
                        Thread.sleep(600);
                    } catch (InterruptedException e) {
                        // manage error ...
                    }
                }
            }
        }).start();*/
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        disconnectLive();
    }

    private void initEvent(){

        btnProses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isActive){

                    disconnectLive();
                    btnProses.setText("Mulai");
                    btnProses.setTextColor(context.getResources().getColor(R.color.color_default));
                    btnProses.setBackground(context.getResources().getDrawable(R.drawable.btn_oval_blue));
                }else{

                    isActive = true;

                    lastXDownload = 0;
                    listYDownload = new ArrayList<>();

                    lastXUpload = 0;
                    listYUpload = new ArrayList<>();

                    btnProses.setText("Berhenti");
                    btnProses.setTextColor(context.getResources().getColor(R.color.color_red));
                    btnProses.setBackground(context.getResources().getDrawable(R.drawable.btn_oval_red));

                    getDataLiveTraffict();
                }


            }
        });
    }

    private void getDataLiveTraffict() {

        JSONObject jBody = new JSONObject();
        try {
            jBody.put("id_site", session.getIdSite());
            jBody.put("to", InitFirebaseSetting.token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ApiVolley request = new ApiVolley(context, jBody, "POST", ServerUrl.getLiveTraffic, 0, new ApiVolley.VolleyCallback() {
            @Override
            public void onSuccess(String result) {

                disconnectLive();
                Log.d(TAG, "onSuccess: ");
            }

            @Override
            public void onError(String result) {

                disconnectLive();
                Log.d(TAG, "onError: ");
            }
        });

    }

    public static void addDownloadEntry(double trDownload, double trUpload) {
        // here, we choose to display max 10 points on the viewport and we scroll to end
        if(isActive){

            int x = lastXDownload++;
            seriesDownload.appendData(new DataPoint(x, trDownload), true, 11);
            if(listYDownload.size() > maxList){

                listYDownload.remove(0);
            }


            seriesUpload.appendData(new DataPoint(x, trUpload), true, 11);
            if(listYUpload.size() > maxList){

                listYUpload.remove(0);
            }

            listYDownload.add(trDownload);
            listYUpload.add(trUpload);
            viewportDownload.setMaxY(getMaxY());
            viewportDownload.setMinX(getMinX(x));
            viewportDownload.setMaxX(x);

            tvDownload.setText("Download " + iv.doubleToStringFull(trDownload) + " Mb");
            tvUpload.setText("Upload " + iv.doubleToStringFull(trUpload) + " Mb");
        }

    }

    /*public static void addUploadEntry(double trUpload) {
        // here, we choose to display max 10 points on the viewport and we scroll to end
        if(isActive){

            int x = lastXUpload++;
            seriesUpload.appendData(new DataPoint(x, trUpload), true, 11);
            if(listYUpload.size() > maxList){

                listYUpload.remove(0);
            }

            listYUpload.add(trUpload);
            viewportUpload.setMaxY(getMaxY(listYUpload));
            viewportUpload.setMinX(getMinX(x));
            viewportUpload.setMaxX(x);
            viewportUpload.setBorderColor(Color.RED);
            tvUpload.setText("Upload " + iv.doubleToStringFull(trUpload) + " Mb");
        }
    }*/

    private static int getMaxY(){

        int max = 0;

        for(Double d : listYUpload){

            if(max < d) max = (int) Math.ceil(d);
        }

        for(Double d : listYDownload){

            if(max < d) max = (int) Math.ceil(d);
        }

        max = (int) Math.ceil(1.5 * max);
        return max;
    }

    private static int getMinX(int x){

        int min = 0;

        if(x - maxList > 0) min = x - maxList;

        return min;
    }

    public static void disconnectLive(){

        try {
            ApiVolley.cancelALL();
            isActive = false;
            btnProses.setText("Mulai");
            btnProses.setTextColor(context.getResources().getColor(R.color.color_default));
            btnProses.setBackground(context.getResources().getDrawable(R.drawable.btn_oval_blue));
            gvLiveDownload.removeAllSeries();
            //gvLiveUpload.removeAllSeries();

            seriesDownload = new LineGraphSeries<DataPoint>();
            seriesDownload.setColor(context.getResources().getColor(R.color.color_trafd));
            seriesUpload = new LineGraphSeries<DataPoint>();
            seriesUpload.setColor(context.getResources().getColor(R.color.color_trafu));

            gvLiveDownload.addSeries(seriesDownload);
            gvLiveDownload.addSeries(seriesUpload);
            tvDownload.setText("Download");
            tvUpload.setText("Upload");

            stopTraffict();
        }catch (Exception e){}
    }

    private static void stopTraffict() {

        JSONObject jBody = new JSONObject();
        String idSite = session.getIdSite();
        ApiVolley request = new ApiVolley(context, jBody, "GET", ServerUrl.stopLoveConnection+idSite, 0, new ApiVolley.VolleyCallback() {
            @Override
            public void onSuccess(String result) {

                //disconnectLive();
                Log.d(TAG, "onSuccess: ");
            }

            @Override
            public void onError(String result) {

                //disconnectLive();
                Log.d(TAG, "onError: ");
            }
        });
    }
}
