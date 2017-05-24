package learn.zhu.com.personalassistant.adapt;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import learn.zhu.com.personalassistant.R;
import learn.zhu.com.personalassistant.model.Category;
import learn.zhu.com.personalassistant.model.Data;
import learn.zhu.com.personalassistant.model.PersonalAssistantDB;
import learn.zhu.com.personalassistant.model.Type;

/**
 * Created by zhu on 2017/4/19.
 */

public class DataRecyclerAdapter extends RecyclerView.Adapter<DataRecyclerAdapter.DataViewHolder> {

    private List<Data> mDatas;
    private Context mContext;
    private PersonalAssistantDB mPersonalAssistantDB;

    public DataRecyclerAdapter(Context context, List<Data> data, PersonalAssistantDB personalAssistantDB) {
        mDatas = data;
        mContext = context;
        mPersonalAssistantDB = personalAssistantDB;
    }

    class DataViewHolder extends RecyclerView.ViewHolder {

        TextView date;
        TextView categoryintroductions;
        TextView money;

        public DataViewHolder(View itemView) {
            super(itemView);
            date = (TextView)itemView.findViewById(R.id.date);
            categoryintroductions = (TextView)itemView.findViewById(R.id.category_introductions);
            money = (TextView)itemView.findViewById(R.id.money);
        }
    }

    @Override
    public DataRecyclerAdapter.DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DataViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_data, parent, false));
    }

    @Override
    public void onBindViewHolder(DataRecyclerAdapter.DataViewHolder holder, int position) {
        Data data = mDatas.get(position);
        holder.date.setText(data.getDate());
        float money = data.getMoney();
        Category category = mPersonalAssistantDB.getCategory(data.getCategoryId());
        holder.categoryintroductions.setText(category.getName());
        if(category.getTypeId() == Type.SR) {
            holder.money.setTextColor(Color.GREEN);
            holder.money.setText("+" + String.valueOf(money));
        }
        else {
            holder.money.setTextColor(Color.RED);
            holder.money.setText("-" + String.valueOf(money));
        }

    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }
}
