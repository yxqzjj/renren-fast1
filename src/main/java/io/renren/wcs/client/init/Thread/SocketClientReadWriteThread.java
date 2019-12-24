package io.renren.wcs.client.init.Thread;
import io.renren.wcs.client.Factory.FactoryConstant;
import io.renren.wcs.client.Factory.FactoryProducer;
import io.renren.wcs.client.cache.MsgQueueCache;
import io.renren.wcs.client.contant.MsgConstant;
import io.renren.wcs.client.dto.*;
import io.renren.wcs.client.util.BccUtil;
import io.renren.wcs.client.util.MsgMakeBytesUtil;
import io.renren.wcs.client.util.MsgSubstringUtil;
import io.renren.wcs.constant.ClientConfigCache;
import io.renren.wcs.constant.ExecutorConstant;
import io.renren.wcs.service.MsgReceiveService;
import io.renren.wcs.service.MsgSendService;
import io.renren.wcs.service.WcsMessageLogService;
import io.renren.wcs.util.Log4j2Util;
import io.renren.wcs.util.ThreadFactoryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Objects;
import java.util.concurrent.*;
/**
 * @author ：zzwy
 * @date ：Created in 2019/12/23 10:32
 * @description：客户端读取和写入线程
 * @modified By：
 * @version: 1.0
 */
public class SocketClientReadWriteThread implements Runnable {
    private Logger logger = LoggerFactory.getLogger(getClass());
    /**
     * 读写线程线程池
     */
    private ExecutorService executorService = new ThreadPoolExecutor(2, 2,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(), new ThreadFactoryUtil("socket client read and write thread"));
    //1.plc名称
    private String plcName;
    //2.网络连接的Socket
    private Socket socket;

