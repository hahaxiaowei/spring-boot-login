package com.huntkey.rx.sceo.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.huntkey.rx.commons.utils.eco.EcoSystemUtil;
import com.huntkey.rx.edm.entity.DepttreeEntity;
import com.huntkey.rx.edm.entity.EnterpriseEntity;
import com.huntkey.rx.edm.entity.JobpositionEntity;
import com.huntkey.rx.edm.entity.PeopleEntity;
import com.huntkey.rx.sceo.common.constant.Constant;
import com.huntkey.rx.sceo.common.entity.JobpositionVo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@Component
public class JwtUtil {
	/**
	 * 由字符串生成加密key
	 * @return
	 */
	public static SecretKey generalKey(){
		String stringKey =  Constant.JWT_SECRET;
		byte[] encodedKey = Base64.decodeBase64(stringKey);
	    SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
	    return key;
	}

	/**
	 * 创建jwt
	 * @param id
	 * @param subject
	 * @param ttlMillis
	 * @return
	 * @throws Exception
	 */
	public static String createJWT(String id, JSONObject subject, long ttlMillis) throws Exception {
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);
		SecretKey key = generalKey();
		JwtBuilder builder = Jwts.builder()
				.setHeaderParam("typ", "JWT")
				.setHeaderParam("alg", "HS256")
				.setIssuedAt(now)
			.claim("sub",subject)
		    .signWith(signatureAlgorithm, key);
		//添加Token过期时间
		if (ttlMillis >= 0) {
		    long expMillis = nowMillis + ttlMillis;
		    Date exp = new Date(expMillis);
		    builder.setExpiration(exp);
		}
		return builder.compact();
	}

	/**
	 * 解密jwt
	 * @param jwt
	 * @return
	 * @throws Exception
	 */
	public Claims parseJWT(String jwt){
		try{
			SecretKey key = generalKey();
			Claims claims = Jwts.parser()
					.setSigningKey(key)
					.parseClaimsJws(jwt).getBody();
			return claims;
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * 生成subject信息
	 * @param
	 * @return
	 */
	public static JSONObject generalSubject(PeopleEntity peopleEntity){
		JSONObject jo = new JSONObject();
		jo.put("id",peopleEntity.getId());
		jo.put("epeo_code",peopleEntity.getEpeo_code());
		jo.put("epeo_tel", peopleEntity.getEpeo_tel());
		jo.put("epeo_mail",peopleEntity.getEpeo_mail());
		jo.put("epeo_name_en",peopleEntity.getEpeo_name_en());
		jo.put("epeo_name_cn",peopleEntity.getEpeo_name_cn());
		jo.put("epeo_name_ni",peopleEntity.getEpeo_name_ni());
		jo.put("epeo_fist_name",peopleEntity.getEpeo_fist_name());
		jo.put("epeo_last_name",peopleEntity.getEpeo_last_name());
		jo.put("epeo_gender",peopleEntity.getEpeo_gender());
		jo.put("epeo_card_no",peopleEntity.getEpeo_card_no());
		return jo;
	}

	public static String getUserId(ServletRequest request){
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String auth = httpRequest.getHeader("Authorization");
		Map<String, String> usermap = new HashMap<String, String>();
		String userId = "";
		if (auth != null) {
			if (EcoSystemUtil.parseJWT(auth) != null) {
				usermap = EcoSystemUtil.ifAuthUsable(auth);
				userId = usermap.get("id");
			}
		}
		return  userId;
	}
}
