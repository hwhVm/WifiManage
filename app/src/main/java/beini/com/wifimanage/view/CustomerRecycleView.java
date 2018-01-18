package beini.com.wifimanage.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Scroller;

import beini.com.wifimanage.R;
import beini.com.wifimanage.adapter.WiFiInfoAdapter;
import beini.com.wifimanage.util.BLog;

/**
 * Create by beini 2018/1/18
 */
public class CustomerRecycleView extends RecyclerView implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

    private int maxLength;
    private int mStartX = 0;
    private LinearLayout itemLayout;
    private int pos;
    private Rect mTouchFrame;
    private int xDown, xMove, yDown, yMove, mTouchSlop, xUp, yUp;
    private Scroller mScroller;
    private ImageView imageView;
    private boolean isFirst = true;
    private GestureDetector gestureDetector;

    public CustomerRecycleView(Context context) {
        this(context, null);
    }

    public CustomerRecycleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomerRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        //滑动到最小距离
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        Log.e("com.beini", "滑动到最小距离 mTouchSlop=" + mTouchSlop);
        //滑动的最大距离
        maxLength = ((int) (130 * context.getResources().getDisplayMetrics().density + 0.5f));
        Log.e("com.beini", "滑动的最大距离 =" + maxLength);
        //初始化Scroller
        mScroller = new Scroller(context, new LinearInterpolator(context, null));
        //利用系统提供的手势监听实现单击，长按等
        gestureDetector = new GestureDetector(context, this);
        gestureDetector.setOnDoubleTapListener(this);
    }

    private boolean isTrue = false;

    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                xDown = x;
                yDown = y;
                View view = getView(x, y);
                if (view != null) {
                    ViewHolder viewHolder = getChildViewHolder(view);
                    itemLayout = viewHolder.itemView.findViewById(R.id.item_recycler_ll);
                    imageView = itemLayout.findViewById(R.id.item_delete_img);
                    BLog.e(" 11 imageView =" + imageView.hashCode());
                    isTrue = true;
                } else {
                    isTrue = false;
                }
            }
            break;
            case MotionEvent.ACTION_MOVE: {
                xMove = x;
                yMove = y;
                int dx = xMove - xDown;
                int dy = yMove - yDown;
                if (Math.abs(dy) < mTouchSlop * 2 && Math.abs(dx) > mTouchSlop) {
                    if (isTrue) {
                        int scrollX = itemLayout.getScrollX();
                        int newScrollX = mStartX - x;
                        if (newScrollX < 0) {
                            itemLayout.scrollBy(newScrollX, 0);
                        } else {
                            if (scrollX < 0) {
                                newScrollX = 0;
                            } else if (scrollX >= maxLength) {
                                newScrollX = 0;
                            } else if (newScrollX >= (maxLength - scrollX)) {
                                newScrollX = maxLength - scrollX;
                            }

                            if (scrollX > maxLength * 2 / 3) {
                                BLog.e(" 22 imageView =" + imageView.hashCode());
                                imageView.setVisibility(VISIBLE);
                                if (isFirst) {
                                    setAnimator();
                                    isFirst = false;
                                }
                                itemLayout.scrollBy(maxLength - scrollX, 0);
                            } else {
                                itemLayout.scrollBy(newScrollX, 0);
                            }

                        }
                    }
                }
            }
            break;
            case MotionEvent.ACTION_UP: {
                xUp = x;
                yUp = y;
                int dx = xUp - xDown;
                int dy = yUp - yDown;
                if (Math.abs(dy) < mTouchSlop && Math.abs(dx) < mTouchSlop) {
                } else {
                    if (isTrue) {
                        int scrollX = itemLayout.getScrollX();
                        if (scrollX > maxLength * 2 / 3) {
                            ((WiFiInfoAdapter) getAdapter()).removeItem(pos);
                        } else {
                            mScroller.startScroll(scrollX, 0, -scrollX, 0);
                            invalidate();
                        }
                        isFirst = true;
                    }
                }
            }
            break;
        }
        mStartX = x;
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    private void setAnimator() {
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(imageView, "scaleX", 1f, 1.2f, 1f);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(imageView, "scaleY", 1f, 1.2f, 1f);
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(animatorX).with(animatorY);
        animSet.setDuration(800);
        animSet.start();
    }

    private View getView(int x, int y) {
        //通过点击的坐标计算当前的position
        int mFirstPosition = ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
        Rect frame = mTouchFrame;
        if (frame == null) {
            mTouchFrame = new Rect();
            frame = mTouchFrame;
        }
        int count = getChildCount();
        for (int i = count - 1; i >= 0; i--) {
            final View child = getChildAt(i);
            if (child.getVisibility() == View.VISIBLE) {
                child.getHitRect(frame);
                if (frame.contains(x, y)) {
                    pos = mFirstPosition + i;
                }
            }
        }
        BLog.e("pos=" + pos);
        //通过position得到item的viewHolder
        View view = getChildAt(pos - mFirstPosition);
        return view;
    }

    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            itemLayout.scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
    }

    //事件监听
    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        int x = (int) e.getX();
        int y = (int) e.getY();
        View view = getView(x, y);
        itemClickListener.onItemClick(view, (Integer) view.getTag());
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        int x = (int) e.getX();
        int y = (int) e.getY();
        View view = getView(x, y);
        itemLongClickListener.onItemLongClick(view, (Integer) view.getTag());
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager instanceof GridLayoutManager) {// GridLayoutManager
                // flag==0，相当于这个功能被关闭
                int dragFlag = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                int swipeFlag = 0;
                // create make
                return makeMovementFlags(dragFlag, swipeFlag);
            } else if (layoutManager instanceof LinearLayoutManager) {// linearLayoutManager
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
                int orientation = linearLayoutManager.getOrientation();

                int dragFlag = 0;
                int swipeFlag = 0;

                if (orientation == LinearLayoutManager.HORIZONTAL) {// 横向的布局
//                        swipeFlag = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                    dragFlag = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                } else if (orientation == LinearLayoutManager.VERTICAL) {// 竖向的布局
                    dragFlag = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
//                        swipeFlag = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                }
                return makeMovementFlags(dragFlag, swipeFlag);
            }

            return 0;
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {

            itemHleperListenter.onMove(recyclerView, viewHolder, target);
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            BLog.e("------------>onSwiped");
        }

        @Override
        public boolean isLongPressDragEnabled() {//是否可以出发长按拖动事件
            return super.isLongPressDragEnabled();
        }

        @Override
        public boolean isItemViewSwipeEnabled() {//是否可以触发滑动事件
            return super.isItemViewSwipeEnabled();
        }


    });
    //
    private ItemHleperListenter itemHleperListenter = null;

    public void setListenter(ItemHleperListenter itemHleperListenter) {
        itemTouchHelper.attachToRecyclerView(this);
        this.itemHleperListenter = itemHleperListenter;
    }

    public interface ItemHleperListenter {
        boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target);

        void onSwiped(RecyclerView.ViewHolder viewHolder, int direction);

    }

    //item onClick 事件
    private OnItemClickListener itemClickListener = null;

    public void setItemClick(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    // item onlongClick 事件
    private onItemLongClickListener itemLongClickListener = null;

    public void setOnItemLongClickListener(onItemLongClickListener onItemLongClickListener) {
        this.itemLongClickListener = onItemLongClickListener;
    }

    public interface onItemLongClickListener {
        void onItemLongClick(View view, int position);
    }

}
