package com.qf.listener;

import com.qf.controller.ItemController;
import com.qf.entity.Goods;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @version 1.0
 * @data 5/28/2019 21:10
 * @user yingyunzhizi
 */
@Component
public class ItemRabbitmqListener {

    @Autowired
    private Configuration configuration;

    @RabbitListener(queues = "item_queue")
    public void goodsMsgHandler(Goods goods) {

        //静态页面的输出路径:必须可以让外界访问到
        String sPath = ItemController.class.getResource("/static/html/").getPath() + goods.getId() + ".html";

        try (FileWriter fileWriter = new FileWriter(sPath)) {
            //获得商品详情页面模板
            Template template = configuration.getTemplate("goods.ftl");
            //获得商品的对应数据,调用商品服务查询商品信息,用这个就不需要查询商品数量
            //Goods goods = goodsService.getGoodsById(gid);
            String[] stringImages = goods.getGimages().split("\\|");
            Map map = new HashMap();
            map.put("goods", goods);
            map.put("stringImages", stringImages);

            //生成详情页
            template.process(map, fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
    }
}
