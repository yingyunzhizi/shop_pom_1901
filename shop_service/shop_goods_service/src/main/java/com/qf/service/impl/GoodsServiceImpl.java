package com.qf.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.qf.IGoodsService;
import com.qf.ISearchService;
import com.qf.dao.IGoodsMapper;
import com.qf.entity.Goods;
import com.qf.entity.Page;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @version 1.0
 * @data 5/20/2019 20:01
 * @user yingyunzhizi
 */
@Service
public class GoodsServiceImpl implements IGoodsService {

    @Reference
    private ISearchService searchService;

    @Autowired
    private IGoodsMapper goodsMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public List<Goods> listGoods() {
        List<Goods> goodsList = goodsMapper.selectList(null);
        return goodsList;
    }

    @Override
    public Goods addGoods(Goods goods) {
        //先添加进入数据库
        int insert = goodsMapper.insert(goods);
        /*//同步到索引库中
        int i = searchService.addGoodsToSearch(goods);
        System.out.println(i);
        System.out.println(insert);
        //同时发生http请求提醒详情工程生成静态页面
        HttpUtil.sendGet("http://localhost:8083/item/creatHtml?gid="+goods.getId());*/
        //将同步到索引库中和发送http请求给详情工程交给rabbit完成
        rabbitTemplate.convertAndSend("goods_exchange","",goods);
        return goods;
    }

    @Override
    public Integer delGoodsById(Integer id) {
        int i = goodsMapper.deleteById(id);
        searchService.delGoodsFromSearch(id);
        return i;
    }

    @Override
    public Goods getGoodsById(Integer id) {
        return goodsMapper.selectById(id);
    }

    @Override
    public Goods updateGoods(Goods goods) {
        int i = goodsMapper.updateById(goods);
        return goods;
    }

    /**
     * 使用原生的page分页
     * @param page
     * @return
     */
    @Override
    public Page<Goods> getGoodsPage(Page<Goods> page) {
        int currentPage = page.getCurrentPage();
        int pageSize = page.getPageSize();

        //总条数
        int totalCount = goodsMapper.selectCount(null);
        //总页数
        int totalPage;
        if (totalCount % pageSize ==0){
            totalPage = totalCount/pageSize;
        }else {
            totalPage = (totalCount/pageSize)+1;
        }
        //每页显示的数据
        List<Goods> list = goodsMapper.getGoodsList((currentPage-1)*pageSize,pageSize);

        page.setTotalCount(totalCount);
        page.setTotalPage(totalPage);
        page.setList(list);
        return page;
    }

    /**
     * 使用pageInf,PageHelper这种方式
    /*@Override
    public Page<Goods> getGoodsPage(Page<Goods> page) {
        int currentPage = page.getCurrentPage();
        int pageSize = page.getPageSize();
        //核心代码,使用分页插件
        PageHelper.startPage(currentPage,pageSize);
        List<Goods> pageHelp = goodsMapper.selectList(null);
        PageInfo<Goods> goodsPageInfo = new PageInfo<>(pageHelp);
        //这里要返回的是实体类的Page,没有返回pageInfo是因为用到pageinfo的要导包,导包了的要配置数据源
        return new Page<Goods>(goodsPageInfo.getPageNum(), goodsPageInfo.getPageSize(), goodsPageInfo.getPages(),(int) goodsPageInfo.getTotal(),   goodsPageInfo.getList());
    }*/
}
