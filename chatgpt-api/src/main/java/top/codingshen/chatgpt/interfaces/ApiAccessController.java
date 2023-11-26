package top.codingshen.chatgpt.interfaces;

import org.junit.Test;
import top.codingshen.chatgpt.domain.security.service.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @ClassName ApiAccessController
 * @Description API 准入验证
 * @Author alex_shen
 * @Date 2023/11/11 - 18:58
 */
@RestController
public class ApiAccessController {
    private Logger logger = LoggerFactory.getLogger(ApiAccessController.class);


    /**
     * localhost:8080/authorize?username=alexshen&password=shen1234
     * 获取授权 token
     *
     * @param username 用户名
     * @param password 密码
     */
    @RequestMapping("/authorize")
    public ResponseEntity<Map<String, String>> authorize(String username, String password) {
        Map<String, String> map = new HashMap<>();

        // 模拟账号和密码校验
        // todo: 加入数据库校验
        if (!(username.equals("alexshen") && password.equals("shen1234"))) {
            map.put("msg", "用户名或密码错误");
            return ResponseEntity.ok(map);
        }

        // 校验通过生成 token
        JwtUtil jwtUtil = new JwtUtil();
        Map<String, Object> claim = new HashMap<>();
        claim.put("username", username);
        String jwtToken = jwtUtil.encode(username, 5 * 60 * 1000, claim);

        map.put("msg", "授权成功");
        map.put("token", jwtToken);

        return ResponseEntity.ok(map);
    }

    /**
     * 来自 http://localhost/api/?token=xxx
     * 通过 Nginx 代理
     * @param token token
     * @return json
     */
    @RequestMapping("/verify")
    public ResponseEntity<String> verify(String token) {
        logger.info("验证 token：{}", token);
        // 经过 shiro 验证

        return ResponseEntity.status(HttpStatus.OK).body("verify success!");
    }

    @RequestMapping("/success")
    public String success() {
        return "test success by alexshen";
    }

}
