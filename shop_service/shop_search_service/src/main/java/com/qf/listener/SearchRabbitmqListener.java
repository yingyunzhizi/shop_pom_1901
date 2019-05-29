package com.qf.listener;

import com.qf.entity.Goods;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @version 1.0
 * @data 5/28/2019 21:05
 * @user yingyunzhizi
 */
@Component
public class SearchRabbitmqListener {

    @Autowired
    private SolrClient solrClient;

    @RabbitListener(queues = "search_queue")
    public void goodsMsgHander(Goods goods){
        SolrInputDocument sd = new SolrInputDocument();
        sd.addField("id",goods.getId()+"");
        sd.addField("gname",goods.getGname());
        sd.addField("ginfo",goods.getGinfo());
        sd.addField("gprice",goods.getGprice().floatValue());
        sd.addField("gsave",goods.getGsave());
        sd.addField("gimages",goods.getGimages());

        try {
            solrClient.add(sd);
            solrClient.commit();
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
