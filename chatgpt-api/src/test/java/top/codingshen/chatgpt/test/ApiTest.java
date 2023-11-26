package top.codingshen.chatgpt.test;

import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import top.codingshen.chatgpt.domain.security.service.JwtUtil;
import org.apache.http.impl.client.HttpClientBuilder;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 小傅哥，微信：fustack
 * @description 单元测试
 * @github https://github.com/fuzhengwei
 * @Copyright 公众号：bugstack虫洞栈 | 博客：https://bugstack.cn - 沉淀、分享、成长，让自己和他人都能有所收获！
 */
public class ApiTest {
    @Test
    public void test_jwt() {
        JwtUtil util = new JwtUtil("danke", SignatureAlgorithm.HS256);
        // 以tom作为秘钥，以HS256加密
        Map<String, Object> map = new HashMap<>();
        map.put("username", "alexshen");
        map.put("password", "shen1234");
        map.put("age", 23);

        String jwtToken = util.encode("alex", 30000, map);

        util.decode(jwtToken).forEach((key, value) -> System.out.println(key + ": " + value));
    }

    /**
     * 这是一个简单的测试，后续会开发 ChatGPT API
     * 测试时候，需要先获得授权token
     * 获取方式；http://api.xfg.im:8080/authorize?username=xfg&password=123 - 此地址暂时有效，后续根据课程首页说明获取token；https://t.zsxq.com/0d3o5FKvc
     */
    @Test
    public void test_chatGPT() throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // 用获取的 token 替换，默认有效期60分钟。地址非长期有效，只做学习验证。如果api.xfg.im链接已不存在，一种是可以在aws服务部署个自己的。另外是继续往下走就可以不耽误课程学习。
        HttpPost post = new HttpPost("https://api.xfg.im/b8b6/v1/completions?token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ4ZmciLCJleHAiOjE2ODMyMDQzMzYsImlhdCI6MTY4MzIwMDczNiwianRpIjoiYjM2Njg3ZjgtOWM5Yi00NzE1LWI2ZjctYjM0YmEyNzE2MWE3IiwidXNlcm5hbWUiOiJ4ZmcifQ.qBskmFVqx_0CKXdhtuSpqWn6XqB5Qq1Qu-c6_4-UoDg");

        post.addHeader("Content-Type", "application/json");
        post.addHeader("Authorization", "Bearer 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");

        String paramJson = "{\"model\": \"text-davinci-003\", \"prompt\": \"帮我写一个java冒泡排序\", \"temperature\": 0, \"max_tokens\": 1024}";

        StringEntity stringEntity = new StringEntity(paramJson, ContentType.create("text/json", "UTF-8"));
        post.setEntity(stringEntity);

        System.out.println("请求入参：" + paramJson);
        CloseableHttpResponse response = httpClient.execute(post);
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            String res = EntityUtils.toString(response.getEntity());
            // api.xfg.im:443 requested authentication 表示 token 错误或者过期。
            System.out.println("测试结果：" + res);
        } else {
            System.out.println(response.getStatusLine().getStatusCode());
        }

    }

}
