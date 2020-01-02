package io.renren.wap.util;

import java.io.File;

/**
 * 删除文件
 *
 * @Author: CalmLake
 * @Date: 2019/4/24  10:43
 * @Version: V1.0.0
 **/
public class DeleteFileUtil {
    private static DeleteFileUtil deleteFileUtil = new DeleteFileUtil();

    public static DeleteFileUtil getInstance() {
        return deleteFileUtil;
    }

    private DeleteFileUtil() {
    }

    /**
     * 删除
     * @author CalmLake
     * @date 2019/4/24 10:47
     * @param path 路径
     * @return boolean
     */
    public boolean delete(String path) {
        try {
            File file = new File(path);
            if (!file.exists()) {
                return false;
            }
            if (file.isFile()) {
                return file.delete();
            }
            File[] files = file.listFiles();
            for (File f : files != null ? files : new File[0]) {
                if (f.isFile()) {
                    if (!f.delete()) {
                        System.out.println(f.getAbsolutePath() + " delete error!");
                        return false;
                    }
                } else {
                    if (!this.delete(f.getAbsolutePath())) {
                        return false;
                    }
                }
            }
            return file.delete();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
