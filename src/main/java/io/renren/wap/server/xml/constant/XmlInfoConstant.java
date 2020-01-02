package io.renren.wap.server.xml.constant;

/**
 * @ClassName: XmlInfoConstant
 * @Description: xml 信息常量
 * @Author: CalmLake
 * @Date: 2018/11/20  17:52
 * @Version: V1.0.0
 **/
public class XmlInfoConstant {
    /**
     * xml 文件头
     */
    public static final String XML_HEAD = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
    /**
     * WMS与WCS通讯消息结束标识
     */
    public static final String XML_END_FLAG = "</WmsWcsXML_Envelope>";
    /**
     * WCS名称
     */
    public static final String XML_WCS_NAME = "WCS";
    /**
     * WMS名称
     */
    public static final String XML_WMS_NAME = "WMS";
    /**
     * xml 中 默认值  00
     */
    public static final String XML_DEFAULT_00 = "00";
    /**
     * xml 中 默认值  0
     */
    public static final String XML_DEFAULT_0 = "0";

    public static final String XML_Information_01 = "01";
    public static final String XML_Information_02 = "02";
    public static final String XML_Information_03 = "03";
}
