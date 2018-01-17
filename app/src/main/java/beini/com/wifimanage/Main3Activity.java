package beini.com.wifimanage;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class Main3Activity extends Activity {
    private Button btn_scroll;
    //private ScrollView scrollView;
    int i = 10;
    private LinearLayout linear_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        btn_scroll = findViewById(R.id.btn_scroll);
//        scrollView = findViewById(R.id.scroll_view);
        linear_layout = findViewById(R.id.linear_layout);
        btn_scroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linear_layout.scrollBy(-100, 0);
            }
        });
    }
}
