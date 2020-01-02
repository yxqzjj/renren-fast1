package io.renren.wap.client.thread;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.modules.generator.entity.WcsPlcconfigEntity;
import io.renren.wap.cache.SystemCache;
import io.renren.wap.client.cache.ClientConfigCache;
import io.renren.wap.client.cache.MsgQueueCache;
import io.renren.wap.client.constant.FactoryConstant;
import io.renren.wap.client.constant.MsgConstant;
import io.renren.wap.client.dto.*;
import io.renren.wap.client.factory.FactoryProducer;
import io.renren.wap.client.service.CreateBytesService;
import io.renren.wap.client.service.MsgReceiveService;
import io.renren.wap.client.service.MsgSendService;
import io.renren.wap.client.util.BccUtil;
import io.renren.wap.client.util.MsgCreateUtil;
import io.renren.wap.client.util.MsgMakeBytesUtil;
import io.renren.wap.client.util.MsgSubstringUtil;
import io.renren.wap.constant.ExecutorConstant;
import io.renren.wap.entity.constant.CompanyConstant;
import io.renren.wap.entity.constant.MachineConstant;
import io.renren.wap.entity.constant.PlcConfigConstant;
import io.renren.wap.service.WcsMessageLogService;
import io.renren.wap.util.DbUtil;
import io.renren.wap.util.Log4j2Util;
import io.renren.wap.util.SleepUtil;
import io.renren.wap.util.ThreadFactoryUtil;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.*;

/**
 * 客户端发送接收消息线程
 *
 * @Author: CalmLake
 * @Date: 2018/11/24  11:17
 * @Version: V1.0.0
 **/
public class SocketClientReadWriteThread implements Runnable {
    /**
     * 读写线程
     */
    private ExecutorService executorService = new ThreadPoolExecutor(2, 2,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(), new ThreadFactoryUtil("socket client read and write thread"));
    private Socket socket;
    /**
     * plc名称
     */
    private String plcName;

    SocketClientReadWriteThread(Socket socket, String plcName) {
        this.socket = socket;
        this.plcName = plcName;
    }

    @Override
    public void run() {
        Log4j2Util.getMsgQueueLogger().info(String.format("%s socket读写线程开启", plcName));
        WcsPlcconfigEntity plcconfigEntity=DbUtil.getPlcConfigDao().selectOne(new QueryWrapper<WcsPlcconfigEntity>().eq("Name",plcName));
        plcconfigEntity.setStatus(PlcConfigConstant.STATUS_CONNECTED);
        plcconfigEntity.setHeartbeatTime(new Date());
        DbUtil.getPlcConfigDao().update(plcconfigEntity,new QueryWrapper<WcsPlcconfigEntity>().eq("Name",plcName));
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
        Log4j2Util.getMsgQueueLogger().info(String.format("%s socket读写线程退出", plcName));
        plcconfigEntity.setStatus(PlcConfigConstant.STATUS_CONNECTING);
        plcconfigEntity.setHeartbeatTime(new Date());
        DbUtil.getPlcConfigDao().update(plcconfigEntity,new QueryWrapper<WcsPlcconfigEntity>().eq("Name",plcName));
    }

    /**
     * client读取信息线程
     *
     * @Author: CalmLake
     * @Date: 2018/11/18  22:53
     * @Version: V1.0.0
     **/
    private class SocketClientReadCallable implements Callable<Boolean> {

        @Override
        public Boolean call() {
            StringBuilder msg = new StringBuilder();
            try {
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
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
                            MsgCreateUtil.replaceReceiveMsgBlockName(msgDTO);
                            MsgQueueCache.addReceiveMsg(msgDTO);
                            wcsMessageLogService.insertIntoCommandLog(msgDTO);
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
     *
     * @Author: CalmLake
     * @Date: 2018/11/18  22:54
     * @Version: V1.0.0
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
                        if (CompanyConstant.SYS_NAME_COMPANY_KERISOM.contains(SystemCache.SYS_NAME_COMPANY)) {
                            if (plcName.contains(MachineConstant.TYPE_ML)) {
                                bytes = CreateBytesService.createBytes(bytes);
                            }
                        }
                        dataOutputStream.write(bytes);
                        wcsMessageLogService.insertIntoWcsMessageLog(msgDTO.getPlcName(), msgDTO.getNumString(), MsgConstant.BYTE_TYPE_SEND, "", "");
                        wcsMessageLogService.insertIntoCommandLog(msgDTO);
                        SleepUtil.sleep(0.052);
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
