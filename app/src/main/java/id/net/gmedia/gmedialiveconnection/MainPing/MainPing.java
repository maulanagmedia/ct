package id.net.gmedia.gmedialiveconnection.MainPing;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
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
import com.maulana.custommodul.ItemValidation;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Random;

import fr.bmartel.speedtest.SpeedTestReport;
import fr.bmartel.speedtest.SpeedTestSocket;
import fr.bmartel.speedtest.inter.ISpeedTestListener;
import fr.bmartel.speedtest.model.SpeedTestError;
import id.net.gmedia.gmedialiveconnection.R;
import pl.pawelkleczkowski.customgauge.CustomGauge;

public class MainPing extends Fragment {

    private Context context;
    private View layout;
    private ItemValidation iv = new ItemValidation();

    private CustomGauge gauge2;

    private int i;
    private TextView text2;
    private Button btnProses;
    private TextView tvUpload, tvDownload;

    public MainPing() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        layout = inflater.inflate(R.layout.fragment_main_ping, container, false);
        context = getContext();

        initUI();
        initEvent();

        return layout;
    }

    private void initUI() {

        tvUpload = (TextView) layout.findViewById(R.id.tv_upload);
        tvDownload = (TextView) layout.findViewById(R.id.tv_download);
        btnProses = (Button) layout.findViewById(R.id.btn_proses);
        gauge2 = layout.findViewById(R.id.gauge2);
        gauge2.setEndValue(100);
        gauge2.setStartValue(0);

        text2  = layout.findViewById(R.id.textView2);
        text2.setText(Integer.toString(gauge2.getValue()));

    }

    private void initEvent(){

        btnProses.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                /*new Thread() {
                    public void run() {
                        for (i = 0; i <= 100; i++) {
                            try {
                                ((Activity)context).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        gauge2.setValue(i);
                                        text2.setText(Integer.toString(gauge2.getValue()));
                                    }
                                });
                                Thread.sleep(50);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }.start();*/
                gauge2.setValue(0);
                text2.setText(Integer.toString(gauge2.getValue()));

                new SpeedTestTask().execute();
            }

        });
    }

    public class SpeedTestTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {

            SpeedTestSocket speedTestSocket = new SpeedTestSocket();

            // add a listener to wait for speedtest completion and progress
            speedTestSocket.addSpeedTestListener(new ISpeedTestListener() {

                @Override
                public void onCompletion(final SpeedTestReport report) {
                    // called when download/upload is finished

                    ((Activity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            tvDownload.setText(getSpeed(report.getTransferRateBit()));
                        }
                    });

                    Log.v("speedtest", "[COMPLETED] rate in octet/s : " + report.getTransferRateOctet());
                    Log.v("speedtest", "[COMPLETED] rate in bit/s   : " + report.getTransferRateBit());
                }

                @Override
                public void onError(SpeedTestError speedTestError, String errorMessage) {
                    // called when a download/upload error occur
                }

                @Override
                public void onProgress(final float percent, final SpeedTestReport report) {
                    // called to notify download/upload progress
                    ((Activity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            gauge2.setValue((int) percent);
                            text2.setText(Integer.toString(gauge2.getValue()) + " %");
                            tvDownload.setText(getSpeed(report.getTransferRateBit()));
                        }
                    });

                    Log.v("speedtest", "[PROGRESS] progress : " + percent + "%");
                    Log.v("speedtest", "[PROGRESS] rate in octet/s : " + report.getTransferRateOctet());
                    Log.v("speedtest", "[PROGRESS] rate in bit/s   : " + report.getTransferRateBit());
                }
            });

            speedTestSocket.startDownload("http://ipv4.ikoula.testdebit.info/1M.iso");

            return null;
        }
    }

    public String getSpeed(BigDecimal size) {

        long sizeCeil = size.longValue();

        if (sizeCeil <= 0)
            return "0";

        final String[] units = new String[] { "b/s", "Kb/s", "Mb/s", "Gb/s", "Tb/s" };
        int digitGroups = (int) (Math.log10(sizeCeil) / Math.log10(1024));

        return new DecimalFormat("#,##0.#").format(sizeCeil / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
}
