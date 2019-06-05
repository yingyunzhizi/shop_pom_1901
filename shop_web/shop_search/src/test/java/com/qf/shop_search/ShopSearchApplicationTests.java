package com.qf.shop_search;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShopSearchApplicationTests {

    @Autowired
    private SolrClient solrClient;

    @Test
    public void contextLoads() {
    }

    @Test
    public void test1() throws IOException, SolrServerException {
        for (int i = 0; i < 10; i++) {
            SolrInputDocument sd = new SolrInputDocument();
            sd.addField("id", i + 1);
            if (i == 5) {
                sd.addField("gname", "传音手机,传音,传音" + i);
            } else {
                sd.addField("gname", "传音手机" + i);
            }
            sd.addField("ginfo", "战斗机");
            sd.addField("gprice", "199.9" + i);
            sd.addField("gsave", "56" + i);
            sd.addField("gimages", "http://www.baidu.com");
            solrClient.add(sd);
            solrClient.commit();

        }
    }


}
