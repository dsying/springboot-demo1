package hello.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource({"classpath*:applicationContext.xml"}) // applicationContext.xml will be imported from classpath.
public class XmlConfiguration {
}
