package com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.IGoodsService;
import com.qf.entity.Goods;
import com.qf.entity.Page;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @version 1.0
 * @data 5/20/2019 17:58
 * @user yingyunzhizi
 */
@Controller
@RequestMapping(value = "/goods")
public class GoodsController {

    @Reference
    private IGoodsService goodsService;

    @Value("${img.server}")
    private String imgPath;

    /**
     * 商品展示
     *
     * @param model
     * @return
     */
    @RequestMapping("/listGoods")
    public String listGoods(Model model, Page<Goods> page) {
        //List<Goods> goodsList = goodsService.listGoods();
        //model.addAttribute("goodsList",goodsList);
        //这里其实没有用到pageInfo了,
        // 只是第一种分页方法懒的改,所以取一样了
        Page<Goods> pageInfo = goodsService.getGoodsPage(page);

        model.addAttribute("page", pageInfo);
        model.addAttribute("imgPath", imgPath);
        model.addAttribute("url", "/goods/listGoods?");

        return "goodslist";
    }

    /**
     * 添加商品
     *
     * @param goods
     * @param gimageList
     * @return
     */
    @RequestMapping(value = "/addGoods")
    public String addGoods(Goods goods, String[] gimageList) {

        //第一先处理商品图片,添加对象中
        String imagePath = "";
        for (String gimage : gimageList) {
            if (imagePath.length() > 0) {
                imagePath += "|";
            }
            imagePath += gimage;
        }
        goods.setGimages(imagePath);

        goodsService.addGoods(goods);

        return "redirect:/goods/listGoods";
    }

    /**
     * 删除商品
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/delGoodsById/{id}")
    public String delGoodsById(@PathVariable("id") Integer id) {
        Integer delNum = goodsService.delGoodsById(id);
        return "redirect:/goods/listGoods";
    }

    /**
     * 根据id查找商品,跳转到修改商品html
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "/getGoodsById/{id}")
    public String getGoodsById(@PathVariable("id") Integer id, Model model) {
        Goods goods = goodsService.getGoodsById(id);
        model.addAttribute("goods", goods);
        model.addAttribute("imgPath", imgPath);
        return "goodsUpdate";
    }

    /**
     * 修改商品
     *
     * @param goods
     * @return
     */
    @RequestMapping(value = "/updateGoodsById")
    public String updateGoodsById(Goods goods, String[] gimageList) {

        //还是先处理商品图片,添加进入对象中
        String imagePath = "";
        for (String gimage : gimageList) {
            if (gimage.length() > 0) {
                imagePath += "|";
            }
            imagePath += gimage;
        }
        goods.setGimages(imagePath);
        goodsService.updateGoods(goods);
        return "redirect:/goods/listGoods";
    }


    @RequestMapping("/page")
    public String page() {

        return "a";
    }
}
