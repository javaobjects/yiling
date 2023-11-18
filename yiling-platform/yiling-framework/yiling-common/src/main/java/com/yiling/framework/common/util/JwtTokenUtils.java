package com.yiling.framework.common.util;

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.yiling.framework.common.pojo.bo.JwtDataModel;

import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;

/**
 * JwtToken生成的工具类
 * JWT token的格式：header.payload.signature
 * header的格式（算法、token的类型）：
 * {"alg": "HS512","typ": "JWT"}
 * payload的格式（用户名、创建时间、生成时间）：
 * {"sub":"wang","created":1489079981393,"exp":1489684781}
 * signature的生成算法：
 * HMACSHA512(base64UrlEncode(header) + "." + base64UrlEncode(payload),secret)
 */
@Slf4j
@Component
public class JwtTokenUtils {

    private static final Logger logger             = LoggerFactory.getLogger(JwtTokenUtils.class);

    private static final String CLAIM_KEY_USERID  = "sub";
    private static final String CLAIM_KEY_CREATED = "created";

    @Value("${jwt.secret}")
    private String              secret;
    @Value("${jwt.expiration}")
    private Long                expiration;

    /**
     * 根据负责生成JWT的token
     */
    private String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 从token中获取JWT中的负载
     */
    private Claims getClaimsFromToken(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (UnsupportedJwtException unsupportedJwtException) {
            logger.error("The claimsJws argument does not represent an Claims JWS. token={}", token);
        } catch (MalformedJwtException malformedJwtException) {
            logger.error("The claimsJws string is not a valid JWS. token={}", token);
        } catch (SignatureException signatureException) {
            logger.error("The claimsJws JWS signature validation fails. token={}", token);
        } catch (ExpiredJwtException expiredJwtException) {
            logger.debug("The specified JWT is a Claims JWT and the Claims has an expiration time before the time this method is invoked. token={}", token);
        } catch (IllegalArgumentException illegalArgumentException) {
            logger.error("The claimsJws string is null or empty or only whitespace. token={}", token);
        }
        return claims;
    }

    /**
     * 生成token的过期时间
     */
    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    /**
     * 从token中获取用户信息
     *
     * @param token
     * @return
     */
    public JwtDataModel getDataFromToken(String token) {
        Claims claims = this.getClaimsFromToken(token);
        if (claims == null) {
            return null;
        }
        String subject = claims.getSubject();
        if (JSONUtil.isJson(subject)) {
            JwtDataModel jwtDataModel = JSONUtil.toBean(subject, JwtDataModel.class);
            jwtDataModel.setExpiration(claims.getExpiration());
            return jwtDataModel;
        } else {
            log.error("subject内容不是JSON格式:{}", subject);
        }

        return null;
    }

    /**
     * 生成 token
     *
     * @param data
     * @return
     */
    public String generateToken(JwtDataModel data) {
        Map<String, Object> claims = MapUtil.newHashMap();
        claims.put(CLAIM_KEY_USERID, JSONUtil.toJsonStr(data));
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }
}
