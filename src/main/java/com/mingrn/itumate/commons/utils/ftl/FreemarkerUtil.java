package com.mingrn.itumate.commons.utils.ftl;

import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    /** 模板模板地址 */
    public static final String DEFAULT_PACKAGE = "templates/";

    private static final Logger LOGGER = LoggerFactory.getLogger(FreemarkerUtil.class);

    /**
     * 读取 freemarker 模板数据 toString
     *
     * @param ftlName 模板名称
     */
    public static String genFtl2String(String ftlName) {
        return genFtl2String(ftlName, null);
    }

    /**
     * 读取 freemarker 模板数据 toString
     *
     * @param ftlName 模板名称
     * @param data    模板数据
     */
    public static String genFtl2String(String ftlName, Map<String, Object> data) {
        return genFtl2String(ftlName, data, DEFAULT_PACKAGE);
    }

    /**
     * 读取 freemarker 模板数据 toString
     *
     * @param ftlName     模板名称
     * @param data        模板数据
     * @param basePackage 模板路径
     */
    public static String genFtl2String(String ftlName, Map<String, Object> data, String basePackage) {
        try (Writer out = new StringWriter()) {
            freemarker.template.Configuration cfg = getConfiguration(basePackage);
            cfg.getTemplate(ftlName).process(data, out);
            out.flush();
            return out.toString();
        } catch (IOException | TemplateException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 加载 freemarker 模板资源
     *
     * @param basePackage 模板路径
     */
    public static freemarker.template.Configuration getConfiguration(String basePackage) {
        freemarker.template.Configuration cfg = new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_28);
        cfg.setClassLoaderForTemplateLoading(FreemarkerUtil.class.getClassLoader(), basePackage);
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
        return cfg;
    }
}