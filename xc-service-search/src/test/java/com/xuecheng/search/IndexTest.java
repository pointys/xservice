package com.xuecheng.search;

import com.alibaba.fastjson.JSONObject;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 韩浩辰
 * @date 2020/6/11 18:28
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class IndexTest {

    @Autowired
    RestHighLevelClient restHighLevelClient;
    @Autowired
    RestClient restClient;

    //创建索引
    @Test
    public void testCreateIndex() throws IOException {
        //创建索引对象
        CreateIndexRequest createIndexRequest = new CreateIndexRequest("xc_course");
        //设置参数
        createIndexRequest.settings(Settings.builder().put("number_of_shards","1").put("number_of_replicas","0"));
        //指定映射
        String source1="{\n" +
                "                \"properties\": {\n" +
                "                    \"description\": {\n" +
                "                        \"type\": \"text\",\n" +
                "                        \"analyzer\": \"ik_max_word\",\n" +
                "                        \"search_analyzer\": \"ik_smart\"\n" +
                "                    },\n" +
                "                    \"name\": {\n" +
                "                        \"type\": \"text\",\n" +
                "                        \"analyzer\": \"ik_max_word\",\n" +
                "                        \"search_analyzer\": \"ik_smart\"\n" +
                "                    },\n" +
                "\t\t\t\t\t\"pic\":{\n" +
                "\t\t\t\t\t\t\"type\":\"text\",\n" +
                "\t\t\t\t\t\t\"index\":false\n" +
                "\t\t\t\t\t},\n" +
                "                    \"price\": {\n" +
                "                        \"type\": \"float\"\n" +
                "                    },\n" +
                "                    \"studymodel\": {\n" +
                "                        \"type\": \"keyword\"\n" +
                "                    },\n" +
                "                    \"timestamp\": {\n" +
                "                        \"type\": \"date\",\n" +
                "                        \"format\": \"yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis\"\n" +
                "                    }\n" +
                "                }\n" +
                "            }";
        String source2="{\n" +
                "    \"properties\":{\n" +
                "        \"description\":{\n" +
                "            \"analyzer\":\"ik_max_word\",\n" +
                "            \"search_analyzer\":\"ik_smart\",\n" +
                "            \"type\":\"text\"\n" +
                "        },\n" +
                "        \"grade\":{\n" +
                "            \"type\":\"keyword\"\n" +
                "        },\n" +
                "        \"id\":{\n" +
                "            \"type\":\"keyword\"\n" +
                "        },\n" +
                "        \"mt\":{\n" +
                "            \"type\":\"keyword\"\n" +
                "        },\n" +
                "        \"name\":{\n" +
                "            \"analyzer\":\"ik_max_word\",\n" +
                "            \"search_analyzer\":\"ik_smart\",\n" +
                "            \"type\":\"text\"\n" +
                "        },\n" +
                "        \"users\":{\n" +
                "            \"index\":false,\n" +
                "            \"type\":\"text\"\n" +
                "        },\n" +
                "        \"charge\":{\n" +
                "            \"type\":\"keyword\"\n" +
                "        },\n" +
                "        \"valid\":{\n" +
                "            \"type\":\"keyword\"\n" +
                "        },\n" +
                "        \"pic\":{\n" +
                "            \"index\":false,\n" +
                "            \"type\":\"keyword\"\n" +
                "        },\n" +
                "        \"qq\":{\n" +
                "            \"index\":false,\n" +
                "            \"type\":\"keyword\"\n" +
                "        },\n" +
                "        \"price\":{\n" +
                "            \"type\":\"float\"\n" +
                "        },\n" +
                "        \"price_old\":{\n" +
                "            \"type\":\"float\"\n" +
                "        },\n" +
                "        \"st\":{\n" +
                "            \"type\":\"keyword\"\n" +
                "        },\n" +
                "        \"status\":{\n" +
                "            \"type\":\"keyword\"\n" +
                "        },\n" +
                "        \"studymodel\":{\n" +
                "            \"type\":\"keyword\"\n" +
                "        },\n" +
                "        \"teachmode\":{\n" +
                "            \"type\":\"keyword\"\n" +
                "        },\n" +
                "        \"teachplan\":{\n" +
                "            \"analyzer\":\"ik_max_word\",\n" +
                "            \"search_analyzer\":\"ik_smart\",\n" +
                "            \"type\":\"text\"\n" +
                "        },\n" +
                "        \"expires\":{\n" +
                "            \"type\":\"date\",\n" +
                "            \"format\":\"yyyy-MM-dd HH:mm:ss\"\n" +
                "        },\n" +
                "        \"pub_time\":{\n" +
                "            \"type\":\"date\",\n" +
                "            \"format\":\"yyyy-MM-dd HH:mm:ss\"\n" +
                "        },\n" +
                "        \"start_time\":{\n" +
                "            \"type\":\"date\",\n" +
                "            \"format\":\"yyyy-MM-dd HH:mm:ss\"\n" +
                "        },\n" +
                "        \"end_time\":{\n" +
                "            \"type\":\"date\",\n" +
                "            \"format\":\"yyyy-MM-dd HH:mm:ss\"\n" +
                "        }\n" +
                "    }\n" +
                "}";
        createIndexRequest.mapping("doc",source2, XContentType.JSON);
        //操作索引的客户端
        IndicesClient indices = restHighLevelClient.indices();
        //执行删除
        AcknowledgedResponse delete = indices.create(createIndexRequest, RequestOptions.DEFAULT);
        boolean acknowledged = delete.isAcknowledged();
        System.out.println(acknowledged);
    }

    //向索引添加文档
    @Test
    public void testAddDoc() throws IOException {

        //json字符串转map
        String jsonStr="{\n" +
                "\"name\": \"spring开发基础\",\n" +
                "\"description\": \"spring 在java领域非常流行，java程序员都在用。\",\n" +
                "\"studymodel\": \"201001\",\n" +
                "\"price\":88.6,\n" +
                "\"timestamp\":\"2018-02-24 19:11:35\",\n" +
                "\"pic\":\"group1/M00/00/00/wKhlQFs6RCeAY0pHAAJx5ZjNDEM428.jpg\"\n" +
                "}";
        JSONObject jsonObject = JSONObject.parseObject(jsonStr);
        Map<String,Object> map = (Map<String,Object>)jsonObject;

       /* //准备文档数据
        HashMap<String, Object> map = new HashMap<>();
        map.put("name","鸡巴谢");
        map.put("description","鸡巴谢的鸡巴贼大，他一个嘴巴塞不下");
        map.put("studymodel","201001");
        map.put("price",100L);
        map.put("timestamp",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));*/

        //创建索引对象
        IndexRequest indexRequest = new IndexRequest("xc_course","doc");
        //向索引对象插入json数据
        indexRequest.source(map);
        //通过client客户端执行http请求
        IndexResponse indexResponse = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        DocWriteResponse.Result result = indexResponse.getResult();
        System.out.println(result);
    }

    //查询文档
    @Test
    public void testQueryDoc() throws IOException {
        //创建查询请求对象
        GetRequest getRequest = new GetRequest("xc_course","doc","j1VJp3IBKKuVKojV7PH9");
        GetResponse getResponse = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
        Map<String, Object> sourceAsMap = getResponse.getSourceAsMap();
        //jdk8遍历Map
        sourceAsMap.forEach((key, value) -> {
            System.out.println("Key = " + key + "  " + " Value = " + value);
        });
    }

    //删除索引
    @Test
    public void testDelIndex() throws IOException {
        //删除索引对象
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest("xc_xuecheng2");
        deleteIndexRequest.indicesOptions(IndicesOptions.LENIENT_EXPAND_OPEN);
        //操作索引的客户端
        IndicesClient indices = restHighLevelClient.indices();
        //执行删除
        AcknowledgedResponse delete = indices.delete(deleteIndexRequest, RequestOptions.DEFAULT);
        boolean acknowledged = delete.isAcknowledged();
        System.out.println(acknowledged);
    }


}
