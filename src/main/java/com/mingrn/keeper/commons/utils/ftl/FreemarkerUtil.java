package com.mingrn.keeper.commons.utils.ftl;

import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

/**
 * freemarker 模板工具类
 *
 * @author MinGRn <br > MinGRn97@gmail.com
 * @date 2019-02-20 21:53
 */
public final class FreemarkerUtil {

    private FreemarkerUtil() {

    }

    /**
     * 读取 freemarker 模板数据 toString
     */
    public static String genFtl2String(String ftlName, Map<String, Object> data) {
        try (Writer out = new StringWriter()) {
            freemarker.template.Configuration cfg = getConfiguration();
            cfg.getTemplate(ftlName).process(data, out);
            out.flush();
            return out.toString();
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 加载 freemarker 模板资源
     */
    private static freemarker.template.Configuration getConfiguration() {
        freemarker.template.Configuration cfg = new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_28);
        cfg.setClassLoaderForTemplateLoading(FreemarkerUtil.class.getClassLoader(), "templates/");
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
        return cfg;
    }
}