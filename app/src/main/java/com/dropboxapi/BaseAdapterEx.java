package com.dropboxapi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by Administrator on 2015-01-06.
 */
public class BaseAdapterEx extends BaseAdapter {



    Context mContext=null;
    LayoutInflater mLayoutInflater=null;


    public BaseAdapterEx(Context context)
    {
        mContext=context;

        mLayoutInflater=LayoutInflater.from(mContext);
    }
@Override
    public int getCount()
    {return  GlobalVar.getImgfileList().size();
    }
    @Override
    public long getItemId(int position)
    {
        return position;
    }
    @Override
    public String getItem(int position)
    {
        return GlobalVar.getImgfileList().get(position);

    }
    class ViewHolder
    {
        TextView mNameTv;


    }
    @Override
    public View getView(int position, View convertView,ViewGroup parent)
    {
        View itemLayout=convertView;
        ViewHolder viewHolder=null;

        if(itemLayout==null)
        {
            itemLayout=mLayoutInflater.inflate(R.layout.list_view_item_layout,null);
            viewHolder=new ViewHolder();
            viewHolder.mNameTv=(TextView)itemLayout.findViewById(R.id.name_text);
            itemLayout.setTag(viewHolder);
        }
        else
        {
           viewHolder=(ViewHolder)itemLayout.getTag();
        }



        viewHolder.mNameTv.setText(GlobalVar.getImgfileList().get(position));

        return itemLayout;
    }


}
