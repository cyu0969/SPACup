package com.example.spacup.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.spacup.Constant;
import com.example.spacup.R;
import com.example.spacup.item.CertificateInfoItem;
import com.example.spacup.item.FavoriteItem;
import com.example.spacup.lib.DialogLib;
import com.example.spacup.lib.GoLib;
import com.example.spacup.lib.MyLog;
import com.example.spacup.lib.StringLib;

import java.util.ArrayList;

public class FavoriteListAdapter extends RecyclerView.Adapter<FavoriteListAdapter.ViewHolder> {

    private final String TAG = this.getClass().getSimpleName();

    private Context context;
    private int resource;
    private ArrayList<FavoriteItem> itemList;
    private int memberSeq;

    public FavoriteListAdapter(Context context, int resource, ArrayList<FavoriteItem> itemList, int memberSeq) {
        this.context = context;
        this.resource = resource;
        this.itemList = itemList;
        this.memberSeq = memberSeq;
    }

    public void setItemList(ArrayList<FavoriteItem> itemList) {
        this.itemList = itemList;
        notifyDataSetChanged();
    }

    public void setItem(CertificateInfoItem newItem) {
        for (int i=0; i < itemList.size(); i++) {
            FavoriteItem item = itemList.get(i);

            if (item.seq == newItem.seq && !newItem.isFavorite) {
                itemList.remove(i);
                notifyItemChanged(i);
                break;
            }
        }
    }

    private void removeItem(int seq) {
        for (int i=0; i < itemList.size(); i++) {
            if (itemList.get(i).seq == seq) {
                itemList.remove(i);
                notifyItemChanged(i);
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final FavoriteItem item = itemList.get(position);
        MyLog.d(TAG, "getView " + item);

        if (item.isFavorite) {
            holder.keep.setImageResource(R.drawable.ic_keep_on);
        } else {
            holder.keep.setImageResource(R.drawable.ic_keep_off);
        }

        holder.name.setText(item.name);
        holder.description.setText(
                StringLib.getInstance().getSubString(context,
                        item.description, Constant.MAX_LENGTH_DESCRIPTION));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoLib.getInstance().goSpecupInfoActivitty(context, item.seq);
            }
        });

        holder.keep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogLib.getInstance().showKeepDeleteDialog(context, keepHandler, memberSeq, item.seq);
            }
        });
    }

    Handler keepHandler;

    {
        keepHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                removeItem(msg.what);
            }
        };
    }

    /**
     * 아이템을 보여주기 위한 뷰홀더 클래스
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        ImageView keep;
        TextView name;
        TextView description;

        public ViewHolder(View itemView) {
            super(itemView);

            image = (ImageView) itemView.findViewById(R.id.image);
            keep = (ImageView) itemView.findViewById(R.id.keep);
            name = (TextView) itemView.findViewById(R.id.name);
            description = (TextView) itemView.findViewById(R.id.description);
        }
    }

}
