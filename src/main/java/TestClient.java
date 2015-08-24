import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * Created by ashishn on 8/23/15.
 */


public class TestClient {
    static String json = "{" +
            "\"user\":\"kimchy\"," +
            "\"postDate\":\"2013-01-30\"," +
            "\"message\":\"trying out Elasticsearch\"" +
            "}";


    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        Client client = new TransportClient()
                .addTransportAddress(new InetSocketTransportAddress("localhost", 9300));



        final IndexResponse response = client.prepareIndex("twitter", "tweet", "1")
                .setSource(json)
                .execute()
                .actionGet();

        System.out.println("created:" + response.isCreated());






        GetResponse response1 = client.prepareGet("twitter", "tweet", "1")
                .execute()
                .actionGet();

        System.out.println(response1.getSource().get("user"));


        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.index("twitter");
        updateRequest.type("tweet");
        updateRequest.id("1");
        updateRequest.doc(jsonBuilder()
                .startObject()
                .field("gender", "male")
                .endObject());

        client.update(updateRequest).get();




        System.out.println(response1.getType());

        IndicesExistsRequest indicesExistsRequest = new IndicesExistsRequest("twitter");


        System.out.println("exists:" + client.admin().indices().exists(indicesExistsRequest).actionGet().isExists());



        System.out.println(response1.getField("gender"));


    }
}
