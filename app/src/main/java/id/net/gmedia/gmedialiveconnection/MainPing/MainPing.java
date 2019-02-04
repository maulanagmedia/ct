package id.net.gmedia.gmedialiveconnection.MainPing;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.net.TrafficStats;
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

import com.github.anastr.speedviewlib.PointerSpeedometer;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.maulana.custommodul.ItemValidation;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;

import fr.bmartel.speedtest.SpeedTestReport;
import fr.bmartel.speedtest.SpeedTestSocket;
import fr.bmartel.speedtest.inter.ISpeedTestListener;
import fr.bmartel.speedtest.model.SpeedTestError;
import fr.bmartel.speedtest.utils.SpeedTestUtils;
import id.net.gmedia.gmedialiveconnection.R;

import static android.content.Context.ACTIVITY_SERVICE;

public class MainPing extends Fragment {

    private Context context;
    private View layout;
    private ItemValidation iv = new ItemValidation();

    private int i;
    private Button btnProses;
    private TextView tvUpload, tvDownload;
    private static PointerSpeedometer psTest;
    private static float maxLenth = 10;

    //download
    private final static int SOCKET_TIMEOUT = 5000;
    //private final static String SPEED_TEST_SERVER_URI_DL = "http://ipv4.ikoula.testdebit.info/10M.iso";
    private final static String SPEED_TEST_SERVER_URI_DL = "https://erpsmg.gmedia.id/client_tools/file/10M.iso";

    //upload

    /**
     * spedd examples server uri.
     */
    private static final String SPEED_TEST_SERVER_URI_UL = "http://ipv4.ikoula.testdebit.info/";
    String fileName = SpeedTestUtils.generateFileName() + ".txt";
    //private static final String SPEED_TEST_SERVER_URI_UL = "ftp://speedtest.tele2.net/upload/" + fileName;
    //private static final String SPEED_TEST_SERVER_URI_UL = "http://192.168.10.104/perkasamobile/pol/file";

    /**
     * upload 10Mo file size.
     */
    private static final int FILE_SIZE = 10000000;

