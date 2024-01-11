package top.codingshen.chatgpt.data.infrastructure.repository;

import org.springframework.stereotype.Repository;
import top.codingshen.chatgpt.data.domain.auth.repository.IAuthRepository;
import top.codingshen.chatgpt.data.infrastructure.redis.IRedisService;

import javax.annotation.Resource;

/**
 * @ClassName AuthRepository
 * @Description description
 * @Author alex_shen
 * @Date 2024/1/12 - 01:33
 */
@Repository
public class AuthRepository implements IAuthRepository {

    private static final String Key = "weixin_code";

    @Resource
    private IRedisService redisService;

    @Override
    public String getCodeUserOpenId(String code) {
        return redisService.getValue(Key + "_" + code);
    }

    @Override
    public void removeCodeByOpenId(String code, String openId) {
        redisService.remove(Key + "_" + code);
        redisService.remove(Key + "_" + openId);
    }
}
