package com.recommand.job.external;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.recommand.job.entity.ExtractRequestBody;
import com.recommand.job.entity.ExtractResponseItem;
import com.recommand.job.entity.Extraction;
import org.apache.http.HttpEntity;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

public class MonkeyLearnClient {
    private static final String EXTRACT_URL = "https://api.monkeylearn.com/v3/extractors/ex_YCya9nrn/extract/";
    private static final String AUTH_TOKEN = "0dbba12ccc58cea104f0a34b7c065f1685ac24ee";

    public List<Set<String>> extract(List<String> articles) { //post 进来的data在数组里
        ObjectMapper mapper = new ObjectMapper();
        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpPost request = new HttpPost(EXTRACT_URL);
        request.setHeader("Content-type", "application/json");
        request.setHeader("Authorization", "Token " + AUTH_TOKEN);
        ExtractRequestBody body = new ExtractRequestBody(articles, 3);

        String jsonBody;
        try {
            jsonBody = mapper.writeValueAsString(body);
//            System.out.println(jsonBody);
        } catch (JsonProcessingException e) {
            return Collections.emptyList();
        }

        try {
            request.setEntity(new StringEntity(jsonBody));
//            System.out.println(request.getEntity());
//            System.out.println(Arrays.toString(request.getAllHeaders()));
        } catch (UnsupportedEncodingException e) {
            return Collections.emptyList();
        }

        ResponseHandler<List<Set<String>>> responseHandler = response -> { //option + Enter
            if (response.getStatusLine().getStatusCode() != 200) { //MonkeyLearn的API给的是200，没有其他数值
                return Collections.emptyList();
            }
            HttpEntity entity = response.getEntity();
            if (entity == null) {
                return Collections.emptyList();
            }
//            System.out.println(Arrays.toString(mapper.readValue(entity.getContent(), ExtractResponseItem[].class)));
            ExtractResponseItem[] results = mapper.readValue(entity.getContent(), ExtractResponseItem[].class); // 最终的数据结构是数组
            List<Set<String>> keywordList = new ArrayList<>();
            for (ExtractResponseItem result : results) {
                Set<String> keywords = new HashSet<>();
                for (Extraction extraction : result.extractions) {
                    keywords.add(extraction.parsedValue);
                }
                keywordList.add(keywords);
            }
            return keywordList;
        };

        try {
            return httpclient.execute(request, responseHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public static void main(String[] arg) {
        List<String> articles = Arrays.asList(
                "Elon Musk has shared a photo of the spacesuit designed by SpaceX. This is the second image shared of the new design and the first to feature the spacesuit’s full-body look.",
                "Former Auburn University football coach Tommy Tuberville defeated ex-US Attorney General Jeff Sessions in Tuesday nights runoff for the Republican nomination for the U.S. Senate. ",
                "The NEOWISE comet has been delighting skygazers around the world this month – with photographers turning their lenses upward and capturing it above landmarks across the Northern Hemisphere."
        );

        MonkeyLearnClient client = new MonkeyLearnClient();
        List<Set<String>> keywordList = client.extract(articles);
        System.out.println(keywordList);
    }
}