package com.zzyycc.management.generator.dto;


import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhuyuechao
 * @version 1.0.0
 * @className MgGeneratorCodeDTO
 * @createTime 2022/2/22 14:13
 * @description
 */
public class MgGeneratorCodeDTO implements Serializable {
    private static final long serialVersionUID = 2819433445126313250L;

    @Schema(name = "链接路径")
    private String url;

    @Schema(name = "用户名")
    private String username;

    @Schema(name = "密码")
    private String password;

    @Schema(name = "数据库名称")
    private String databaseName;

    @Schema(name = "表名列表")
    private List<String> tableNameList;

    @Schema(name = "作者名")
    private String author;

    @Schema(name = "父包名")
    private String parent;

    @Schema(name = "模块名")
    private String moduleName;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public List<String> getTableNameList() {
        return tableNameList;
    }

    public void setTableNameList(List<String> tableNameList) {
        this.tableNameList = tableNameList;
    }

    @Override
    public String toString() {
        return "MgGeneratorCodeDTO{" +
                "databaseName='" + databaseName + '\'' +
                ", tableNameList=" + tableNameList +
                ", author='" + author + '\'' +
                ", parent='" + parent + '\'' +
                ", moduleName='" + moduleName + '\'' +
                '}';
    }
}
