package com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.ISearchService;
import com.qf.entity.Goods;
import com.qf.entity.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @version 1.0
 * @data 5/22/2019 17:52
 * @user yingyunzhizi
 */
@Controller
@RequestMapping(value = "/search")
public class SearchController {

    @Reference
    private ISearchService searchService;

    /**
     * 根据关键字进行搜索
     * @param keyWord
     * @param model
     * @return
     */
    @RequestMapping("/searchByKeyWord")
    public String searchByKeyWord(String keyWord, Model model, Page<Goods> page){
        //通过关键词进行搜索
        System.out.println("进行商品的搜索，关键词是：" + keyWord);
        Page<Goods> goodsPage = searchService.queryByKeyWord(keyWord,page);
        model.addAttribute("page",goodsPage);
        //model.addAttribute("url","/search/searchByKeyWord?keyWord="+keyWord+"&");
        model.addAttribute("url","/search/searchByKeyWord?keyWord="+keyWord);
        //model.addAttribute("url","/search/searchByKeyWord/keyWord?");
        System.out.println(goodsPage);
        return "searchlist";
    }
}
