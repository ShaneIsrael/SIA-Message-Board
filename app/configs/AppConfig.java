package configs;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

@Configuration
@ComponentScan({"controllers","services"})
public class AppConfig {

	@Bean
    public static PropertyPlaceholderConfigurer placeholderConfigurer() {
        return new PropertyPlaceholderConfigurer() {
            @Override
            protected String resolvePlaceholder(String placeholder, Properties props) {
                Properties p = new Properties();
                try {
                    p.load(FileUtils.openInputStream(new File("conf/application.conf")));
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                // When property is not found, returning null will cause an IllegalArgumentException stating the parameter that is
                // missing in @Value.
                String result = p.getProperty(placeholder);
                if (result == null) {
                    result = "";
                }
                return result.replaceAll("\"", "");
            }
        };
    }
}