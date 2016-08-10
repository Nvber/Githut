package com.feicui.zh.ganh;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.feicui.zh.R;
import com.feicui.zh.ganh.model.GanHItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/8/5.
 */
public class GanHAdapter extends BaseAdapter {

    private final ArrayList<GanHItem> datas;

    public GanHAdapter() {
        datas = new ArrayList<GanHItem>();
    }

    public void setDatas(List<GanHItem> ganHItems) {
        datas.clear();
        datas.addAll(ganHItems);
    }


    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public GanHItem getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            convertView = layoutInflater.inflate(R.layout.layout_item_gank, parent, false);
            convertView.setTag(new ViewHolder(convertView));
        }
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        GanHItem ganHItem = getItem(position);
        viewHolder.ganhItem.setText(ganHItem.getDesc());
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.ganh_item)
        TextView ganhItem;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
