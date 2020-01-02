package io.renren.wap.service.block.impl;


import io.renren.modules.generator.entity.WcsMachineEntity;
import io.renren.wap.cache.MachineCache;
import io.renren.wap.cache.SystemCache;
import io.renren.wap.entity.constant.CompanyConstant;
import io.renren.wap.entity.constant.MachineConstant;
import io.renren.wap.service.block.OperationKeyService;
import io.renren.wap.util.Log4j2Util;

/**
 * 设备消息制作发送工厂
 *
 * @Author: CalmLake
 * @Date: 2019/3/27  11:36
 * @Version: V1.0.0
 **/
public class BlockServiceImplFactory {

    public static void blockServiceDoKey(String blockName,boolean result){
        if (result){
            blockServiceDoKey(blockName);
        }
    }

    public static void blockServiceDoKey(String blockName) {
        try {
            OperationKeyService operationKeyService = createBlockServiceImpl(blockName);
            operationKeyService.operationKey(blockName);
        } catch (Exception e) {
            e.printStackTrace();
            Log4j2Util.getBlockBrickLogger().error(String.format("blockName:%s,处理出现异常，异常信息：%s",blockName,e.getMessage()));
        }
    }

    /**
     * 获取各设备制作消息的对象
     *
     * @param blockName 数据block名称
     * @return com.wap.service.block.OperationKeyService
     * @author CalmLake
     * @date 2019/3/27 11:46
     */
    private static OperationKeyService createBlockServiceImpl(String blockName) {
        WcsMachineEntity machine = MachineCache.getMachine(blockName);
        Integer type = machine.getType();
        OperationKeyService operationKeyService;
        if (MachineConstant.BYTE_TYPE_ML.equals(type)) {
            operationKeyService = new MlBlockServiceImpl();
        } else if (MachineConstant.BYTE_TYPE_AL.equals(type)) {
            operationKeyService = new AlBlockServiceImpl();
        } else if (MachineConstant.BYTE_TYPE_MC.equals(type)) {
            operationKeyService = new McBlockServiceImpl();
        } else if (MachineConstant.BYTE_TYPE_RGV.equals(type)) {
            operationKeyService = new RgvBlockServiceImpl();
        } else if (MachineConstant.BYTE_TYPE_CL.equals(type)) {
            operationKeyService = new ClBlockServiceImpl();
        } else {
            if(CompanyConstant.SYS_NAME_COMPANY_JIA_TIAN.equals(SystemCache.SYS_NAME_COMPANY)){
                operationKeyService = new JiaTianScBlockServiceImpl();
            }else {
                operationKeyService = new ScBlockServiceImpl();
            }
        }
        return operationKeyService;
    }
}
