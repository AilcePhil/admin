package com.zzyycc.management.service;

import org.springframework.stereotype.Component;

@Component
public class NacosServiceImpl implements NacosService {
    @Override
    public String getApplicationInfo(String s) {
        return s;
    }
}
