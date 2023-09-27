package com.zzyycc.management.utils;

import com.zzyycc.common.core.exception.MgException;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author zhuyuechao
 * @version 1.0.0
 * @className ToZip
 * @createTime 2022/2/28 16:29
 * @description 转换为zip
 */
public class ZipUtil {

    /**
     * 向网页端输出zip包
     *
     * @param response servlet响应
     * @param sourceFile 源文件
     * @date 2022/3/1 9:51
     */
    public static void toZip(HttpServletResponse response, File sourceFile) {
        if (null == sourceFile) {
            throw new MgException("文件不存在，请重试！");
        }
        ZipOutputStream zipOutputStream = null;
        BufferedOutputStream bufferedOut = null;
        try {
            String headerType = "Content-disposition";
            String fileName = "autoGenerator" + ".zip";
            response.setContentType("application/octet-stream");
            response.setHeader("Access-Control-Expose-Headers", headerType);
            // 表示不能用浏览器直接打开
            response.setHeader("Connection", "close");
            // 告诉客户端允许断点续传多线程连接下载
            response.setHeader("Accept-Ranges", "bytes");
            response.setCharacterEncoding("UTF-8");
            response.setHeader(headerType, "attachment; filename=\"" + fileName + "\"");

            // 创建zip输出流和缓冲流
            zipOutputStream = new ZipOutputStream(response.getOutputStream());
            bufferedOut = new BufferedOutputStream(zipOutputStream);
            compress(zipOutputStream, bufferedOut, sourceFile, null);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭流
            try {
                if (null != zipOutputStream) {
                    zipOutputStream.closeEntry();
                    zipOutputStream.close();
                }
                if (null != bufferedOut) {
                    bufferedOut.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 向本地磁盘输出zip包
     *
     * @param path 输出的本地路径
     * @param sourceFile 源文件
     * @date 2022/3/1 9:51
     */
    public static void toZip(String path, File sourceFile) {
        if (null == sourceFile) {
            throw new MgException("文件不存在，请重试！");
        }
        if (StringUtils.isEmpty(path)) {
            throw new MgException("输出路径不能为空！");
        }

        ZipOutputStream zipOutputStream = null;
        BufferedOutputStream bufferedOut = null;
        try {
            // 创建zip输出流和缓冲流
            zipOutputStream = new ZipOutputStream(new FileOutputStream(path));
            bufferedOut = new BufferedOutputStream(zipOutputStream);
            compress(zipOutputStream, bufferedOut, sourceFile, null);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭流
            try {
                if (null != zipOutputStream) {
                    zipOutputStream.closeEntry();
                    zipOutputStream.close();
                }
                if (null != bufferedOut) {
                    bufferedOut.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 递归压缩文件
     *
     * @param zipOut 压缩流
     * @param bufferedOut 缓冲流
     * @param sourceFile 源文件
     * @param name 名称
     * @throws IOException io异常
     */
    private static void compress(ZipOutputStream zipOut, BufferedOutputStream bufferedOut, File sourceFile, String name) throws IOException {
        if (StringUtils.isEmpty(name)) {
            name = sourceFile.getName();
        }
        // 如果是文件
        if (sourceFile.isFile()) {
            zipOut.putNextEntry(new ZipEntry(name));
            FileInputStream fileInputStream = new FileInputStream(sourceFile);
            //将源文件写入到zip文件中
            int len;
            byte[] bytes = new byte[2048];
            while ((len = fileInputStream.read(bytes)) != -1) {
                bufferedOut.write(bytes, 0, len);
            }
            // 需要刷新关闭流
            bufferedOut.flush();
            zipOut.closeEntry();
            fileInputStream.close();
        } else {
            // 如果是文件夹，递归压缩
            File[] files = sourceFile.listFiles();
            // 如果为空，只输出文件夹，不为空是名称需要带上上级目录+/，不然会全部生成在一个目录下
            if (null == files) {
                zipOut.putNextEntry(new ZipEntry(name + "/"));
            } else {
                for (File file : files) {
                    compress(zipOut, bufferedOut, file, name + "/" + file.getName());
                }
            }
        }
    }
}

