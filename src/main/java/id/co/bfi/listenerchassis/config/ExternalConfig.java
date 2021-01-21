package id.co.bfi.listenerchassis.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@PropertySources({ @PropertySource(factory = YamlPropertySourceFactory.class, value = { "classpath:appsettings.yml",
		"file:/conf/appsettings.yml", "file:c:/conf/appsettings.yml" }, ignoreResourceNotFound = true) })
public class ExternalConfig {
}
