package io.renren.wap.server.xml.util;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import io.renren.wap.server.xml.constant.XmlInfoConstant;
import io.renren.wap.server.xml.dto.EnvelopeDTO;

/**
 * xml解析与封装工具类
 *
 * @Author: CalmLake
 * @Date: 2018/11/20  17:46
 * @Version: V1.0.0
 **/
public class XStreamUtil {
    private static XStream xStream;

    static {
        xStream = new XStream(new DomDriver());
        xStream.processAnnotations(EnvelopeDTO.class);
    }


    /**
     * @return java.lang.String
     * 对象转xml字符串
     * @author CalmLake
     * @date 2018/11/20 17:48
     * @Param [envelopeDTO] xml对象
     */
    public static String toXMLString(EnvelopeDTO envelopeDTO) {
        String xmlString = XmlInfoConstant.XML_HEAD + xStream.toXML(envelopeDTO);
        xmlString = xmlString.replace("__", "_");
        return xmlString;
    }

    /**
     * @return com.wap.server.xml.dto.EnvelopeDTO
     * xml字符串转对象
     * @author CalmLake
     * @date 2018/11/20 17:50
     * @Param [xmlString]
     */
    public static EnvelopeDTO stringToEnvelopeDto(String xmlString) {
        return (EnvelopeDTO) xStream.fromXML(xmlString);
    }
}
