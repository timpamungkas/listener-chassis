package id.co.bfi.listenerchassis.util;

import net.logstash.logback.encoder.org.apache.commons.lang3.StringUtils;

public class BasicAuthUtil {

	public static String basicAuthString(String username, String password) {
		var authString = EncodeDecodeUtil.encodeBase64(StringUtils.join(username, ":", password));
		return StringUtils.join("Basic ", authString);
	}

}