    private static final String TAG = "PING";

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
        psTest = (PointerSpeedometer) layout.findViewById(R.id.ps_test);
        btnProses = (Button) layout.findViewById(R.id.btn_proses);
        psTest.speedTo(0);

    }

    private void initEvent(){

        btnProses.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                maxLenth = 10;
                psTest.setMaxSpeed(maxLenth);
                psTest.speedTo(maxLenth);
                tvDownload.setText("Processing...");
                tvUpload.setText("");
                new getDownladTraffic().execute();
            }

        });
    }

    public class getDownladTraffic extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {

            // instantiate speed examples
            final SpeedTestSocket speedTestSocket = new SpeedTestSocket();

            //set timeout for download
            speedTestSocket.setSocketTimeout(SOCKET_TIMEOUT);

            // add a listener to wait for speed examples completion and progress
            speedTestSocket.addSpeedTestListener(new ISpeedTestListener() {

                @Override
                public void onCompletion(final SpeedTestReport report) {

                    //called when download/upload is complete
                    ((Activity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            psTest.setUnit(getUnit(report.getTransferRateBit()));
                            psTest.setMaxSpeed(getSpeed(report.getTransferRateBit()) + 10);
                            psTest.speedTo(getSpeed(report.getTransferRateBit()));

                            tvDownload.setText(iv.floatToString(getSpeed(report.getTransferRateBit())) +" "+ getUnit(report.getTransferRateBit()));
                            tvUpload.setText("");
                            maxLenth = 10;
                            psTest.speedTo(maxLenth);
                            mStartTX = 0;
                            new getUploadTraffic().execute();
                        }
                    });
                }

                @Override
                public void onError(final SpeedTestError speedTestError, final String errorMessage) {

                    Log.d(TAG, "onError: " + errorMessage);
                }

                @Override
                public void onProgress(final float percent, final SpeedTestReport downloadReport) {

                    ((Activity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            maxLenth = getSpeed(downloadReport.getTransferRateBit()) + 10;
                            psTest.setMaxSpeed(maxLenth);
                            psTest.setUnit(getUnit(downloadReport.getTransferRateBit()));
                            psTest.speedTo(getSpeed(downloadReport.getTransferRateBit()));

                            tvDownload.setText("Processing...");
                            tvUpload.setText("");
                        }
                    });
                }
            });

            speedTestSocket.startDownload(SPEED_TEST_SERVER_URI_DL);

            return null;
        }
    }

    private long mStartTX = 0;
    public class getUploadTraffic extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {

            // instantiate speed examples
            final SpeedTestSocket speedTestSocket = new SpeedTestSocket();

            //set timeout for download
            speedTestSocket.setSocketTimeout(SOCKET_TIMEOUT);

            //speedTestSocket.setUploadStorageType(UploadStorageType.FILE_STORAGE);

            // add a listener to wait for speed examples completion and progress
            speedTestSocket.addSpeedTestListener(new ISpeedTestListener() {

                @Override
                public void onCompletion(final SpeedTestReport report) {

                    ((Activity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            psTest.setUnit(getUnit(report.getTransferRateBit()));
                            psTest.setMaxSpeed(getSpeed(report.getTransferRateBit()) + 10);
                            psTest.speedTo(getSpeed(report.getTransferRateBit()));

                            tvUpload.setText(iv.floatToString(getSpeed(report.getTransferRateBit())) +" "+ getUnit(report.getTransferRateBit()));
                        }
                    });
                }

                @Override
                public void onError(final SpeedTestError speedTestError, final String errorMessage) {

                    Log.d(TAG, "onError: " + errorMessage);
                }

                @Override
                public void onProgress(final float percent, final SpeedTestReport downloadReport) {

                    ((Activity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            maxLenth = getSpeed(downloadReport.getTransferRateBit()) + 10;
                            psTest.setMaxSpeed(maxLenth);
                            psTest.setUnit(getUnit(downloadReport.getTransferRateBit()));
                            psTest.speedTo(getSpeed(downloadReport.getTransferRateBit()));

                            // Get running processes
                            /*long txBytes = TrafficStats.getTotalRxBytes() - mStartTX;
                            mStartTX = TrafficStats.getTotalRxBytes();
                            String uploadX = Long.toString(txBytes) + " bytes";
                            if (txBytes >= 1024) {
                                // KB or more
                                long txKb = txBytes / 1024;
                                uploadX = Long.toString(txKb) + " KBs";
                                if (txKb >= 1024) {
                                    // MB or more
                                    long txMB = txKb / 1024;
                                    uploadX = Long.toString(txMB) + " MBs";
                                    if (txMB >= 1024) {
                                        // GB or more
                                        long txGB = txMB / 1024;
                                        uploadX = Long.toString(txGB);
                                    }// rxMB>1024
                                }// rxKb > 1024
                            }// rxBytes>=1024

                            //Log.e("Total", "Bytes received " + android.net.TrafficStats.getMobileTxBytes());
                            Log.e("Total", "Bytes received " + uploadX);*/
                            tvUpload.setText("Processing...");
                        }
                    });
                }
            });

            speedTestSocket.startUpload(SPEED_TEST_SERVER_URI_UL, FILE_SIZE);

            return null;
        }
    }

    private static float getSpeed(BigDecimal size) {

        long sizeCeil = size.longValue();

        if (sizeCeil <= 0)
            return 0;

        final String[] units = new String[] { "b/s", "Kb/s", "Mb/s", "Gb/s", "Tb/s" };
        int digitGroups = (int) (Math.log10(sizeCeil) / Math.log10(1024));

        return (float) (sizeCeil / Math.pow(1024, digitGroups));
    }

    private static String getUnit(BigDecimal size) {

        long sizeCeil = size.longValue();

        if (sizeCeil <= 0)
            return "0";

        final String[] units = new String[] { "b/s", "Kb/s", "Mb/s", "Gb/s", "Tb/s" };
        int digitGroups = (int) (Math.log10(sizeCeil) / Math.log10(1024));

        return units[digitGroups];
    }


}
