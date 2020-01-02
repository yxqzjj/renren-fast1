package io.renren.wap.service;


import io.renren.wap.constant.StorageAreaConstant;

/**
 * 货位分区
 *
 * @Author: CalmLake
 * @Date: 2019/1/10  14:31
 * @Version: V1.0.0
 **/
public class StorageAreaService {
    /**
     * 模具仓，设备是否处于同一巷道
     *
     * @param row  排
     * @param row2 排
     * @return boolean
     * @author CalmLake
     * @date 2019/3/7 15:04
     */
    public static boolean isSameArea_MUJVCANG(int row, int row2) {
        if (row > StorageAreaConstant.ZERO && row <= StorageAreaConstant.MU_JV_CANG_AREA_1_END) {
            return row2 > StorageAreaConstant.ZERO && row2 <= StorageAreaConstant.MU_JV_CANG_AREA_1_END;
        } else if (row > StorageAreaConstant.MU_JV_CANG_AREA_1_END && row <= StorageAreaConstant.MU_JV_CANG_AREA_2_END) {
            return row2 > StorageAreaConstant.MU_JV_CANG_AREA_1_END && row2 <= StorageAreaConstant.MU_JV_CANG_AREA_2_END;
        } else {
            return false;
        }
    }

    /**
     * 佳田，设备是否处于同一巷道
     *
     * @param row  排
     * @param row2 排
     * @return boolean
     * @author CalmLake
     * @date 2019/3/7 15:04
     */
    public static boolean isSameArea_JIATIAN(int row, int row2) {
        if (row > StorageAreaConstant.ZERO && row <= StorageAreaConstant.JIA_TIAN_AREA_1_END) {
            return row2 > StorageAreaConstant.ZERO && row2 <= StorageAreaConstant.JIA_TIAN_AREA_1_END;
        } else if (row > StorageAreaConstant.JIA_TIAN_AREA_1_END && row <= StorageAreaConstant.JIA_TIAN_AREA_2_END) {
            return row2 > StorageAreaConstant.JIA_TIAN_AREA_1_END && row2 <= StorageAreaConstant.JIA_TIAN_AREA_2_END;
        } else {
            return false;
        }
    }
    /**
     * 演示区，设备是否处于同一巷道
     *
     * @param row  排
     * @param row2 排
     * @return boolean
     * @author CalmLake
     * @date 2019/3/7 15:04
     */
    public static boolean isSameArea_YANSHIQU(int row, int row2) {
        if (row > StorageAreaConstant.ZERO && row <= StorageAreaConstant.YAN_SHI_QU_AREA_1_END) {
            return row2 > StorageAreaConstant.ZERO && row2 <= StorageAreaConstant.YAN_SHI_QU_AREA_1_END;
        } else if (row > StorageAreaConstant.YAN_SHI_QU_AREA_1_END && row <= StorageAreaConstant.YAN_SHI_QU_AREA_2_END) {
            return row2 > StorageAreaConstant.YAN_SHI_QU_AREA_1_END && row2 <= StorageAreaConstant.YAN_SHI_QU_AREA_2_END;
        } else {
            return false;
        }
    }

    /**
     * 永祥，设备是否处于同一巷道
     *
     * @param row  排
     * @param row2 排
     * @return boolean
     * @author CalmLake
     * @date 2019/3/7 15:04
     */
    public static boolean isSameArea_YONGXIANG(int row, int row2) {
        if (row > StorageAreaConstant.ZERO && row <= StorageAreaConstant.YONG_XIANG_AREA_1_END) {
            return row2 > StorageAreaConstant.ZERO && row2 <= StorageAreaConstant.YONG_XIANG_AREA_1_END;
        } else if (row > StorageAreaConstant.YONG_XIANG_AREA_1_END && row <= StorageAreaConstant.YONG_XIANG_AREA_2_END) {
            return row2 > StorageAreaConstant.YONG_XIANG_AREA_1_END && row2 <= StorageAreaConstant.YONG_XIANG_AREA_2_END;
        } else if (row > StorageAreaConstant.YONG_XIANG_AREA_2_END && row <= StorageAreaConstant.YONG_XIANG_AREA_3_END) {
            return row2 > StorageAreaConstant.YONG_XIANG_AREA_2_END && row2 <= StorageAreaConstant.YONG_XIANG_AREA_3_END;
        } else {
            return false;
        }
    }

    /**
     * 伽力森，设备是否处于同一巷道
     *
     * @param line  列
     * @param line2 列
     * @return boolean
     * @author CalmLake
     * @date 2019/3/7 15:04
     */
    public static boolean isSameArea_JIALISEN(int line, int line2) {
        if (line > StorageAreaConstant.ZERO && line <= StorageAreaConstant.KERISOM_AREA_1_END) {
            return line2 > StorageAreaConstant.ZERO && line2 <= StorageAreaConstant.KERISOM_AREA_1_END;
        } else if (line > StorageAreaConstant.KERISOM_AREA_1_END && line <= StorageAreaConstant.KERISOM_AREA_2_END) {
            return line2 > StorageAreaConstant.KERISOM_AREA_1_END && line2 <= StorageAreaConstant.KERISOM_AREA_2_END;
        } else if (line > StorageAreaConstant.KERISOM_AREA_2_END && line <= StorageAreaConstant.KERISOM_AREA_3_END) {
            return line2 > StorageAreaConstant.KERISOM_AREA_2_END && line2 <= StorageAreaConstant.KERISOM_AREA_3_END;
        } else if (line > StorageAreaConstant.KERISOM_AREA_3_END && line <= StorageAreaConstant.KERISOM_AREA_4_END) {
            return line2 > StorageAreaConstant.KERISOM_AREA_3_END && line2 <= StorageAreaConstant.KERISOM_AREA_4_END;
        } else {
            return false;
        }
    }

    /**
     * 武汉有机 货位分区 比较：是否处于同一区域
     *
     * @param row  排
     * @param row2 排
     * @return boolean
     * @author CalmLake
     * @date 2019/1/10 14:47
     */
    public static boolean isSameArea_YOUJI(int row, int row2) {
        if (row > StorageAreaConstant.ZERO && row <= StorageAreaConstant.WU_HAN_YOU_JI_AREA_1_END) {
            return row2 > StorageAreaConstant.ZERO && row2 <= StorageAreaConstant.WU_HAN_YOU_JI_AREA_1_END;
        } else if (row > StorageAreaConstant.WU_HAN_YOU_JI_AREA_1_END && row <= StorageAreaConstant.WU_HAN_YOU_JI_AREA_2_END) {
            return row2 > StorageAreaConstant.WU_HAN_YOU_JI_AREA_1_END && row2 <= StorageAreaConstant.WU_HAN_YOU_JI_AREA_2_END;
        } else if (row > StorageAreaConstant.WU_HAN_YOU_JI_AREA_2_END && row <= StorageAreaConstant.WU_HAN_YOU_JI_AREA_3_END) {
            return row2 > StorageAreaConstant.WU_HAN_YOU_JI_AREA_2_END && row2 <= StorageAreaConstant.WU_HAN_YOU_JI_AREA_3_END;
        } else {
            return false;
        }
    }
}
