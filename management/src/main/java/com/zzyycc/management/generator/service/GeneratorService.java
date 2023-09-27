package com.zzyycc.management.generator.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzyycc.management.generator.dto.MgGeneratorCodeDTO;
import com.zzyycc.management.generator.dto.MgTablesDTO;


import javax.servlet.http.HttpServletResponse;

/**
 * @author zhuyuechao
 * @version 1.0.0
 * @className GeneratorService
 * @createTime 2022/2/22 14:24
 * @description
 */
public interface GeneratorService {

    /**
     * 生成代码
     *
     * @date 2022/2/22 14:26
     * @param dto 入参
     */
    void generatorCode(MgGeneratorCodeDTO dto);

    /**
     * 下载代码
     *
     * @param dto 入参
     * @param response 响应
     * @date 2022/2/25 14:23
     */
    void downloadCode(MgGeneratorCodeDTO dto, HttpServletResponse response);

    /**
     * 分页查询表信息
     * @param page 分页
     * @param dto 入参
     * @return MgTablesDTO
     */
    Page<MgTablesDTO> tables(Page page, MgTablesDTO dto);
}
