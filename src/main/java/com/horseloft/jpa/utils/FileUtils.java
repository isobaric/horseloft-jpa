package com.horseloft.jpa.utils;

import com.alibaba.excel.EasyExcel;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * Date: 2020/2/2 下午5:23
 * User: YHC
 * Desc: 文件处理
 */
public class FileUtils {

    /**
     * excel导出
     * @param response
     * @param list
     * @param filename
     * @throws IOException
     */
    public static void excelDownload(HttpServletResponse response, List<?> list, String filename) throws IOException {
        if (list.isEmpty()) {
            return;
        }
        //使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码|swagger/postman等工具可能是乱码，浏览器正常
        String fileName;
        try {
            fileName = URLEncoder.encode(filename, "UTF-8");
        } catch (Exception e) {
            fileName = String.valueOf(System.currentTimeMillis());
        }
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        //表格导出
        EasyExcel.write(response.getOutputStream(), list.get(0).getClass()).sheet("列表").doWrite(list);
    }
}
