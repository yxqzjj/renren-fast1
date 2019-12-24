package io.renren;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.modules.generator.dao.WcsMachineDao;
import io.renren.modules.generator.entity.WcsMachineEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author ：zzwy
 * @date ：Created in 2019/12/23 14:11
 * @description：Mybatis-plus条件构造器测试
 * @modified By：
 * @version: 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class QueryWrapperTests {

     @Autowired
     private WcsMachineDao wcsMachineDao;

     private Logger logger = LoggerFactory.getLogger(getClass());

     public List<WcsMachineEntity> getListMachine(){
         QueryWrapper<WcsMachineEntity> queryWrapper = new QueryWrapper<>();
         List<WcsMachineEntity> wcsMachineEntities = wcsMachineDao.selectList(queryWrapper);
         return  wcsMachineEntities;
     }

     @Test
     public void getListMachineTest()
     {
         List<WcsMachineEntity> listMachine = getListMachine();
         for(WcsMachineEntity wcsMachineEntity : listMachine)
         {
             logger.info("设备信息"+wcsMachineEntity.getBlockName());
         }
     }
}
