package id.net.gmedia.gmedialiveconnection.MainTroubleAnalytic.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.maulana.custommodul.ItemValidation;

import id.net.gmedia.gmedialiveconnection.R;

public class MainRouterStatus extends Fragment {

    private Context context;
    private View layout;
    private ItemValidation iv = new ItemValidation();

    public MainRouterStatus() {
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
        layout = inflater.inflate(R.layout.fragment_main_router_status, container, false);
        context = getContext();

        initUI();
        initEvent();

        return layout;
    }

    private void initUI() {

    }

    private void initEvent() {

    }

}
