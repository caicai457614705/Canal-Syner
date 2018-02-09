package com.faker.canal.component.es;

import com.alibaba.fastjson.JSONObject;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * Created by faker on 18/2/9.
 */
public class EsAPI {

    private final static Logger logger = LoggerFactory.getLogger(EsAPI.class);

    private TransportClient client;

    private String index;

    public EsAPI(TransportClient transportClient, String index) {
        Assert.notNull(transportClient);
        Assert.notNull(index);
        this.client = transportClient;
        this.index = index;
    }

    public int put(String type, JSONObject data, String id) {
        return putDataToEs(type, data, id);
    }

    public int put(String type, JSONObject data) {
        return putDataToEs(type, data, null);
    }

    public int deleteById(String type, String id) {
        try {
            DeleteResponse response = client.prepareDelete(index, type, id)
                    .get();
            int status = response.status().getStatus();
            if (status == 1) {
                return status;
            } else {
                logger.error("delete data in elasticsearch failed , index: {} , type: {} , id : {} , status : {} ,", index, type, id, status);
            }
        } catch (Exception e) {
            logger.error("delete data in elasticsearch error, index: {} , type: {} , id : {} ", index, type, id, e);
        }
        return -1;

    }

    public int update(String type, String id, JSONObject updataJson) {
        try {
            XContentBuilder xBuilder = XContentFactory.jsonBuilder().startObject();
            for (String key : updataJson.keySet()) {
                xBuilder.field(key, updataJson.get(key));
            }
            UpdateRequest updateRequest = new UpdateRequest(index, type, id)
                    .upsert(xBuilder.endObject());


            UpdateResponse response = client.update(updateRequest).get();

            int status = response.status().getStatus();
            if (status == 1) {
                return status;
            } else {
                logger.error("updata data in elasticsearch failed , index: {} , type: {} , id : {} , status : {} ,data : {} ", index, type, id, status, updataJson);
            }
        } catch (Exception e) {
            logger.error("updata data in elasticsearch error, index: {} , type: {} , id : {} ,data : {}", index, type, id, updataJson, e);
        }
        return -1;

    }


    private int putDataToEs(String type, JSONObject data, String id) {
        try {
            IndexResponse response;
            if (id != null) {
                response = client.prepareIndex(index, type, id)
                        .setSource(data).execute().actionGet();
            } else {
                response = client.prepareIndex(index, type)
                        .setSource(data).execute().actionGet();
            }

            int status = response.status().getStatus();
            if (status == 1) {
                return status;
            } else {
                logger.error("put data to elasticsearch failed , index: {} , type: {} , status : {} , data : {} ", index, type, status, JSONObject.toJSONString(data));
            }
        } catch (Exception e) {
            logger.error("put data to elasticsearch error index: {} , type: {} ,  data : {} ", index, type, JSONObject.toJSONString(data), e);
        }
        return -1;
    }
}
