package com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.IGoodsService;
import com.qf.entity.Goods;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @version 1.0
 * @data 5/23/2019 20:01
 * @user yingyunzhizi
 */
@Controller
@RequestMapping("/item")
public class ItemController {

    @Autowired
    private Configuration configuration;

    @Reference
    private IGoodsService goodsService;

    /**
     * 在添加商品时候将指定的商品详情页面生成静态页面
     * @param gid
     * @return
     */
    @RequestMapping("/creatHtml")
    public String creatHtml(Integer gid){

        //静态页面的输出路径:必须可以让外界访问到
        String sPath = ItemController.class.getResource("/static/html/").getPath() + gid + ".html";

        try (FileWriter fileWriter = new FileWriter(sPath)) {
            //获得商品详情页面模板
            Template template = configuration.getTemplate("goods.ftl");
            //获得商品的对应数据,调用商品服务查询商品信息
            Goods goods = goodsService.getGoodsById(gid);
            String[] stringImages = goods.getGimages().split("\\|");
            Map map = new HashMap();
            map.put("goods",goods);
            map.put("stringImages",stringImages);

            //生成详情页
            template.process(map,fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }

        return "success";
    }

}
