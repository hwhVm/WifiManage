package beini.com.wifimanage;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import beini.com.wifimanage.adapter.RecycleAdapter;
import beini.com.wifimanage.view.MyRecycleView;

public class Main2Activity extends Activity {
    private MyRecycleView my_recycle_view;
    private RecycleAdapter recycleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        List<Integer> integerList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            integerList.add(i);
        }
        recycleAdapter = new RecycleAdapter(this, integerList);
        my_recycle_view = findViewById(R.id.my_recycle_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        my_recycle_view.setLayoutManager(layoutManager);
        my_recycle_view.setAdapter(recycleAdapter);
    }
}
