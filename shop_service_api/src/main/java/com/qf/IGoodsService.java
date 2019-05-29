package com.qf;

import com.qf.entity.Goods;
import com.qf.entity.Page;

import java.util.List;

public interface IGoodsService {

    List<Goods> listGoods();

    Goods addGoods(Goods goods);

    Integer delGoodsById(Integer id);

    Goods getGoodsById(Integer id);

    Goods updateGoods(Goods goods);

    //这是pageHelper的
    //Page<Goods> getGoodsPage(Page<Goods> page);

    //原生的分页page
    Page<Goods> getGoodsPage(Page<Goods> page);

}