    public SocketClientReadWriteThread(Socket socket,String plcName){
        this.socket = socket;
        this.plcName = plcName;
    }
    @Override
    public void run() {
        logger.info(String.format("%s socket读写线程开启", plcName));
        Log4j2Util.getMsgQueueLogger().info(String.format("%s socket读写线程开启", plcName));
        //DbUtil.getPlcConfigDao().updateHeartTimeAndStatusByPrimaryKey(PlcConfigConstant.STATUS_CONNECTED,new Date(), plcName);
        SocketClientReadCallable socketClientReadCallable = new SocketClientReadCallable();
        SocketClientWriteCallable socketClientWriteCallable = new SocketClientWriteCallable();
        Future<Boolean> futureRead = executorService.submit(socketClientReadCallable);
        Future<Boolean> futureWrite = executorService.submit(socketClientWriteCallable);
        try {
            if (!futureRead.get() || !futureWrite.get()) {
                futureRead.cancel(ExecutorConstant.FUTURE_CANCEL_FLAG);
                futureWrite.cancel(ExecutorConstant.FUTURE_CANCEL_FLAG);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }
        logger.info(String.format("%s socket读写线程退出",plcName));
        Log4j2Util.getMsgQueueLogger().info(String.format("%s socket读写线程退出", plcName));
       // DbUtil.getPlcConfigDao().updateStatusByPrimaryKey(PlcConfigConstant.STATUS_CONNECTING, plcName);
    }
    /**
     * client读取信息线程
     **/
    private class SocketClientReadCallable implements Callable<Boolean> {

        @Override
        public Boolean call() {
            StringBuilder msg = new StringBuilder();
            try {
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                logger.info(String.format("%s socket读写线程读取中"));
                Log4j2Util.getMsgQueueLogger().info(String.format("%s socket读写线程读取中....", plcName));
                while (socket.isConnected() && ClientConfigCache.SOCKET_CLIENT_THREAD_ON_OFF_FLAG && !Thread.currentThread().isInterrupted()) {
                    byte rcvByte = dataInputStream.readByte();
                    if (rcvByte == 2) {
                        //报头
                        msg = new StringBuilder();
                    } else if (rcvByte == 3) {
                        //报尾
                        WcsMessageLogService wcsMessageLogService = new WcsMessageLogService();
                        String data = MsgSubstringUtil.getMsgData(msg.toString());
                        String bcc = MsgSubstringUtil.getMsgBcc(msg.toString());
                        wcsMessageLogService.insertIntoWcsMessageLog(plcName, msg.toString(), MsgConstant.BYTE_TYPE_RECEIVE, "", "");
                        boolean resultDataCheck = BccUtil.isBcc(data, bcc);
                        if (resultDataCheck) {
                            MsgReceiveService msgReceiveService = Objects.requireNonNull(FactoryProducer.getFactory(FactoryConstant.RECEIVE)).getMsgReceiveService(msg.toString());
                            MsgDTO msgDTO = msgReceiveService.getMsgDTO(msg.toString());
                            msgDTO.setPlcName(plcName);
                            if (!(msgDTO instanceof MsgHeartBeatSignalAckDTO) && !(msgDTO instanceof MsgMachineryStatusOrderAckDTO)) {
                                Log4j2Util.getMsgQueueLogger().info(String.format("received 队列,%s 接收消息 [%s] —— %s", plcName, msgDTO.getCommandType(), msgDTO.toString()));
                            } else {
                               Log4j2Util.getMsgHeartMachineStatus().info(String.format("received 队列,%s 接收消息 [%s] %s", plcName, msgDTO.getCommandType(), msgDTO.toString()));
                            }
                            MsgQueueCache.addReceiveMsg(plcName, msgDTO);
                             //wcsMessageLogService.insertIntoCommandLog(msgDTO);
                        } else {
                            Log4j2Util.getMsgQueueLogger().info(String.format("received 队列,%s Bcc校验失败，msg：%s", plcName, msg.toString()));
                        }
                        msg = new StringBuilder();
                    } else {
                        msg.append((char) rcvByte);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
              Log4j2Util.getMsgQueueLogger().error(String.format("received 队列,%s,异常时信息记录， 接收消息 [%s]", plcName, msg));
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return false;
        }
    }

    /**
     * client写入信息线程
     **/
    private class SocketClientWriteCallable implements Callable<Boolean> {
        @Override
        public Boolean call() {
            try {
                if (socket != null && socket.isConnected() && !socket.isClosed() && !Thread.currentThread().isInterrupted()) {
                    DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    Log4j2Util.getMsgQueueLogger().info(String.format("%s socket读写线程写入中....", plcName));
                    while (socket.isConnected() && ClientConfigCache.SOCKET_CLIENT_THREAD_ON_OFF_FLAG) {
                        WcsMessageLogService wcsMessageLogService = new WcsMessageLogService();
                        MsgDTO msgDTO = MsgQueueCache.getSendMsg(plcName);
                        if (!(msgDTO instanceof MsgHeartBeatSignalAskDTO) && !(msgDTO instanceof MsgMachineryStatusOrderAskDTO)) {
                            Log4j2Util.getMsgQueueLogger().info(String.format("send 队列,%s 发送消息 [%s] %s", plcName, msgDTO.getCommandType(), msgDTO.toString()));
                        } else {
                           Log4j2Util.getMsgHeartMachineStatus().info(String.format("send 队列,%s 发送消息 [%s] %s", plcName, msgDTO.getCommandType(), msgDTO.toString()));
                        }
                        MsgSendService msgSendService = Objects.requireNonNull(FactoryProducer.getFactory(FactoryConstant.SEND)).getMsgSendService(msgDTO);
                        byte[] bytes = msgSendService.msgDTOToBytes(msgDTO);
                        MsgMakeBytesUtil.replaceBytesSTXAndETX(bytes);
                       /* if (CompanyConstant.SYS_NAME_COMPANY_KERISOM.contains(SystemCache.SYS_NAME_COMPANY)) {
                            if (plcName.contains(MachineConstant.TYPE_ML)) {
                                bytes = CreateBytesService.createBytes(bytes);
                            }
                        }*/
                        dataOutputStream.write(bytes);
                        wcsMessageLogService.insertIntoWcsMessageLog(msgDTO.getPlcName(), msgDTO.getNumString(), MsgConstant.BYTE_TYPE_SEND, "", "");
                        //wcsMessageLogService.insertIntoCommandLog(msgDTO);
                       // SleepUtil.sleep(0.052);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
              Log4j2Util.getMsgQueueLogger().error(String.format("send 队列,%s,异常时信息记录，异常：%s", plcName, e.getMessage()));
            } finally {
                try {
                    assert socket != null;
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return false;
        }
    }
}
