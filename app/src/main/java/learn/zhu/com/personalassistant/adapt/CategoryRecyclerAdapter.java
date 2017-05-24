package learn.zhu.com.personalassistant.adapt;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import learn.zhu.com.personalassistant.R;
import learn.zhu.com.personalassistant.model.Category;

/**
 * Created by zhu on 2017/4/19.
 */

public class CategoryRecyclerAdapter extends RecyclerView.Adapter<CategoryRecyclerAdapter.CategoryViewHolder> {

    private List<Category> mCategories;
    private Context mContext;

    public CategoryRecyclerAdapter(Context context, List<Category> categories) {
        mCategories = categories;
        mContext = context;
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {

        TextView categoryName;
        TextView categoryintroductions;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            categoryName = (TextView)itemView.findViewById(R.id.category_name);
            categoryintroductions = (TextView)itemView.findViewById(R.id.category_introductions);
        }
    }

    @Override
    public CategoryRecyclerAdapter.CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CategoryViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_category, parent, false));
    }

    @Override
    public void onBindViewHolder(CategoryRecyclerAdapter.CategoryViewHolder holder, int position) {
        holder.categoryName.setText(mCategories.get(position).getName());
        holder.categoryintroductions.setText(mCategories.get(position).getIntroductions());
    }

    @Override
    public int getItemCount() {
        return mCategories.size();
    }
}
