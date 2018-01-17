package beini.com.wifimanage.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import beini.com.wifimanage.R;

/**
 * Created by beini on 2018/1/17.
 */

public class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView txt;
    public ImageView img;
    public LinearLayout layout;

    public MyViewHolder(View itemView) {
        super(itemView);
        txt = (TextView) itemView.findViewById(R.id.item_recycler_txt);
        img = (ImageView) itemView.findViewById(R.id.item_delete_img);
        layout = (LinearLayout) itemView.findViewById(R.id.item_recycler_ll);
    }
}
