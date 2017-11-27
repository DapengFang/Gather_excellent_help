package com.gather_excellent_help.ui.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.gather_excellent_help.R;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.bean.AddressDetailBean;
import com.gather_excellent_help.bean.CodeStatueBean;
import com.gather_excellent_help.bean.suning.SuningGoodscartBean;
import com.gather_excellent_help.event.AnyEvent;
import com.gather_excellent_help.event.EventType;
import com.gather_excellent_help.ui.activity.address.AddNewAddressActivity;
import com.gather_excellent_help.ui.activity.address.PersonAddressActivity;
import com.gather_excellent_help.ui.activity.suning.SuningGoodscartActivity;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.NetUtil;
import com.gather_excellent_help.utils.Tools;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import de.greenrobot.event.EventBus;
import okhttp3.Call;

/**
 * 作者：Dapeng Fang on 2016/9/9 16:22
 * <p>
 * 邮箱：fdp111888@163.com
 */

public class PersonAddressAdapter extends RecyclerView.Adapter<PersonAddressAdapter.AddressViewholder> {

    private Context context;
    private List<AddressDetailBean.DataBean> data;
    private TextView tv_address_top_num;
    private android.app.AlertDialog alertDialog;

    public PersonAddressAdapter(Context context, List<AddressDetailBean.DataBean> data, TextView tv_address_top_num) {
        this.context = context;
        this.data = data;
        this.tv_address_top_num = tv_address_top_num;
    }

    @Override
    public AddressViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AddressViewholder(View.inflate(context, R.layout.item_me_address, null));
    }

    @Override
    public void onBindViewHolder(final AddressViewholder holder, final int position) {
        final AddressDetailBean.DataBean dataBean = data.get(position);
        if (dataBean.getAccept_name() != null) {
            holder.tvName.setText(dataBean.getAccept_name());
        }
        if (dataBean.getMobile() != null) {
            holder.tvPhone.setText(dataBean.getMobile());
        }
        if (dataBean.getArea() != null && dataBean.getAddress() != null) {
            holder.tvDetails.setText(dataBean.getArea() + " " + dataBean.getAddress());
        }
        int is_default = dataBean.getIs_default();
        if (is_default == 1) {
            holder.cb_address_default.setChecked(true);
        } else {
            holder.cb_address_default.setChecked(false);
        }

        holder.tv_address_bianji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toUpdateAddress(dataBean);
            }
        });
        holder.tv_address_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.tv_address_delete.setClickable(false);
                showDeleteDialog(dataBean, position, v);
            }
        });
        holder.cb_address_default.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i = 0; i < data.size(); i++) {
                    if (i != position) {
                        data.get(i).setIs_default(0);
                        notifyItemChanged(i);//指定更新位置
                        //updateAddress(i,0);
                    }
                }
                if (dataBean.getIs_default() == 0) {
                    dataBean.setIs_default(1);
                } else if (dataBean.getIs_default() == 1) {
                    dataBean.setIs_default(1);
                }
                notifyItemChanged(position);
                updateIsDefault(dataBean);
            }
        });
    }

    /**
     * 修改默认地址
     *
     * @param dataBean
     */
    private void updateIsDefault(AddressDetailBean.DataBean dataBean) {
        String userLogin = Tools.getUserLogin(context);
        String default_url = Url.BASE_URL + "suning/SNbusinessHandler.ashx?action=SetDefault";
        OkHttpUtils
                .post()
                .url(default_url)
                .addParams("user_id", userLogin)
                .addParams("id", String.valueOf(dataBean.getId()))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtil.e("网络连接出现问题~");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtil.e(response);
                        CodeStatueBean codeStatueBean = new Gson().fromJson(response, CodeStatueBean.class);
                        int statusCode = codeStatueBean.getStatusCode();
                        Toast.makeText(context, codeStatueBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                        EventBus.getDefault().post(new AnyEvent(EventType.UPDATA_ADDRESS,"更新地址界面信息"));
                        EventBus.getDefault().post(new AnyEvent(EventType.UPDATA_ADDRESS_ORDER,"更新订单页面的地址信息"));
                    }
                });
    }

    /**
     * 删除地址信息
     *
     * @param dataBean
     * @param v
     */
    private void delAddress(final AddressDetailBean.DataBean dataBean, final int position, final View v) {
        String del_url = Url.BASE_URL + "suning/SNbusinessHandler.ashx?action=DeleteUserAddress";
        OkHttpUtils
                .post()
                .url(del_url)
                .addParams("addrId", String.valueOf(dataBean.getId()))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtil.e("网络连接出现问题~");
                        v.setClickable(true);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtil.e(response);
                        v.setClickable(true);
                        CodeStatueBean codeStatueBean = new Gson().fromJson(response, CodeStatueBean.class);
                        int statusCode = codeStatueBean.getStatusCode();
                        Toast.makeText(context, codeStatueBean.getStatusMessage(), Toast.LENGTH_SHORT).show();
                        switch (statusCode) {
                            case 1:
                                int index = data.indexOf(dataBean);
                                data.remove(index);
                                notifyItemRemoved(position);//当移除的时候用这个刷新
                                notifyDataSetChanged();
                                tv_address_top_num.setText(data.size() + "");
                                EventBus.getDefault().post(new AnyEvent(EventType.UPDATA_ADDRESS,"更新地址界面信息"));
                                EventBus.getDefault().post(new AnyEvent(EventType.UPDATA_ADDRESS_ORDER,"更新订单页面的地址信息"));
                                break;
                        }
                    }
                });
    }

    /**
     * 编辑用户地址信息
     *
     * @param dataBean
     */
    private void toUpdateAddress(AddressDetailBean.DataBean dataBean) {
        Intent intent = new Intent(context, AddNewAddressActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("addrId", dataBean.getId());
        bundle.putString("accept_name", dataBean.getAccept_name());
        bundle.putString("mobile", dataBean.getMobile());
        bundle.putString("area", dataBean.getArea());
        bundle.putString("area_id", dataBean.getArea_id());
        bundle.putString("address", dataBean.getAddress());
        bundle.putString("postmall", dataBean.getPost_code());
        bundle.putInt("isdefault", dataBean.getIs_default());
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    /**
     * 解除支付宝绑定的dialog
     */
    private void showDeleteDialog(final AddressDetailBean.DataBean dataBean, final int position, final View v) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setTitle("温馨提示")
                .setMessage("您确定要删除该收货地址吗?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        delAddress(dataBean, position, v);
                    }
                })
                .setNegativeButton("取消", null);
        alertDialog = builder.create();
        PersonAddressActivity activity = (PersonAddressActivity) context;
        if (activity != null && !activity.isFinishing()) {
            alertDialog.show();
        }
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    class AddressViewholder extends RecyclerView.ViewHolder {

        private TextView tvName;
        private TextView tvPhone;
        private TextView tvDetails;
        private TextView tv_address_bianji;
        private TextView tv_address_delete;
        private CheckBox cb_address_default;
        private LinearLayout ll_me_address;

        public AddressViewholder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_address_name);
            tvPhone = (TextView) itemView.findViewById(R.id.tv_address_phone);
            tvDetails = (TextView) itemView.findViewById(R.id.tv_address_details);
            tv_address_delete = (TextView) itemView.findViewById(R.id.tv_address_delete);
            tv_address_bianji = (TextView) itemView.findViewById(R.id.tv_address_bianji);
            cb_address_default = (CheckBox) itemView.findViewById(R.id.cb_address_default);
            ll_me_address = (LinearLayout) itemView.findViewById(R.id.ll_me_address);
        }
    }

}
