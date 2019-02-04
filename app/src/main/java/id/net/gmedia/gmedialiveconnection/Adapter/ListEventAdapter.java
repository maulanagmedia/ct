package id.net.gmedia.gmedialiveconnection.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.maulana.custommodul.CustomItem;
import com.maulana.custommodul.ImageUtils;
import com.maulana.custommodul.ItemValidation;

import java.util.List;

import id.net.gmedia.gmedialiveconnection.R;

/**
 * Created by Shin on 3/1/2017.
 */

public class ListEventAdapter extends RecyclerView.Adapter<ListEventAdapter.MyViewHolder> {

    private Context context;
    private List<CustomItem> masterList;
    private ItemValidation iv = new ItemValidation();
    private int menuWidth;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CardView cvContainer;
        public ImageView ivIcon;

        public MyViewHolder(View view) {
            super(view);
            cvContainer = (CardView) view.findViewById(R.id.cv_container);
            ivIcon = (ImageView) view.findViewById(R.id.iv_thumbnail);
        }
    }

    public ListEventAdapter(Context context, List<CustomItem> masterList, int menuWidth){
        this.context = context;
        this.masterList = masterList;
        this.menuWidth = menuWidth;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cv_event, parent, false);

        return new MyViewHolder(itemView);
    }

    public void AddListItemAdapter(List items){
        this.masterList.addAll(items);
        this.notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final CustomItem promo = masterList.get(position);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(menuWidth,menuWidth);
        holder.cvContainer.setLayoutParams(lp);
        // loading image using Picasso library
        ImageUtils iu = new ImageUtils();
        iu.LoadPromoImage(context, promo.getItem2(), holder.ivIcon);
        /*holder.cvContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailMerchant.class);
                intent.putExtra("id", promo.getItem1());
                context.startActivity(intent);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return masterList.size();
    }

}
