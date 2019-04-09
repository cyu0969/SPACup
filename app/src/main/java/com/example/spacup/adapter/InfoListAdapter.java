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
import com.example.spacup.MyApp;
import com.example.spacup.R;
import com.example.spacup.item.CertificateInfoItem;
import com.example.spacup.item.MemberInfoItem;
import com.example.spacup.lib.DialogLib;
import com.example.spacup.lib.GoLib;
import com.example.spacup.lib.MyLog;
import com.example.spacup.lib.StringLib;
import com.example.spacup.remote.RemoteService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class InfoListAdapter extends RecyclerView.Adapter<InfoListAdapter.ViewHolder> {

    private final String TAG = this.getClass().getSimpleName();

    private Context context;
    private int resource;
    private ArrayList<CertificateInfoItem> itemList;
    private MemberInfoItem memberInfoItem;

    public InfoListAdapter(Context context, int resource, ArrayList<CertificateInfoItem> itemList) {
        this.context = context;
        this.resource = resource;
        this.itemList = itemList;

        memberInfoItem = ((MyApp) context.getApplicationContext()).getMemberInfoItem();
    }

    public void setItem(CertificateInfoItem newItem) {
        for (int i=0; i < itemList.size(); i++) {
            CertificateInfoItem item = itemList.get(i);

            if (item.seq == newItem.seq) {
                itemList.set(i, newItem);
                notifyItemChanged(i);
                break;
            }
        }
    }

    public void addItemList(ArrayList<CertificateInfoItem> itemList) {
        this.itemList.addAll(itemList);
        notifyDataSetChanged();
    }

    private void changeItemKeep(int seq, boolean keep) {
        for (int i=0; i < itemList.size(); i++) {
            if (itemList.get(i).seq == seq) {
                itemList.get(i).isKeep = keep;
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
        final CertificateInfoItem item = itemList.get(position);
        MyLog.d(TAG, "getView " + item);

        if (item.isKeep) {
            holder.keep.setImageResource(R.drawable.ic_keep_on);
        } else {
            holder.keep.setImageResource(R.drawable.ic_keep_off);
        }

        holder.name.setText(item.name);
        holder.description.setText(StringLib.getInstance().getSubString(context,
                item.description, Constant.MAX_LENGTH_DESCRIPTION));

        setImage(holder.image, item.imageFilename);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoLib.getInstance().goSpecupInfoActivitty(context, item.seq);
            }
        });

        holder.keep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item.isKeep) {
                    DialogLib.getInstance().showKeepDeleteDialog(context,
                            keepDeleteHandler, memberInfoItem.seq, item.seq);
                } else {
                    DialogLib.getInstance().showKeepInsertDialog(context,
                            keepInsertHandler, memberInfoItem.seq, item.seq);
                }
            }
        });
    }

    private void setImage(ImageView imageView, String fileName) {
        if (StringLib.getInstance().isBlank(fileName)) {
            Picasso.with(context).load(R.drawable.bg_specup_drawer).into(imageView);
        } else {
            Picasso.with(context).load(RemoteService.IMAGE_URL + fileName).into(imageView);
        }
    }

    Handler keepInsertHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            changeItemKeep(msg.what, true);
        }
    };

    Handler keepDeleteHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            changeItemKeep(msg.what, false);
        }
    };

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
