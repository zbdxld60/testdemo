package com.landon.nfcapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.landon.nfcapplication.R;
import com.landon.nfcapplication.bean.CardSnInfo;
import java.util.List;

public class SnAdapter extends ArrayAdapter<CardSnInfo> {
    private int resourceId;

    public SnAdapter(Context context, int textViewResourceId, List<CardSnInfo> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CardSnInfo cardSnInfo = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.mtv_id= (TextView) view.findViewById (R.id.tv_id);
            viewHolder.mtv_sn= (TextView) view.findViewById (R.id.tv_sn);
            viewHolder.mtv_read_time= (TextView) view.findViewById (R.id.tv_read_time);
            view.setTag(viewHolder); // 将ViewHolder存储在View中
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag(); // 重新获取ViewHolder
        }
        viewHolder.mtv_id.setText("" + cardSnInfo.getId());
        viewHolder.mtv_sn.setText(cardSnInfo.getSn());
        viewHolder.mtv_read_time.setText(cardSnInfo.getReadTime());
        return view;
    }

    private class ViewHolder {
        TextView mtv_id;
        TextView mtv_sn;
        TextView mtv_read_time;
    }
}
