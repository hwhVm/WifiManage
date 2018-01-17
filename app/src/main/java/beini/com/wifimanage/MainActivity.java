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

import java.util.List;

import beini.com.wifimanage.util.BLog;
import beini.com.wifimanage.adapter.BaseAdapter;
import beini.com.wifimanage.adapter.BaseBean;
import beini.com.wifimanage.util.GetWifiInfoUtil;
import beini.com.wifimanage.adapter.WiFiInfoAdapter;
import beini.com.wifimanage.util.WifiTool;

public class MainActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView recyclerView;
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


    }

    public void getAllNetWorkList() {
        wifiTool.openWifi();
        wifiTool.startScan();
        scanResults = wifiTool.getWifiList();
        if (scanResults != null && scanResults.size() > 0) {
            if (isFirstLoad) {
                wifiTool.getConfiguration();

                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                wifiInfoAdapter = new WiFiInfoAdapter(new BaseBean<>(R.layout.item_wifi_list, scanResults));
                recyclerView.setAdapter(wifiInfoAdapter);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                wifiInfoAdapter.setItemClick(onItemClickListener);
                isFirstLoad = false;

            } else {
                wifiTool.getConfiguration();
                wifiInfoAdapter.notifyDataSetChanged();
            }
            swip_refresh.setRefreshing(false);
        }
    }

    WiFiInfoAdapter.OnItemClickListener onItemClickListener = new BaseAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            ScanResult scanResult = scanResults.get(position);
            String strResult = connectWifi("divoomzhongke508", scanResult);
            BLog.d("strResult=" + strResult + "   SSID==" + scanResult.SSID);
        }
    };

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
        getAllNetWorkList();
    }
}
