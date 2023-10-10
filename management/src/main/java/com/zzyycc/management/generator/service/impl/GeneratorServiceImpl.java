package com.zzyycc.management.generator.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.ConstVal;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.keywords.MySqlKeyWordsHandler;
import com.zzyycc.management.generator.dto.MgGeneratorCodeDTO;
import com.zzyycc.management.generator.dto.MgTablesDTO;
import com.zzyycc.management.generator.service.GeneratorService;
import com.zzyycc.management.utils.ZipUtil;
import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhuyuechao
 * @version 1.0.0
 * @className GeneratorServiceImpl
 * @createTime 2022/2/22 14:25
 * @description
 */
@Component
public class GeneratorServiceImpl implements GeneratorService {

    @Value("${spring.datasource.username}")
    private String defaultUsername;
    @Value("${spring.datasource.password}")
    private String defaultPassword;
    @Value("${spring.datasource.url}")
    private String defaultUrl;

    private final StringRedisTemplate stringRedisTemplate;

    private final JdbcTemplate jdbcTemplate;

    public GeneratorServiceImpl(StringRedisTemplate stringRedisTemplate, JdbcTemplate jdbcTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void generatorCode(MgGeneratorCodeDTO dto) {
        fastAutoGenerator(dto).execute();
    }

    @Override
    public void downloadCode(MgGeneratorCodeDTO dto, HttpServletResponse response) {
        this.generatorCode(dto);
        // 读取文件
        String path = this.getSystemPath();
        ZipUtil.toZip(response, new File(path));
    }

    @Override
    public Page<MgTablesDTO> tables(Page page, MgTablesDTO dto) {
        Page<MgTablesDTO> resultPage = new Page<>();

        String sql ="SELECT 'mysql' AS dbType, table_name AS tableName, ENGINE AS engine, table_comment AS tableComment, create_time AS createTime " +
                "FROM information_schema.TABLES WHERE table_schema = ( SELECT DATABASE()) " ;

        if (StringUtils.isNotEmpty(dto.getTableName())) {
            sql += " and table_name like '%"+dto.getTableName()+"%' ";
        }
        sql += " order by create_time desc ";

        // 分页查询
        long current = page.getCurrent();
        long size = page.getSize();
        sql += " limit "+ (current-1)*size +", " + size ;

        List<MgTablesDTO> resultList = this.jdbcTemplate.query(sql, (ResultSet rs) -> {
            List<MgTablesDTO> tempList = new ArrayList<>();
            while (rs.next()) {
                MgTablesDTO result = new MgTablesDTO();
                result.setDbType(rs.getString("dbType"));
                result.setTableName(rs.getString("tableName"));
                result.setTableComment(rs.getString("tableComment"));
                result.setCreateTime(rs.getTimestamp("createTime"));
                result.setEngine(rs.getString("engine"));
                tempList.add(result);
            }
            return tempList;
        });

        // 查询总条数
        String countSql ="SELECT count(1) FROM information_schema.TABLES WHERE table_schema = ( SELECT DATABASE())" ;
        Integer count = this.jdbcTemplate.queryForObject(countSql, Integer.class);
        if (null == count) {
            count = 0;
        }

        resultPage.setCurrent(current);
        resultPage.setSize(size);
        resultPage.setPages(count%size==0 ? count/size : count/size +1);
        resultPage.setRecords(resultList);
        resultPage.setTotal(count);
        return resultPage;
    }

    private String getSystemPath() {
        return System.getProperty("os.name").toLowerCase().contains("windows") ? "D://AutoGenerator" : "/tmp/AutoGenerator";
    }

    private FastAutoGenerator fastAutoGenerator(MgGeneratorCodeDTO dto) {
        deleteFile(new File(getSystemPath()));
        return FastAutoGenerator.create(
                new DataSourceConfig.Builder(
                        null == dto.getUrl() ? defaultUrl : dto.getUrl(),
                        null == dto.getUsername() ? defaultUsername : dto.getUsername(),
                        null == dto.getPassword() ? defaultPassword : dto.getPassword())
                        .typeConvert(new MySqlTypeConvert()).keyWordsHandler(new MySqlKeyWordsHandler()))
                .templateEngine( new FreemarkerTemplateEngine())
                .globalConfig(builder -> builder
                        .disableOpenDir()
                        .outputDir(getSystemPath())
                        .author(StringUtils.isNotEmpty(dto.getAuthor()) ? dto.getAuthor() : "zhuyuechao")
                        .enableSpringdoc()
                        .dateType(DateType.ONLY_DATE)
                        .build())
                .packageConfig(builder -> builder
                        .parent(StringUtils.isNotEmpty(dto.getParent()) ? dto.getParent() : "com.management")
                        .moduleName(StringUtils.isNotEmpty(dto.getModuleName()) ? dto.getModuleName() : "")
                        .entity("entity")
                        .service("service")
                        .serviceImpl("service.impl")
                        .mapper("mapper")
                        .xml("mapper.xml")
                        .controller("controller")
                        .build())
//                .templateConfig(builder -> builder
//                        // 禁用所用模板，启用自己的配置模板
//                        .disable()
//                        .entity("/templates/entity.java")
//                        .service("/templates/service.java")
//                        .serviceImpl("/templates/serviceImpl.java")
//                        .mapper("/templates/mapper.java")
//                        .xml("/templates/xml.java")
//                        .controller("/templates/controller.java")
//                        .build())
                .strategyConfig(builder -> builder
                        .enableSkipView()
                        .addInclude(dto.getTableNameList())
                        .build()
                        // entity配置
                        .entityBuilder()
                        .enableActiveRecord()
                        .enableColumnConstant()
                        .enableRemoveIsPrefix()
                        .enableLombok()
                        .enableTableFieldAnnotation()
                        .enableFileOverride()
                        //.enableTableFieldAnnotation()
                        .naming(NamingStrategy.underline_to_camel)
                        // controller配置
                        .controllerBuilder().enableRestStyle()
                        // service配置
                        .serviceBuilder().superServiceClass(IService.class).superServiceImplClass(ServiceImpl.class)
                        .convertServiceFileName(entityName -> entityName + ConstVal.SERVICE)
                        // mapper配置
                        .mapperBuilder().superClass(BaseMapper.class).enableMapperAnnotation().enableBaseResultMap()
                        .enableBaseColumnList()
                .build());
    }

    private void deleteFile(File file) {
        //判断文件不为null或文件目录存在
        if (file == null || !file.exists()){
            return;
        }
        //取得这个目录下的所有子文件对象
        File[] files = file.listFiles();
        //遍历该目录下的文件对象
        if (null != files) {
            for (File f: files){
                //打印文件名
                String name = file.getName();
                System.out.println(name);
                //判断子目录是否存在子目录,如果是文件则删除
                if (f.isDirectory()){
                    deleteFile(f);
                }else {
                    f.delete();
                }
            }
        }
        //删除空文件夹  for循环已经把上一层节点的目录清空。
        file.delete();
    }
}
