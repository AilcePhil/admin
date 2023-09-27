package com.zzyycc.management.generator.dto;


import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zhuyuechao
 * @version 1.0.0
 * @className MgTablesDTO
 * @createTime 2022/3/16 10:41
 * @description
 */
public class MgTablesDTO implements Serializable {
    private static final long serialVersionUID = -3989225065356824928L;

    @Schema(name = "表名")
    private String tableName;

    @Schema(name = "数据库类型")
    private String dbType;

    @Schema(name = "搜索引擎")
    private String engine;

    @Schema(name = "备注")
    private String tableComment;

    @Schema(name = "创建时间")
    private Date createTime;

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public String getTableComment() {
        return tableComment;
    }

    public void setTableComment(String tableComment) {
        this.tableComment = tableComment;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public String toString() {
        return "MgTablesDTO{" +
                "tableName='" + tableName + '\'' +
                '}';
    }
}
