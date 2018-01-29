package com.gather_excellent_help.utils.shareutil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gather_excellent_help.MainActivity;
import com.gather_excellent_help.R;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.utils.NetUtil;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dapeng Fang on 2018/1/16.
 */

public class ShareUtil {
    public static final String changware_url = Url.BASE_URL + "GoodsConvert.aspx";
    public static final String changcoupon_url = Url.BASE_URL + "GetCouponUrl.aspx";
    public static final String getwords_url = Url.BASE_URL + "GetTpwd.aspx";


    /**
     * 获取商品的转链链接
     */
    public static void getWareChangeUrl(NetUtil netUtil, Map<String, String> map) {
        netUtil.okHttp2Server2(changware_url, map);
    }

    /**
     * 获取优惠券的转链链接
     */
    public static void getCouponChangeUrl(NetUtil netUtil, Map<String, String> map) {
        netUtil.okHttp2Server2(changcoupon_url, map);
    }

    /**
     * 获取淘口令
     */
    public static void getChangeWordUrl(NetUtil netUtil, Map<String, String> map) {
        netUtil.okHttp2Server2(getwords_url, map);
    }

    /**
     * 获取商品转链的参数封装
     *
     * @param map
     * @param goodsId
     * @param adzoneId
     * @return
     */
    public static Map<String, String> getChangeWareParam(Map<String, String> map, String goodsId, String adzoneId) {
        map.put("goodsId", goodsId);
        map.put("adzoneId", adzoneId);
        return map;
    }

    /**
     * 获取商品淘口令的参数封装
     *
     * @param map
     * @param user_id
     * @param click_url
     * @param goods_img
     * @param goods_title
     * @return
     */
    public static Map<String, String> getChangeWordsParam(Map<String, String> map, String user_id, String click_url, String goods_img, String goods_title) {
        map.put("user_id", user_id);
        map.put("convert_url", click_url);
        map.put("img_url", goods_img);
        map.put("title", goods_title);
        return map;
    }


    /**
     * 剪切板剪切淘口令
     */
    public static void showCopyDialog(final Context context, int whick_share, final String share_content, String goods_price,
                                      final UMShareListener shareListener, final String goods_img, final String goods_title, AlertDialog dialog) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View inflate = View.inflate(context, R.layout.item_copy_taoword_dialog, null);
        TextView tvCopyContent = (TextView) inflate.findViewById(R.id.tv_copy_taoword_content);
        TextView tvCopyDismiss = (TextView) inflate.findViewById(R.id.tv_copy_taoword_dismiss);
        TextView tvCopyShare = (TextView) inflate.findViewById(R.id.tv_copy_taoword_share);
        LinearLayout ll_share_qq = (LinearLayout) inflate.findViewById(R.id.ll_share_qq);
        LinearLayout ll_share_weixin = (LinearLayout) inflate.findViewById(R.id.ll_share_weixin);
        LinearLayout ll_share_sina = (LinearLayout) inflate.findViewById(R.id.ll_share_sina);
        LinearLayout ll_share_weixin_friend = (LinearLayout) inflate.findViewById(R.id.ll_share_weixin_friend);
        int index = share_content.indexOf(goods_price);
        SpannableString spannableString = new SpannableString(share_content);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#333333"));
        spannableString.setSpan(colorSpan, index - 1, index + goods_price.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        RelativeSizeSpan sizeSpan01 = new RelativeSizeSpan(1.2f);
        spannableString.setSpan(sizeSpan01, index, index + goods_price.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tvCopyContent.setText(spannableString);
        dialog = builder.setView(inflate)
                .show();

        ll_share_qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareDiffSolfplam(SHARE_MEDIA.QQ, shareListener, context, share_content, goods_img, goods_title);
            }
        });

        ll_share_weixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareDiffSolfplam(SHARE_MEDIA.WEIXIN, shareListener, context, share_content, goods_img, goods_title);
            }
        });

        ll_share_sina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareDiffSolfplam(SHARE_MEDIA.SINA, shareListener, context, share_content, goods_img, goods_title);
            }
        });

        ll_share_weixin_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareDiffSolfplam(SHARE_MEDIA.WEIXIN_CIRCLE, shareListener, context, share_content, goods_img, goods_title);
            }
        });

        final AlertDialog finalDialog = dialog;
        tvCopyDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (finalDialog.isShowing()) {
                    finalDialog.dismiss();
                }
            }
        });
        tvCopyShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                cmb.setText(share_content);
                Toast.makeText(context, "淘口令已复制", Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * 分享淘口令链接到不同给的平台
     *
     * @param platform
     */
    public static void shareDiffSolfplam(SHARE_MEDIA platform, UMShareListener shareListener, Context context, String share_content, String goods_img, String goods_title) {
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(share_content);
        Activity activity = (Activity) context;
        UMImage image = new UMImage(context, goods_img);//网络图片
        UMImage thumb = new UMImage(context, R.mipmap.juyoubang_logo);
        image.setThumb(thumb);
        new ShareAction(activity)
                .setPlatform(platform)
                .withText(goods_title)
                .withMedia(image)
                .setCallback(shareListener)
                .share();
    }


}
