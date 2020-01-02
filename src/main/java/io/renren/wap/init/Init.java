package io.renren.wap.init;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.modules.generator.dao.WcsMachineDao;
import io.renren.modules.generator.entity.*;
import io.renren.wap.cache.LockCache;
import io.renren.wap.cache.MachineCache;
import io.renren.wap.cache.PriorityCache;
import io.renren.wap.cache.SystemCache;
import io.renren.wap.customer.WorkPlanFinishCustomer;
import io.renren.wap.dto.LockDTO;
import io.renren.wap.entity.constant.MachineConstant;
import io.renren.wap.lock.LockImpl;
import io.renren.wap.lock.LockInterface;
import io.renren.wap.singleton.ThreadPoolServiceSingleton;
import io.renren.wap.thread.DeleteLogThreadTimerTask;
import io.renren.wap.thread.MachineAlTaskingThread;
import io.renren.wap.thread.MachineClTaskingThread;
import io.renren.wap.thread.MachineTaskingThread;
import io.renren.wap.util.DbUtil;
import io.renren.wap.util.SleepUtil;
import io.renren.wap.yml.WmsInfo;
import org.apache.logging.log4j.LogManager;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 初始化服务
 *
 * @Author: CalmLake
 * @Date: 2019/1/4  16:31
 * @Version: V1.0.0
 **/
@Component
public class Init {

    @PostConstruct
    public void init() {
        InitThread initThread = new InitThread();
        ThreadPoolServiceSingleton.getInstance().getDefaultThreadFactory().newThread(initThread).start();
    }

    private class InitThread implements Runnable {

        @Override
        public void run() {
            SleepUtil.sleep();
            init();
        }

        void init() {
            LogManager.getLogger().info("初始化服务-开始=========================================================================！");
            initMachineHashMap();
            initPriorityHashMap();
            SocketClientInit socketClientInit = new SocketClientInit();
            socketClientInit.startSocketClientInit();
            CustomerInit customerInit = new CustomerInit();
            customerInit.init();
            tasking();
            workPlanFinishCustomer();
            iniWMSSocketServerService();
            LogManager.getLogger().info("初始化服务-结束=========================================================================！");
            LogManager.getLogger().info("***************飞雪连天射白鹿，笑书神侠倚碧鸳***********！");
        }

        /**
         * 初始化设备信息
         *
         * @author CalmLake
         * @date 2019/3/8 14:36
         */
        private void initMachineHashMap() {
            List<WcsMachineEntity> machineList = DbUtil.getMachineDao().selectList(new QueryWrapper<WcsMachineEntity>());
            for (WcsMachineEntity machine : machineList) {
                String blockName = machine.getBlockName();
                WcsDefaultlocationEntity defaultLocation = DbUtil.getDefaultLocationDao().selectOne(new QueryWrapper<WcsDefaultlocationEntity>().eq("block_name",blockName));
                if (defaultLocation != null) {
                    System.out.println(defaultLocation.getDefaultLocation());
                    machine.setDefaultLocation(defaultLocation.getDefaultLocation());
                }
                MachineCache.addMachine(machine.getBlockName(), machine);
            }
            LogManager.getLogger().info("初始化设备信息！");
        }

        /**
         * 初始化优先级信息
         *
         * @author CalmLake
         * @date 2019/3/8 14:40
         */
        private void initPriorityHashMap() {
            List<WcsPriorityconfigEntity> priorityConfigList = DbUtil.getPriorityConfigDao().selectList(new QueryWrapper<WcsPriorityconfigEntity>());
            for (WcsPriorityconfigEntity priorityConfig : priorityConfigList) {
                PriorityCache.addByte(priorityConfig.getWorkplantype(), priorityConfig.getPriority());
            }
            LogManager.getLogger().info("初始化工作计划优先级信息！");
        }

