package id.net.gmedia.gmedialiveconnection.MainTicket;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import id.net.gmedia.gmedialiveconnection.MainTicket.Fragment.MainBOD;
import id.net.gmedia.gmedialiveconnection.MainTicket.Fragment.MainComplaint;
import id.net.gmedia.gmedialiveconnection.MainTicket.Fragment.MainRequest;
import id.net.gmedia.gmedialiveconnection.R;

public class MainTicket extends AppCompatActivity {

    private Context context;
    private TextView tvTitle;
    private ImageView ivMenu;
    private RadioGroup rbTab;
    private RadioButton rbTab1, rbTab2, rbTab3;
    private static int stateFragment = -1;
    private int state = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_ticket);

        context = this;

        initUI();
        initEvent();
    }

    private void initUI() {

        tvTitle = (TextView) findViewById(R.id.tv_title);
        ivMenu = (ImageView) findViewById(R.id.iv_menu);
        rbTab = (RadioGroup) findViewById(R.id.rg_tab);
        rbTab1 = (RadioButton) findViewById(R.id.rb_tab1);
        rbTab2 = (RadioButton) findViewById(R.id.rb_tab2);
        rbTab3 = (RadioButton) findViewById(R.id.rb_tab3);

        tvTitle.setText("Ticket");
    }

    private void initEvent() {

        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        rbTab.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                int checkedID = radioGroup.getCheckedRadioButtonId();
                if(checkedID == rbTab1.getId()){

                    ChangeFragment(0);
                }else if(checkedID== rbTab2.getId()){

                    ChangeFragment(1);
                }else if(checkedID == rbTab3.getId()){

                    ChangeFragment(2);
                }
            }
        });

        if(stateFragment == -1){

            ChangeFragment(1);
            ChangeFragment(0);

            rbTab1.setChecked(true);
        }
    }

    @Override
    protected void onDestroy() {


        super.onDestroy();
    }

    private void ChangeFragment(int stateChecked){

        stateFragment = stateChecked;

        switch (stateChecked){
            case 0:
                //setTitle("Live Chart");
                fragment = new MainComplaint();
                break;
            case 1:

                //fragment = new MainPing();
                fragment = new MainRequest();
                break;
            case 2:
                //setTitle("Ping");
                fragment = new MainBOD();
                break;
            default:
                //setTitle("Live Chart");
                fragment = new MainComplaint();
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
    public void onBackPressed() {
        //super.onBackPressed();

        if(state != 0){

            rbTab1.setChecked(true);
        }else{

            stateFragment = -1;
            finish();
            overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
        }
    }
}
