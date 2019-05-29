package com.qf;

import com.qf.entity.Goods;
import com.qf.entity.Page;

/**
 * @version 1.0
 * @data 5/22/2019 19:26
 * @user yingyunzhizi
 */
public interface ISearchService {

    Page<Goods> queryByKeyWord(String keyword,Page<Goods> page);

    int addGoodsToSearch(Goods goods);

    void delGoodsFromSearch(Integer id);
}
