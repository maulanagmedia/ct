package id.net.gmedia.gmedialiveconnection;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Trace;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.maulana.custommodul.SessionManager;

import id.net.gmedia.gmedialiveconnection.MainLiveCart.MainLiveChart;
import id.net.gmedia.gmedialiveconnection.MainPing.MainPing;
import id.net.gmedia.gmedialiveconnection.MainTorch.MainTorch;
import id.net.gmedia.gmedialiveconnection.NotificationUtils.InitFirebaseSetting;

public class MainActivity extends AppCompatActivity {

    private Context context;
    private int state = 0;
    private SessionManager session;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_live_chart:
                    ChangeFragment(0);
                    return true;
                case R.id.menu_torch:
                    ChangeFragment(1);
                    return true;
                case R.id.menu_ping:
                    ChangeFragment(2);
                    return true;
            }
            return false;
        }
    };
    private BottomNavigationView buttonNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InitFirebaseSetting.getFirebaseSetting(context);
        InitFirebaseSetting.token = FirebaseInstanceId.getInstance().getToken();
        context = this;
        session = new SessionManager(context);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            if (bundle.getBoolean("exit", false)) {

                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        }

        initUI();
    }

    private void initUI() {

        buttonNavigation = (BottomNavigationView) findViewById(R.id.navigation);
        buttonNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        ChangeFragment(1);
        ChangeFragment(0);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {

        if(MainLiveChart.isActive) MainLiveChart.disconnectLive();
        if(MainTorch.isActive) MainTorch.disconnectLive();
        super.onDestroy();
    }

    private void ChangeFragment(int stateChecked){

        if(MainLiveChart.isActive) MainLiveChart.disconnectLive();
        if(MainTorch.isActive) MainTorch.disconnectLive();

        switch (stateChecked){
            case 0:
                setTitle("Live Chart");
                fragment = new MainLiveChart();
                break;
            case 1:
                setTitle(context.getResources().getString(R.string.title_dashboard));
                fragment = new MainTorch();
                break;
            case 2:
                setTitle("Ping");
                fragment = new MainPing();
                break;
            default:
                setTitle("Live Chart");
                fragment = new MainLiveChart();
                break;
        }

        if(stateChecked > state){

            callFragment(context, fragment);
        }else if (stateChecked < state){
            callFragmentBack(context, fragment);
        }

        state = stateChecked;
    }

    private static Fragment fragment;
    private static void callFragment(Context context, Fragment fragment) {
        ((AppCompatActivity)context).getSupportFragmentManager()
                .beginTransaction()
                //.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up)
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right)
                .replace(R.id.fl_container, fragment, fragment.getClass().getSimpleName())
                .addToBackStack(null)
                .commit();
    }

    private static void callFragmentBack(Context context, Fragment fragment) {
        ((AppCompatActivity)context).getSupportFragmentManager()
                .beginTransaction()
                //.setCustomAnimations(R.anim.slide_in_down, R.anim.slide_out_down)
                .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left)
                .replace(R.id.fl_container, fragment, fragment.getClass().getSimpleName())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.menu_logout){

            AlertDialog dialog = new AlertDialog.Builder(context)
                    .setTitle("Konfirmasi")
                    .setMessage("Apakah anda yakin ingin logout")
                    .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            Intent intent = new Intent(context, LoginScreen.class);
                            session.logoutUser(intent);
                        }
                    })
                    .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })
                    .show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        if(state != 0){

            buttonNavigation.getMenu().findItem(R.id.menu_live_chart).setChecked(true);
            ChangeFragment(0);
        }else{

            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
            LayoutInflater inflater = (LayoutInflater) ((Activity)context).getSystemService(LAYOUT_INFLATER_SERVICE);
            View viewDialog = inflater.inflate(R.layout.layout_exit_dialog, null);
            builder.setView(viewDialog);
            builder.setCancelable(false);

            final Button btnYa = (Button) viewDialog.findViewById(R.id.btn_ya);
            final Button btnTidak = (Button) viewDialog.findViewById(R.id.btn_tidak);

            final AlertDialog alert = builder.create();
            alert.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            btnYa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view2) {

                    if(alert != null) alert.dismiss();

                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    intent.putExtra("exit", true);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            });

            btnTidak.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view2) {

                    if(alert != null) alert.dismiss();
                }
            });

            alert.show();
        }
    }
}
