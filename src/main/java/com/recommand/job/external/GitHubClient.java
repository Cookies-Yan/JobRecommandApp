package com.recommand.job.external;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recommand.job.entity.Item;
import org.apache.http.HttpEntity;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

public class GitHubClient {
    private static final String URL_TEMPLATE = "https://jobs.github.com/positions.json?description=%s&lat=%s&long=%s";
    private static final String DEFAULT_KEYWORD = "developer";

    /**
     * 关于HttpClient使用：
     * https://orchidflower.gitee.io/2018/04/23/Apache-HttpClient-Manual/
     * https://www.yiibai.com/apache_httpclient/apache_httpclient_response_handlers.html
     */
    public List<Item> search(double lat, double lon, String keyword) {
        if (keyword == null) {
            keyword = DEFAULT_KEYWORD;
        }
        // eddy yang => eddy%20, eddy+yang
        try {
            keyword = URLEncoder.encode(keyword, "UTF-8"); //普通字符转MIME字符
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = String.format(URL_TEMPLATE, keyword, lat, lon); //填充通配符

        CloseableHttpClient httpClient = HttpClients.createDefault();
        ResponseHandler<List<Item>> responseHandler = response -> {  //跳转到② //response 是Apache call的。 response也是它给的
            if (response.getStatusLine().getStatusCode() != 200) {
                return Collections.emptyList();
            }
            HttpEntity entity = response.getEntity(); // 获取响应的实体内容，就是我们所要抓取得网页内容
//            System.out.println(entity);
            if (entity == null) {
                return Collections.emptyList(); //final属性，不可往list里面添加元素
            }
            ObjectMapper mapper = new ObjectMapper();
//            InputStream inputStream = entity.getContent();
            List<Item> items = Arrays.asList(mapper.readValue(entity.getContent(), Item[].class)); //Item[] 是因为拿到数据JSON中是Array
            extractKeywords(items);
            return items;
//            Item[] items = mapper.readValue(inputStream, Item[].class); //文字转class
//            return Arrays.asList(items);  //EntityUtils.toString(entity);
        };

        try {  //②
//            System.out.println(httpClient.execute(new HttpGet(url), responseHandler));
            return httpClient.execute(new HttpGet(url), responseHandler); // 比较高级，call的什么类型，返回什么类型
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList(); //new ArrayList<>();
    }

    private void extractKeywords(List<Item> items) {
        MonkeyLearnClient monkeyLearnClient = new MonkeyLearnClient();

        List<String> descriptions = items.stream()
                .map(Item::getDescription) // 一次性全部map上，并拿到结果
                .collect(Collectors.toList()); // 把结果转换成list

        List<Set<String>> keywordList = monkeyLearnClient.extract(descriptions);
        for (int i = 0; i < items.size(); i++) {
            if (items.size() != keywordList.size() && i > keywordList.size() - 1) {
                items.get(i).setKeywords(new HashSet<>());
                continue;
            }
            items.get(i).setKeywords(keywordList.get(i));
        }
    }
}
