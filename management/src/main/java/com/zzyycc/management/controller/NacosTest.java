package com.zzyycc.management.controller;

import com.zzyycc.management.service.NacosService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Tag(name = "test", description = "test")
@RestController
@RequestMapping("/nacos")
public class NacosTest {

    @Resource
    private NacosService nacosService;

    @Value("${zzyycc.name}")
    private String name;

    @Value("${zzyycc.age}")
    private String age;

    @Operation(summary = "查询")
    @GetMapping("/info")
    public String getApplicationInfo() {
        return nacosService.getApplicationInfo(name + ":" + age);
    }
}
