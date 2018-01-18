package beini.com.wifimanage;

import android.app.Activity;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import beini.com.wifimanage.adapter.BaseBean;
import beini.com.wifimanage.adapter.WiFiInfoAdapter;
import beini.com.wifimanage.util.BLog;
import beini.com.wifimanage.util.GetWifiInfoUtil;
import beini.com.wifimanage.util.WifiTool;
import beini.com.wifimanage.view.CustomerRecycleView;

public class MainActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener {
    private CustomerRecycleView recyclerView;
    private TextView text_wifi_setting_info;
    private SwipeRefreshLayout swip_refresh;
    private WifiTool wifiTool;
    private List<ScanResult> scanResults;
    private WiFiInfoAdapter wifiInfoAdapter;
    private boolean isFirstLoad = true;
    private String WIFI_CONNECT_SUCCESS = "WIFI_CONNECT_SUCCESS";
    private String WIFI_CONNECT_ERROR = "WIFI_CONNECT_ERROR";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycle_wifi);
        text_wifi_setting_info = findViewById(R.id.text_wifi_setting_info);
        swip_refresh = findViewById(R.id.swip_refresh);
        swip_refresh.setOnRefreshListener(this);
        text_wifi_setting_info.setText(GetWifiInfoUtil.getWifiInfo(this));
        wifiTool = new WifiTool(this);
        scanResults = new ArrayList<>();
        scanResults.add(null);
        scanResults.add(null);
        scanResults.add(null);
        scanResults.add(null);
        scanResults.add(null);
        scanResults.add(null);
        scanResults.add(null);
        scanResults.add(null);
        scanResults.add(null);
        scanResults.add(null);
        scanResults.add(null);
        scanResults.add(null);
        scanResults.add(null);
        scanResults.add(null);
        initList(scanResults);
    }

    public void getAllNetWorkList() {
        wifiTool.openWifi();
        wifiTool.startScan();
        scanResults = wifiTool.getWifiList();
        if (scanResults != null && scanResults.size() > 0) {
            if (isFirstLoad) {
                wifiTool.getConfiguration();
                wifiInfoAdapter.update(scanResults);
                isFirstLoad = false;
            } else {
                wifiTool.getConfiguration();
                wifiInfoAdapter.notifyDataSetChanged();
            }
        }
        swip_refresh.setRefreshing(false);
    }

    private void initList(final List<ScanResult> scanResults) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        wifiInfoAdapter = new WiFiInfoAdapter(new BaseBean<>(R.layout.item_recycle, scanResults));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setListenter(new CustomerRecycleView.ItemHleperListenter() {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                Collections.swap(scanResults, viewHolder.getAdapterPosition(), target.getAdapterPosition());
                wifiInfoAdapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

            }
        });
        recyclerView.setItemClick(new CustomerRecycleView.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                BLog.e("--onItemClick------>position=" + position);
//               ScanResult scanResult = scanResults.get(position);
//            String strResult = connectWifi("divoomzhongke508", scanResult);
//            BLog.d("strResult=" + strResult + "   SSID==" + scanResult.SSID);
            }
        });
        recyclerView.setOnItemLongClickListener(new CustomerRecycleView.onItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                BLog.e("---onItemLongClick----->position=" + position);
            }
        });
        recyclerView.setAdapter(wifiInfoAdapter);
    }


    // /data/misc/wifi/wpa_supplicant.conf
    public String connectWifi(String wifiPassword, ScanResult scanResult) {
        String wifiItemSSID = scanResult.SSID;
        int wifiItemId = wifiTool.IsConfiguration("\""
                + scanResult.SSID + "\"");
        if (wifiItemId != -1) {
            boolean isConnectSuccess = wifiTool.ConnectWifi(wifiItemId);// 连接已保存密码的WiFi
            BLog.d("--连接已保存密码的WiFi------->isConnectSuccess=" + isConnectSuccess);
            if (isConnectSuccess) {
                return WIFI_CONNECT_SUCCESS;
            }
        } else { // 没有配置好信息，配置
            if (TextUtils.isEmpty(wifiPassword)) {
                return WIFI_CONNECT_ERROR;
            }
            int netId = wifiTool.AddWifiConfig(scanResults, wifiItemSSID, wifiPassword);
            BLog.d("netId=" + netId);
            if (netId == -1) {
                return WIFI_CONNECT_ERROR;
            }
            wifiTool.getConfiguration();// 添加了配置信息，要重新得到配置信息
            boolean isConnectSuccess = wifiTool.ConnectWifi(netId);
            BLog.d("没有配置好信息------->isConnectSuccess=" + isConnectSuccess);
            if (isConnectSuccess) {
                return WIFI_CONNECT_SUCCESS;
            }
        }

        return WIFI_CONNECT_ERROR;
    }

    @Override
    public void onRefresh() {
        BLog.d("onRefresh()");
//        getAllNetWorkList();
        swip_refresh.setRefreshing(false);

    }
}
