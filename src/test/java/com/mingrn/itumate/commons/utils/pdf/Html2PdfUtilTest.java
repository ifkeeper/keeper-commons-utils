package com.mingrn.itumate.commons.utils.pdf;

import com.mingrn.itumate.commons.utils.ftl.FreemarkerUtil;
import org.junit.Test;

import java.util.UUID;

public class Html2PdfUtilTest {

    @Test
    public void testWrite2PDF(){
        String html4 = FreemarkerUtil.genFtl2String("testpdf.ftl");
        System.out.println(html4);
        Html2PdfUtil.write2PDF(html4, "C:\\Users\\MinGRn\\Downloads\\pdf\\" + UUID.randomUUID().toString() + ".pdf");
    }
}
