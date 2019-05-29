package com.qf.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.qf.ISearchService;
import com.qf.entity.Goods;
import com.qf.entity.Page;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @version 1.0
 * @data 5/22/2019 20:57
 * @user yingyunzhizi
 */
@Service
public class SearchServiceImpl implements ISearchService {

    @Autowired
    private SolrClient solrClient;

    @Override
    public Page<Goods> queryByKeyWord(String keyword,Page<Goods> page) {

        SolrQuery solrQuery;
        if (keyword==null||keyword.trim().equals("")){
            solrQuery = new SolrQuery("*.*");
        }else {
            solrQuery = new SolrQuery("gname:" + keyword + " || ginfo:" + keyword);
        }

        //设置高亮
        solrQuery.setHighlight(true);//开启高亮
        solrQuery.setHighlightSimplePre("<font color='red'>");//设置前缀
        solrQuery.setHighlightSimplePost("</font>");//设置后缀
        solrQuery.addHighlightField("gname");

        //高亮的折叠
        solrQuery.setHighlightFragsize(5);//这个是设置高亮折叠后每次显示的长度
        solrQuery.setHighlightSnippets(3);//这个是设置高亮折叠的次数

        //设置分页参数
        solrQuery.setStart((page.getCurrentPage()-1)*(page.getPageSize()));
        int pageSize = page.getPageSize();
        solrQuery.setRows(pageSize);

        //执行查询
        List<Goods> goodsList = new ArrayList<>();
        try {
            QueryResponse query = solrClient.query(solrQuery);
            //通过QueryResponse对象获取普通的搜索结果
            SolrDocumentList results = query.getResults();

            //eg:{"1";{"gname},["传音<font>手机"]}}

            //通过queryResponse对象获取高亮的搜索结果
            Map<String, Map<String, List<String>>> highlighting = query.getHighlighting();

            //设置分页参数,solr库中查出来的是object类型的获得总条数和总页码
            int totalCount = Integer.parseInt(results.getNumFound()+"");
            if (totalCount % pageSize==0){
                page.setTotalPage(totalCount/pageSize);
            }else {
                page.setTotalPage((totalCount/pageSize)+1);
            }
            page.setTotalCount(totalCount);

            for (SolrDocument result : results) {
                Goods goods = new Goods();
                goods.setId(Integer.parseInt(result.get("id")+""));
                goods.setGname(result.get("gname")+"");
                goods.setGinfo(result.get("ginfo")+"");
                BigDecimal bigDecimal = BigDecimal.valueOf((float)result.get("gprice"));
                goods.setGprice(bigDecimal);
                goods.setGsave(Integer.parseInt(result.get("gsave")+""));
                goods.setGimages(result.get("gimages")+"");

                //判断当前商品是否存在高亮
                if (highlighting.containsKey(goods.getId()+"")){
                    //当前商品存在高亮
                    Map<String, List<String>> stringListMap = highlighting.get(goods.getId() + "");
                    //获得高亮的字段
                    List<String> gname = stringListMap.get("gname");
                    if (gname!=null){
                        goods.setGname(gname.get(0));
                    }
                }
                goodsList.add(goods);
            }
            page.setList(goodsList);
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return page;
    }

    /**
     * 将商品数据保存到索引库中
     * @param goods
     * @return
     */
    @Override
    public int addGoodsToSearch(Goods goods) {
        /*SolrInputDocument sd = new SolrInputDocument();
        sd.addField("id",goods.getId()+"");
        sd.addField("gname",goods.getGname());
        sd.addField("ginfo",goods.getGinfo());
        sd.addField("gprice",goods.getGprice().floatValue());
        sd.addField("gsave",goods.getGsave());
        sd.addField("gimages",goods.getGimages());

        try {
            solrClient.add(sd);
            solrClient.commit();
            return 1;
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        return 0;
    }

    @Override
    public void delGoodsFromSearch(Integer id) {
        SolrInputDocument sd = new SolrInputDocument();
        try {
            solrClient.deleteById(id+"");
            solrClient.commit();
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
