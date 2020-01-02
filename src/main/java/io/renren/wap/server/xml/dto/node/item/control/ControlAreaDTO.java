package io.renren.wap.server.xml.dto.node.item.control;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

/**
 * @ClassName: ControlAreaDTO
 * @Description: xml 控制区域信息
 * @Author: CalmLake
 * @Date: 2018/11/20  11:31
 * @Version: V1.0.0
 **/
@XStreamAlias("ControlArea")
public class ControlAreaDTO implements Serializable{
    @XStreamAlias("Sender")
    private SenderDTO senderDTO;
    @XStreamAlias("Receiver")
    private ReceiverDTO receiverDTO;
    @XStreamAlias("CreationDateTime")
    private String creationDateTime;
    @XStreamAlias("RefId")
    private RefIdDTO refIdDTO;

    public SenderDTO getSenderDTO() {
        return senderDTO;
    }

    public void setSenderDTO(SenderDTO senderDTO) {
        this.senderDTO = senderDTO;
    }

    public ReceiverDTO getReceiverDTO() {
        return receiverDTO;
    }

    public void setReceiverDTO(ReceiverDTO receiverDTO) {
        this.receiverDTO = receiverDTO;
    }

    public String getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(String creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public RefIdDTO getRefIdDTO() {
        return refIdDTO;
    }

    public void setRefIdDTO(RefIdDTO refIdDTO) {
        this.refIdDTO = refIdDTO;
    }
}
