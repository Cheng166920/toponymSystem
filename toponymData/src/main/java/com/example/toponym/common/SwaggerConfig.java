package com.example.toponym.common;
//
//import io.swagger.v3.oas.annotations.OpenAPIDefinition;
//import io.swagger.v3.oas.annotations.info.Info;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import springfox.documentation.builders.ApiInfoBuilder;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.service.Contact;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//
//import java.util.ArrayList;
//
////集合swagger3接口文档,可用于测试接口
////@EnableOpenApi
//@Configuration
////@OpenAPIDefinition(
////        info = @Info(
////                title = "荆州地名地址系统API",
////                version = "1.0",
////                description = "荆州地名地址系统API"
////        )
////)
//public class SwaggerConfig {
//    @Bean
//    public Docket createRestApi() {
//        return new Docket(DocumentationType.OAS_30)  // 指定swagger3.0版本
//                .apiInfo(createApiInfo());
//    }
//
//    /**
//     * 配置swagger的ApiInfo bean
//     * @return
//     */
//    @Bean
//    public ApiInfo createApiInfo(){
//        return new ApiInfo("Java1234 Swagger"
//                ,"Java1234 Api Documentation"
//                ,"3.0"
//                ,"http://www.java1234.vip"
//                ,new Contact("小锋", "http://www.java1234.vip", "caofeng2012@126.com")
//                ,"Apache 2.0"
//                ,"http://www.apache.org/licenses/LICENSE-2.0"
//                ,new ArrayList());
//    }
//}
import com.example.toponym.service.impl.ExceptionEnum;
import io.netty.util.internal.StringUtil;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.apache.coyote.Response;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI springShopOpenAPI() {
//        List<Response> responses = new ArrayList<>();
//        for(ExceptionEnum value: ExceptionEnum.values()){
//            String message = value.getResultMsg();
//            String code = value.getResultCode();
//            if (StringUtil.isNullOrEmpty(code)){
//                message = value.getResultMsg() + " " + value.getResultMsg();
//            }
//            responses.add(new ResponseMessageBuilder().code(errorEnums.getCode()).message(errorEnums.getMsg()).responseModel(
//                    new ModelRef(errorEnums.getMsg())).build()
//            );
//        }

        return new OpenAPI()
                .info(new Info().title("荆州地名地址系统API")
                        .description("荆州地名地址系统API文档")
                        .version("v1"));
    }

}
