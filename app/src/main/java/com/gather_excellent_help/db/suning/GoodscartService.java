package com.gather_excellent_help.db.suning;

import java.util.List;
import java.util.Map;

/**
 * Created by Dapeng Fang on 2017/11/15.
 */

public interface GoodscartService {

    public void addGoods(Object[] params);

    public void deleteGoods(Object[] params);

    public void deleteAllGoods();

    public void updateGoods(Object[] params);

    public void updateGoodsCheck(Object[] params);

    public Map<String, String> selectGoods(String[] selectionArgs);

    public Map<String, String> selectGoodsId(String[] selectionArgs);

    public List<Map<String, String>> selectAllGoods(String[] selectionArgs);
}
