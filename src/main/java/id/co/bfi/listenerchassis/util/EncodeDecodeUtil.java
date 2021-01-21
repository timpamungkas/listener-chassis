package id.co.bfi.listenerchassis.util;

import java.nio.charset.StandardCharsets;

import org.springframework.util.Base64Utils;
import org.springframework.web.util.UriUtils;

public class EncodeDecodeUtil {

	public static String encodeBase64(String original) {
		return Base64Utils.encodeToString(original.getBytes(StandardCharsets.UTF_8));
	}

	public static String decodeBase64(String encoded) {
		return new String(Base64Utils.decodeFromString(encoded), StandardCharsets.UTF_8);
	}

	public static String encodeUrl(String original) {
		return UriUtils.encode(original, StandardCharsets.UTF_8);
	}

	public static String decodeUrl(String encoded) {
		return UriUtils.decode(encoded, StandardCharsets.UTF_8);
	}

}
