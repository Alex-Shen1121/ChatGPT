package top.codingshen.chatgpt.domain.security.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.codec.binary.Base64;

import java.util.*;

/**
 * @ClassName JwtUtil
 * @Description Jwt 工具类
 * @Author alex_shen
 * @Date 2023/11/11 - 20:57
 */
public class JwtUtil {
    private final String base64EncodedSecretKey;
    private static final String defaultBase64EncodedSecretKey = "B*Biui^";

    private final SignatureAlgorithm signatureAlgorithm;
    private static final SignatureAlgorithm defaultSignatureAlgorithm = SignatureAlgorithm.HS256;

    /**
     * 无参构造,使用 default 密钥和签名算法
     */
    public JwtUtil() {
        this(defaultBase64EncodedSecretKey, defaultSignatureAlgorithm);
    }

    public JwtUtil(String secretKey, SignatureAlgorithm signatureAlgorithm) {
        this.base64EncodedSecretKey = Base64.encodeBase64String(secretKey.getBytes());
        this.signatureAlgorithm = signatureAlgorithm;
    }

    /**
     * 产生 jwt 字串串
     * jwt 包含三个部分:
     * 1. Header
     * -当前字符串的类型，一般都是“JWT”
     * -哪种算法加密，“HS256”或者其他的加密算法
     * -所以一般都是固定的，没有什么变化
     * 2. Payload
     * - 一般有四个最常见的标准字段（下面有）
     * - iat：签发时间，也就是这个jwt什么时候生成的
     * - jti：JWT的唯一标识
     * - iss：签发人，一般都是username或者userId
     * - exp：过期时间
     * 3. Signature
     *
     * @param issuer    签发人
     * @param ttlMillis 过期时间
     * @param claims    声明
     * @return JWT 字符串
     */
    public String encode(String issuer, long ttlMillis, Map<String, Object> claims) {
        if (claims == null) {
            claims = new HashMap<>();
        }
        long nowMillis = System.currentTimeMillis();

        JwtBuilder jwtBuilder = Jwts.builder()
                // payload部分

                // jwt 唯一标识
                .setId(UUID.randomUUID().toString())
                // 签发时间
                .setIssuedAt(new Date(nowMillis))
                // 签发人(jwt给谁的), 一般是 username/userid
                .setSubject(issuer)
                // 生成 jwt 使用的算法和密钥
                .signWith(signatureAlgorithm, base64EncodedSecretKey);

        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date date = new Date(expMillis);
            // 设置过期时间
            jwtBuilder.setExpiration(date);
        }

        // 返回 jwt 令牌
        return jwtBuilder.compact();
    }

    // 相当于encode的方向，传入jwtToken生成对应的username和password等字段。Claim就是一个map
    // 也就是拿到荷载部分所有的键值对
    public Claims decode(String jwtToken) {
        // 得到 DefaultJwtParser
        return Jwts.parser()
                // 设置签名的秘钥
                .setSigningKey(base64EncodedSecretKey)
                // 设置需要解析的 jwt
                .parseClaimsJws(jwtToken)
                .getBody();
    }


    public boolean isVerify(String jwtToken) {
        // 这个是官方的校验规则，这里只写了一个”校验算法“，可以自己加
        Algorithm algorithm = null;
        switch (signatureAlgorithm) {
            case HS256:
                algorithm = Algorithm.HMAC256(Base64.decodeBase64(base64EncodedSecretKey));
                break;
            default:
                throw new RuntimeException("不支持该算法");
        }
        JWTVerifier verifier = JWT.require(algorithm).build();
        verifier.verify(jwtToken);
        // 校验不通过会抛出异常
        // 判断合法的标准：1. 头部和荷载部分没有篡改过。2. 没有过期
        return true;

    }
}