        /**
         * 初始化wcs-wms通讯服务
         *
         * @author CalmLake
         * @date 2019/3/29 16:32
         */
        private void iniWMSSocketServerService() {
            if (DbUtil.getYmlReadUtil().getSystemConfigInfo().isWmsOpen()) {
                WmsInfo wmsInfo = DbUtil.getYmlReadUtil().getWmsInfo();
                SocketAddress socketAddress = new InetSocketAddress(wmsInfo.getPort());
                SocketServerInit socketServerInit = new SocketServerInit(wmsInfo.getName(), socketAddress);
                socketServerInit.startSocketServerService();
                LogManager.getLogger().info("wcs-wms通讯启动！");
            } else {
                LogManager.getLogger().info("wcs-wms通讯未配置启动！");
            }
        }

        /**
         * 定时删除文件
         *
         * @author CalmLake
         * @date 2019/4/24 14:47
         */
        private void deleteFile() {
            if (SystemCache.DELETE_LOG_SWITCH) {
                DeleteLogThreadTimerTask deleteLogThreadTimerTask = new DeleteLogThreadTimerTask();
                ThreadPoolServiceSingleton.getInstance().getExecutorServiceDelete().scheduleAtFixedRate(deleteLogThreadTimerTask, 0, SystemCache.LOG_RESERVE_TIME, TimeUnit.DAYS);
            }
        }

        /**
         * 任务分配
         *
         * @author CalmLake
         * @date 2019/6/14 15:35
         */
        private void tasking() {
            List<WcsMachineEntity> machineList = MachineCache.getTaskFlagMachine();
            Lock lock = new ReentrantLock();
            for (WcsMachineEntity machine : machineList) {
                if (machine.getTaskFlag()) {
                    LockDTO lockDTO = new LockDTO();
                    Condition condition = lock.newCondition();
                    lockDTO.setName(machine.getBlockName());
                    lockDTO.setLock(lock);
                    lockDTO.setCondition(condition);
                    LockInterface lockInterface = new LockImpl(lockDTO);
                    LockCache.put(machine.getBlockName(),lockInterface);
                    if (MachineConstant.BYTE_TYPE_AL.equals(machine.getType())){
                        MachineAlTaskingThread machineAlTaskingThread = new MachineAlTaskingThread(machine, lockInterface);
                        ThreadPoolServiceSingleton.getInstance().getDefaultThreadFactory().newThread(machineAlTaskingThread).start();
                    }else {
                        MachineTaskingThread machineTaskingThread = new MachineTaskingThread(machine, lockInterface);
                        ThreadPoolServiceSingleton.getInstance().getDefaultThreadFactory().newThread(machineTaskingThread).start();
                    }
                }
            }
            List<WcsCrossrouteEntity> crossRouteList=DbUtil.getCrossRouteDao().selectList(new QueryWrapper<WcsCrossrouteEntity>());
            for (WcsCrossrouteEntity crossRoute:crossRouteList){
                String runBlockName=crossRoute.getRunBlockName();
                LockDTO lockDTO = new LockDTO();
                Condition condition = lock.newCondition();
                lockDTO.setName(runBlockName);
                lockDTO.setLock(lock);
                lockDTO.setCondition(condition);
                LockInterface lockInterface = new LockImpl(lockDTO);
                LockCache.put(crossRoute.getRunBlockName(),lockInterface);
                MachineClTaskingThread machineClTaskingThread=new MachineClTaskingThread(runBlockName,lockInterface);
                ThreadPoolServiceSingleton.getInstance().getDefaultThreadFactory().newThread(machineClTaskingThread).start();
            }

        }

        /**
         * 工作计划完成处理
         *
         * @author CalmLake
         * @date 2019/6/14 15:35
         */
        private void workPlanFinishCustomer() {
            WorkPlanFinishCustomer workPlanFinishCustomer = new WorkPlanFinishCustomer();
            ThreadPoolServiceSingleton.getInstance().getDefaultThreadFactory().newThread(workPlanFinishCustomer).start();
        }
    }
}
