package beini.com.wifimanage.view;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import beini.com.wifimanage.util.BLog;

/**
 * Created by beini on 2018/1/17.
 */

public class ScrollView extends View {
    public ScrollView(Context context) {
        super(context);
    }

    public ScrollView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    //scrollTo、scrollBy、getScrollX、getScrollY这4个方法的含义。
    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
    }

    @Override
    public void scrollBy(int x, int y) {
        super.scrollBy(x, y);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        BLog.e(" onScrollChanged   " + l + "           t=" + t + "  old1=" + oldl + "  oldt=" + oldt);
        super.onScrollChanged(l, t, oldl, oldt);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
    }
}
