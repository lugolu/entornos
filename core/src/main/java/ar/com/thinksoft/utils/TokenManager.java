package ar.com.thinksoft.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator.Builder;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.itextpdf.text.pdf.codec.Base64;



public class TokenManager {
	private static TokenManager instance = new TokenManager();
	public static TokenManager getInstance() { return instance; }
	//
	private RSAPrivateKey PRIVATE_KEY;
	private RSAPublicKey PUBLIC_KEY;

	private TokenManager() {
		PRIVATE_KEY = (RSAPrivateKey) loadPrivateKey();
		PUBLIC_KEY = (RSAPublicKey) loadPublicKey();
	}

	private PrivateKey loadPrivateKey() {
		PrivateKey privateKey = null;
		try {
			InputStream inputStream = TokenManager.class.getResourceAsStream("/keys/private_key.ppk");
			InputStreamReader isReader = new InputStreamReader(inputStream);
			BufferedReader reader = new BufferedReader(isReader);
			StringBuffer sb = new StringBuffer();
			String str;
			while((str = reader.readLine())!= null){
				sb.append(str);
			}
			String keyStr = sb.toString();
			//String keyStr = new String(Files.readAllBytes(Paths.get("keys/private_key.ppk")));
			keyStr = keyStr.replace("-----BEGIN PRIVATE KEY-----", "");
			keyStr = keyStr.replace("-----END PRIVATE KEY-----", "");
			keyStr = keyStr.replaceAll("\\s+","");
			byte[] decoded = Base64.decode(keyStr);
			PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
			privateKey = KeyFactory.getInstance("RSA").generatePrivate(spec);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return privateKey;
	}

	private PublicKey loadPublicKey() {
		PublicKey publlicKey = null;
		try {
			InputStream inputStream = TokenManager.class.getResourceAsStream("/keys/public_key.pub");
			InputStreamReader isReader = new InputStreamReader(inputStream);
			BufferedReader reader = new BufferedReader(isReader);
			StringBuffer sb = new StringBuffer();
			String str;
			while((str = reader.readLine())!= null){
				sb.append(str);
			}
			String keyStr = sb.toString();
			//String keyStr = new String(Files.readAllBytes(Paths.get("src/keys/public_key.pub")));
			keyStr = keyStr.replace("-----BEGIN PUBLIC KEY-----", "");
			keyStr = keyStr.replace("-----END PUBLIC KEY-----", "");
			keyStr = keyStr.replaceAll("\\s+","");
			byte[] decoded = Base64.decode(keyStr);
			X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
			publlicKey = KeyFactory.getInstance("RSA").generatePublic(spec);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return publlicKey;
	}

	@SuppressWarnings("deprecation")
	public String createJWT(Map<String, Object> payload) {
		Builder b = JWT.create();
		for(Entry<String, Object> entry : payload.entrySet()) {
			if(entry.getValue() instanceof String) {
				b.withClaim(entry.getKey(), (String) entry.getValue());
			} else if(entry.getValue() instanceof Long) {
				b.withClaim(entry.getKey(), (Long) entry.getValue());
			} else if(entry.getValue() instanceof Double) {
				b.withClaim(entry.getKey(), (Double) entry.getValue());
			} else if(entry.getValue() instanceof Date) {
				b.withClaim(entry.getKey(), (Date) entry.getValue());
			}
		}
		return b.sign(Algorithm.RSA256(PRIVATE_KEY));
	}

	@SuppressWarnings("deprecation")
	public DecodedJWT verifyToken(String token) throws Exception {
		Algorithm algorithm = Algorithm.RSA256(PUBLIC_KEY);
		return JWT.require(algorithm).build().verify(token);
	}

	public DecodedJWT decodeToken(String token) throws Exception {
		return JWT.decode(token);
	}

	public static void main(String[] args) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("tipoToken", Constants.STRING_ENCUESTA_ALTA);
			map.put("idAtencionAmb", 48445l);
			String token = TokenManager.getInstance().createJWT(map);
			System.out.println(token);

			DecodedJWT decoded = TokenManager.getInstance().decodeToken(token);
			System.out.println(Base64.decode(decoded.getPayload()));
			System.out.println(decoded.getClaim("idAtencionAmb").asLong());
			System.out.println(decoded.getClaim("idAtencssionAmb").asLong());
		} catch(Exception ex) {

		}
	}

}
