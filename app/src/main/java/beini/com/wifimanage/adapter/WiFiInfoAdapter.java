package beini.com.wifimanage.adapter;

import android.net.wifi.ScanResult;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import beini.com.wifimanage.R;

/**
 * Created by beini on 2017/4/13.
 */

public class WiFiInfoAdapter extends BaseAdapter {

    List<ScanResult> scanResults;

    public WiFiInfoAdapter(BaseBean<ScanResult> baseBean) {
        super(baseBean);
        this.scanResults = baseBean.getBaseList();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        holder.itemView.setTag(position);
        ScanResult scanResult = scanResults.get(position);
        getTextView((ViewHolder) holder, R.id.text_wifi_name).setText(scanResult.SSID);
    }
}
