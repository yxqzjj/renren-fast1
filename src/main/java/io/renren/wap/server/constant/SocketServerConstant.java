package io.renren.wap.server.constant;

/**
 * @ClassName: SocketServerConstant
 * @Description: 服务端常量信息
 * @Author: CalmLake
 * @Date: 2018/11/21  11:26
 * @Version: V1.0.0
 **/
public class SocketServerConstant {
    /**
     * 伽力森wcs-wms消息心跳包
     */
    public static final String XML_HEART_INFO = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<WmsWcsXML_Envelope>\n" +
            "  <TransportModeChangeReport>\n" +
            "    <ControlArea>\n" +
            "      <Sender>\n" +
            "        <Division>WCS</Division>\n" +
            "      </Sender>\n" +
            "      <Receiver>\n" +
            "        <Division>WMS</Division>\n" +
            "      </Receiver>\n" +
            "      <CreationDateTime>2019-07-12 16:26:46</CreationDateTime>\n" +
            "    </ControlArea>\n" +
            "    <DataArea>\n" +
            "      <TransportType>01</TransportType>\n" +
            "      <MHA>1101</MHA>\n" +
            "      <Information>00</Information>\n" +
            "    </DataArea>\n" +
            "  </TransportModeChangeReport>\n" +
            "</WmsWcsXML_Envelope>";
}
