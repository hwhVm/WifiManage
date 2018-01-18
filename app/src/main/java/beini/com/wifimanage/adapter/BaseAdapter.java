package beini.com.wifimanage.adapter;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


/**
 * Created by beini on 2017/2/18.
 */

public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseAdapter.ViewHolder> {

    private List<T> baseList;
    private int layoutId;

    public BaseAdapter(BaseBean<T> baseBean) {
        this.baseList = baseBean.getBaseList();
        this.layoutId = baseBean.getId();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public int getItemCount() {
        if (baseList == null || baseList.size() == 0) {
            return 0;
        }
        return baseList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        View view;

        ViewHolder(View view) {
            super(view);
            this.view = view;
        }
    }

    protected TextView getTextView(@NonNull ViewHolder viewHolder, @IdRes int viewId) {
        return (TextView) viewHolder.view.findViewById(viewId);
    }

    protected ImageView getImageView(@NonNull ViewHolder viewHolder, @IdRes int viewId) {
        return (ImageView) viewHolder.view.findViewById(viewId);
    }

    protected Button getButton(@NonNull ViewHolder viewHolder, @IdRes int viewId) {
        return (Button) viewHolder.view.findViewById(viewId);
    }

    public void addItem(T bean, int postion) {
        baseList.add(postion, bean);
        notifyItemInserted(postion);
        notifyItemRangeChanged(postion, baseList.size());
    }

    public void removeItem(int postion) {
        baseList.remove(postion);
        notifyItemRemoved(postion);
        notifyItemRangeChanged(postion, baseList.size());
    }


}
