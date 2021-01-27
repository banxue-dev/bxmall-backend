package cn.enilu.flash.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Value("${uploadFile.webImgPath}")
    private String webImgPath;
    @Value("${uploadFile.hostPath}")
    private String hostPath;
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(webImgPath).addResourceLocations("file:" + hostPath);
        //上传的图片在D盘下的OTA目录下，访问路径如：http://localhost:8080/xxx.jpg
    }

}
