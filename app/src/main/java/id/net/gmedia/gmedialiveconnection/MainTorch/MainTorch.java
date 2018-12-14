package id.net.gmedia.gmedialiveconnection.MainTorch;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.maulana.custommodul.CustomItem;

import java.util.ArrayList;
import java.util.List;

import id.net.gmedia.gmedialiveconnection.MainTorch.Adapter.ListTorchAdapter;
import id.net.gmedia.gmedialiveconnection.R;

public class MainTorch extends Fragment {

    private Context context;
    private View layout;
    private ListView lvTorch;
    private Button btnProses, btnStop;
    private List<CustomItem> listTorch = new ArrayList<>();
    private ListTorchAdapter adapter;

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

        initUI();

        return layout;
    }

    private void initUI() {

        lvTorch = (ListView) layout.findViewById(R.id.lv_torch);
        btnProses = (Button) layout.findViewById(R.id.btn_proses);
        btnStop = (Button) layout.findViewById(R.id.btn_stop);

        adapter = new ListTorchAdapter((Activity) context, listTorch);
        lvTorch.setAdapter(adapter);

        listTorch.add(new CustomItem(
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

        adapter.notifyDataSetChanged();
    }
}
