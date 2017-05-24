package learn.zhu.com.personalassistant.callback;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

/**
 * Created by zhu on 2017/4/20.
 */

public class RecyclerViewCallback extends ItemTouchHelper.Callback {

    public interface OnSwipedListener {
        void onSwiped(int position, int direction);
    }
    public interface OnSelectedListener {
        void onSelected(View view);
        void onUnSelected(View view);
    }
    private OnSwipedListener mOnSwipedListener;
    private OnSelectedListener mSelectedListener;

    public OnSwipedListener getOnSwipedListener() {
        return mOnSwipedListener;
    }

    public void setOnSwipedListener(OnSwipedListener onSwipedListener) {
        mOnSwipedListener = onSwipedListener;
    }

    public OnSelectedListener getSelectedListener() {
        return mSelectedListener;
    }

    public void setSelectedListener(OnSelectedListener selectedListener) {
        mSelectedListener = selectedListener;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if(actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            //选中
            if(mSelectedListener != null)
                mSelectedListener.onSelected(viewHolder.itemView);
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        //清除背景颜色
        if(mSelectedListener != null)
            mSelectedListener.onUnSelected(viewHolder.itemView);
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        //设置移动方式
        final int dragFlags;        //拖拽标志位
        final int swipeFlags;       //滑动标志位
        if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
            dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            swipeFlags = 0;
        } else {
            dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        }
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        //移动过程中会调用，进行数据更新
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        mOnSwipedListener.onSwiped(position, direction);
    }
}
