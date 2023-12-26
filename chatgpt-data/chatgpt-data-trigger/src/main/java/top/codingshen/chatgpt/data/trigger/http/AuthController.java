package top.codingshen.chatgpt.data.trigger.http;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import top.codingshen.chatgpt.data.domain.auth.model.entity.AuthStateEntity;
import top.codingshen.chatgpt.data.domain.auth.model.valobj.AuthTypeVO;
import top.codingshen.chatgpt.data.domain.auth.service.IAuthService;
import top.codingshen.chatgpt.data.types.common.Constants;
import top.codingshen.chatgpt.data.types.model.Response;

import javax.annotation.Resource;

/**
 * @ClassName AuthController
 * @Description description
 * @Author alex_shen
 * @Date 2023/12/26 - 01:24
 */
@Slf4j
@RestController()
@CrossOrigin("${app.config.cross-origin}")
@RequestMapping("/api/${app.config.api-version}/auth/")
public class AuthController {
    @Resource
    private IAuthService authService;

    @PostMapping("login")
    public Response<String> doLogin(@RequestParam String code) {
        log.info("鉴权登录校验开始，验证码: {}", code);
        try {
            AuthStateEntity authStateEntity = authService.doLogin(code);
            log.info("鉴权登录校验完成,验证码:{},结果:{}", code, JSON.toJSONString(authStateEntity));

            // 鉴权失败 -> 拦截
            if (!authStateEntity.getCode().equals(AuthTypeVO.A0000.getCode())) {
                return Response.<String>builder()
                        .code(Constants.ResponseCode.TOKEN_ERROR.getCode())
                        .info(Constants.ResponseCode.TOKEN_ERROR.getInfo())
                        .build();
            }

            // 鉴权成功 -> 放行
            return Response.<String>builder()
                    .code(Constants.ResponseCode.SUCCESS.getCode())
                    .info(Constants.ResponseCode.SUCCESS.getInfo())
                    .data(authStateEntity.getToken())
                    .build();
        } catch (Exception e) {
            log.error("鉴权登录校验失败,验证码:{}", code);
            return Response.<String>builder()
                    .code(Constants.ResponseCode.UN_ERROR.getCode())
                    .info(Constants.ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }
}
