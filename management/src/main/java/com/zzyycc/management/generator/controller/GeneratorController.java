package com.zzyycc.management.generator.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzyycc.common.core.utils.ResponseData;
import com.zzyycc.management.generator.dto.MgGeneratorCodeDTO;
import com.zzyycc.management.generator.dto.MgTablesDTO;
import com.zzyycc.management.generator.service.GeneratorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * @author zhuyuechao
 * @version 1.0.0
 * @className Generator
 * @createTime 2022/2/21 14:31
 * @description
 */
@RestController
@RequestMapping("/generator")
@Tag(name = "代码生成")
public class GeneratorController {

    private final GeneratorService generatorService;

    public GeneratorController(GeneratorService generatorService) {
        this.generatorService = generatorService;
    }

    @Operation(summary = "代码生成")
    @PostMapping("/create/code")
    public ResponseData<Void> generatorCode(@RequestBody MgGeneratorCodeDTO dto) {
        generatorService.generatorCode(dto);
        return ResponseData.success();
    }

    @Operation(summary = "下载生成")
    @PostMapping("/download/code")
    public ResponseData<Void> downloadCode(@RequestBody MgGeneratorCodeDTO dto, HttpServletResponse response) {
        generatorService.downloadCode(dto, response);
        return ResponseData.success();
    }


    @Operation(summary = "获取表相关信息")
    @PostMapping("/tables")
    public ResponseData<Page<MgTablesDTO>> tables(Page page, @RequestBody MgTablesDTO dto) {
        return ResponseData.success(generatorService.tables(page, dto));
    }



}
