package id.net.gmedia.gmedialiveconnection;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Trace;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
                case R.id.navigation_home:
                    ChangeFragment(0);
                    return true;
                case R.id.navigation_dashboard:
                    ChangeFragment(1);
                    return true;
                case R.id.navigation_notifications:
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

    private void ChangeFragment(int stateChecked){

        switch (stateChecked){
            case 0:
                setTitle("Live Chart");
                fragment = new MainLiveChart();
                break;
            case 1:
                setTitle("Torch");
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
}
