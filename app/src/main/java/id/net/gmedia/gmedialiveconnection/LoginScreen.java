package id.net.gmedia.gmedialiveconnection;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.maulana.custommodul.ApiVolley;
import com.maulana.custommodul.ItemValidation;
import com.maulana.custommodul.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import id.net.gmedia.gmedialiveconnection.NotificationUtils.InitFirebaseSetting;
import id.net.gmedia.gmedialiveconnection.utils.ServerUrl;

public class LoginScreen extends AppCompatActivity {

    private static boolean doubleBackToExitPressedOnce;
    private boolean exitState = false;
    private int timerClose = 2000;

    private EditText edtUsername, edtPassword;
    private Button btnLogin;
    private SessionManager session;
    private ItemValidation iv = new ItemValidation();
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        //Check close statement
        context = this;
        session = new SessionManager(context);
        doubleBackToExitPressedOnce = false;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            if (bundle.getBoolean("exit", false)) {
                exitState = true;
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        }

        InitFirebaseSetting.getFirebaseSetting(LoginScreen.this);

        InitFirebaseSetting.token = FirebaseInstanceId.getInstance().getToken();
        initUI();
        initEvent();
    }

    private void initUI() {

        edtUsername = (EditText) findViewById(R.id.edt_username);
        edtPassword = (EditText) findViewById(R.id.edt_password);
        btnLogin = (Button) findViewById(R.id.btn_login);

        /*if(session.isLoggedIn())*/ redirectToMain();
    }

    private void initEvent() {

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (edtUsername.getText().length() <= 0) {

                    Snackbar.make(findViewById(android.R.id.content), "ID Sirkuit tidak boleh kosong",
                            Snackbar.LENGTH_LONG).setAction("OK",
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                }
                            }).show();

                    edtUsername.requestFocus();
                    return;
                }

                if (edtPassword.getText().length() <= 0) {

                    Snackbar.make(findViewById(android.R.id.content), "Password tidak boleh kosong",
                            Snackbar.LENGTH_LONG).setAction("OK",
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                }
                            }).show();

                    edtPassword.requestFocus();

                    return;
                }

                iv.hideSoftKey(context);
                login();
            }
        });
    }

    private void login() {

        final ProgressDialog progressDialog = new ProgressDialog(LoginScreen.this, R.style.AppTheme_Login_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        JSONObject jBody = new JSONObject();
        try {
            jBody.put("id_sirkuit", edtUsername.getText().toString());
            jBody.put("password", edtPassword.getText().toString());
            jBody.put("fcm_id", InitFirebaseSetting.token);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApiVolley request = new ApiVolley(LoginScreen.this, jBody, "POST", ServerUrl.auth, -1, new ApiVolley.VolleyCallback() {
            @Override
            public void onSuccess(String result) {

                String message = "Terjadi kesalahan saat memuat data, harap ulangi";
                if(progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();

                try {

                    JSONObject response = new JSONObject(result);
                    String status = response.getJSONObject("metadata").getString("status");
                    message = response.getJSONObject("metadata").getString("message");
                    if(iv.parseNullInteger(status) == 200){

                        String idSite = response.getJSONObject("response").getString("id_site");
                        String customerName = response.getJSONObject("response").getString("customer_name");
                        String siteName = response.getJSONObject("response").getString("site_name");
                        String customerId = response.getJSONObject("response").getString("customer_id");
                        String serviceId = response.getJSONObject("response").getString("service_id");
                        session.createLoginSession(edtUsername.getText().toString()
                                ,idSite
                                ,customerName
                                ,siteName
                                ,customerId
                                ,serviceId);
                        Toast.makeText(LoginScreen.this, message, Toast.LENGTH_SHORT).show();

                        redirectToMain();
                    }else{

                        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                    Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
                }

            }

            @Override
            public void onError(String result) {
                Snackbar.make(findViewById(android.R.id.content), "Terjadi kesalahan koneksi, harap ulangi kembali nanti", Snackbar.LENGTH_LONG).show();
                if(progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();
            }
        });
    }

    private void redirectToMain() {

        Intent intent = new Intent(context, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {

        // Origin backstage
        if (doubleBackToExitPressedOnce) {
            Intent intent = new Intent(LoginScreen.this, LoginScreen.class);
            intent.putExtra("exit", true);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            //System.exit(0);
        }

        if(!exitState && !doubleBackToExitPressedOnce){
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, getResources().getString(R.string.app_exit), Toast.LENGTH_SHORT).show();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, timerClose);
    }
}
