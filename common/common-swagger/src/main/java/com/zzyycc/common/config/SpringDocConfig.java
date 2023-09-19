package com.zzyycc.common.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhuyuechao
 * @version 1.0.0
 * @className SwaggerConfig
 * @createTime 2022/2/23 15:36
 * @description
 */

@Configuration
public class SpringDocConfig {

    private static final String TOKEN_HEADER = "Authorization";


    @Bean
    public OpenAPI openApi() {
        return new OpenAPI()
            .components(
                new Components()
                    .addSecuritySchemes(TOKEN_HEADER,
                        new SecurityScheme()
                            .type(SecurityScheme.Type.APIKEY)
                            // 这里配置 bearer 后，你的请求里会自动在 token 前加上 Bearer
                            .scheme("bearer")
                            .bearerFormat("JWT"))
                    .addParameters(TOKEN_HEADER,
                        new Parameter()
                            .in("header")
                            .schema(new StringSchema())
                            .name(TOKEN_HEADER)
                    ))
            .info(
                new Info()
                    .title("zzyycc")
                    .description("zzyycc")
                    .contact(new Contact().name("zhuyuechao").email("email").url("url"))
                    // 参考 Apache 2.0 许可及地址，你可以不配此项
                    .license(new License().name("Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0.html"))
                    .version("0.1")
            )
            // 引入外部的文档，我这里引得是 springdoc 官方文档地址，你可以不配此项
            .externalDocs(new ExternalDocumentation()
                .description("SpringDoc Full Documentation")
                .url("https://springdoc.org/")
            );
    }

    /**
     * GroupedOpenApi 是对接口文档分组，类似于 swagger 的 Docket
     *
     */

    @Bean
    public GroupedOpenApi managementApi() {
        return GroupedOpenApi.builder()
            .group("management")
            .packagesToScan("com.zzyycc.management")
            .build();
    }

    @Bean
    public GroupedOpenApi sysApi() {
        return GroupedOpenApi.builder()
            .group("system")
            .packagesToScan("com.zzyycc.sys")
            .build();
    }


}
