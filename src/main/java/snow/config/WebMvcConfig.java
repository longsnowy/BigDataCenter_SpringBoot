package snow.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


@Configuration

public class WebMvcConfig extends WebMvcConfigurationSupport {

    @Override

    public void addResourceHandlers(ResourceHandlerRegistry registry) {

//        String path = "E:\\Practices\\";//"/tools/images/";//"E:/视频制作/图片/";
//
//// 上传路径映射 会使spring boot的自动配置失效

//        registry.addResourceHandler("/image/**").addResourceLocations("file:" + path);

        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");

        registry.addResourceHandler("/upload/**").addResourceLocations("file:/home/g2431/upload/");

        super.addResourceHandlers(registry);

    }

}
