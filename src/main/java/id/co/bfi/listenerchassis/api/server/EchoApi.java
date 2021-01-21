package id.co.bfi.listenerchassis.api.server;

import java.io.IOException;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EchoApi {

	@RequestMapping(value = "/echo")
	public String echo(HttpServletRequest request) throws IOException {
		var sb = new StringBuilder();

		sb.append("Parameter Map\n");
		request.getParameterMap().forEach((k, v) -> sb.append(k + ":" + v + "\n"));
		sb.append("\n\nHeaders");
		request.getHeaderNames().asIterator()
				.forEachRemaining(h -> sb.append(h + " : " + request.getHeaders(h) + "\n"));
		sb.append("\n\nQuery string\n");
		sb.append(request.getQueryString());
		sb.append("\n\n\nVerb\n");
		sb.append(request.getMethod());
		sb.append("\n\n\nBody\n");
		var body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
		sb.append(body);

		sb.append("\n\n\nini-content\n");
		sb.append(request.getHeader("ini-content"));

		sb.append("\n\n\nContent-Type\n");
		sb.append(request.getHeader("Content-Type"));

		sb.append("\n\n\napikey\n");
		sb.append(request.getHeader("apikey"));

		sb.append("\n\n\nxxx\n");
		sb.append(request.getHeader("Content-Length"));

		System.out.println(sb.toString());

		return sb.toString();
	}
}
