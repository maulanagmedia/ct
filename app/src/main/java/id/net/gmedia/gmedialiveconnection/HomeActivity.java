package id.net.gmedia.gmedialiveconnection;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.maulana.custommodul.CustomItem;
import com.maulana.custommodul.SessionManager;

import java.util.ArrayList;
import java.util.List;

import id.net.gmedia.gmedialiveconnection.Adapter.ListEventAdapter;
import id.net.gmedia.gmedialiveconnection.MainCoverage.MainCoverage;
import id.net.gmedia.gmedialiveconnection.MainInternetUsage.MainInternetUsage;
import id.net.gmedia.gmedialiveconnection.MainTicket.MainTicket;
import id.net.gmedia.gmedialiveconnection.MainTroubleAnalytic.MainTroubleAnalytic;
import id.net.gmedia.gmedialiveconnection.NotificationUtils.InitFirebaseSetting;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Context context;
    private int state = 0;
    private SessionManager session;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private ImageView ivMenu;
    private TextView tvTitle;
    private RecyclerView rvEvent;
    private List<CustomItem> eventList = new ArrayList<>();
    private ListEventAdapter adapter;
    private LinearLayout llInternetUsage, llTicket, llTrouble, llCourage;
    private LinearLayout llWBilling, llWInternet, llWTicket, llWTrouble, llWCoverage, llWLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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
        initEvent();
        initData();
    }

    private void initUI() {

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ivMenu = (ImageView) findViewById(R.id.iv_menu);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        rvEvent = (RecyclerView) findViewById(R.id.rv_event);

        llInternetUsage = (LinearLayout) findViewById(R.id.ll_interne_usage);
        llTicket = (LinearLayout) findViewById(R.id.ll_ticket);
        llTrouble = (LinearLayout) findViewById(R.id.ll_trouble);
        llCourage = (LinearLayout) findViewById(R.id.ll_courage);

        llWBilling = (LinearLayout) navigationView.findViewById(R.id.ll_w_billing);
        llWInternet = (LinearLayout) navigationView.findViewById(R.id.ll_w_internet);
        llWTicket = (LinearLayout) navigationView.findViewById(R.id.ll_w_ticket);
        llWTrouble = (LinearLayout) navigationView.findViewById(R.id.ll_w_trouble);
        llWCoverage = (LinearLayout) navigationView.findViewById(R.id.ll_w_couverage);
        llWLogout = (LinearLayout) navigationView.findViewById(R.id.ll_w_logout);

        tvTitle.setText(session.getCustomerName());
    }

    private void initEvent() {

        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!drawer.isDrawerOpen(GravityCompat.START)) {

                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });

        llInternetUsage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, MainInternetUsage.class);
                startActivity(intent);
            }
        });

        llTrouble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, MainTroubleAnalytic.class);
                startActivity(intent);
            }
        });

        llTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, MainTicket.class);
                startActivity(intent);
            }
        });

        llCourage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, MainCoverage.class);
                startActivity(intent);
            }
        });

        llWInternet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, MainInternetUsage.class);
                startActivity(intent);
            }
        });

        llWTrouble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, MainTroubleAnalytic.class);
                startActivity(intent);
            }
        });

        llWTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, MainTicket.class);
                startActivity(intent);
            }
        });

        llWCoverage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, MainCoverage.class);
                startActivity(intent);
            }
        });

        llWLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
            }
        });

    }

    private void initData() {

        eventList.clear();
        eventList.add(new CustomItem("1", "https://erpsmg.gmedia.id/client_tools/images/1.jpg"));
        eventList.add(new CustomItem("2", "https://erpsmg.gmedia.id/client_tools/images/2.jpg"));
        eventList.add(new CustomItem("3", "https://erpsmg.gmedia.id/client_tools/images/3.jpg"));

        setEventAdapter();
    }

    private void setEventAdapter(){

        rvEvent.setAdapter(null);
        Display display = ((Activity)context).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        try {
            // this is why the minimal sdk must be JB
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                display.getRealSize(size);
            }else {
                display.getSize(size);
            }
        } catch (NoSuchMethodError err) {
            display.getSize(size);
        }

        int menuWidth = 0;
        //double menuFloat = 0.145 * (size.y - 60); // 60 from activity_home.xml
        double menuFloat = 0.16 * (size.y); // seperlima dari tinggi
        menuWidth = (int) menuFloat;

        adapter = new ListEventAdapter(context, eventList, menuWidth);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        rvEvent.setLayoutManager(mLayoutManager);
        rvEvent.setItemAnimator(new DefaultItemAnimator());
        rvEvent.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        if(state != 0){

            /*buttonNavigation.getMenu().findItem(R.id.menu_live_chart).setChecked(true);
            ChangeFragment(0);*/
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

                    Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }
}
