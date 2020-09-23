package com.k;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @Description TODO
 * @Author 王一鸣
 * @Date 2020/8/17 14:44
 **/
public class 处理图片 {
    /**
     * 字符串转图片
     *
     * @param imgStr   --->图片字符串
     * @param filename --->图片名
     * @return boolean
     */
    public static boolean generateImage(String imgStr, String filename) {

        if (imgStr == null) {
            return false;
        }
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            // 解密
            byte[] b = decoder.decodeBuffer(imgStr);
            // 处理数据
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            OutputStream out = new FileOutputStream("E:\\A工作相关文档\\项目\\山东\\济南\\济南一网通办\\济南一网通办\\前台页面\\" + filename);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;

    }

    /**
     * 图片转字符串
     *
     * @param filePath --->文件路径
     * @return String
     */
    public static String getImageStr(String filePath) {
        InputStream inputStream;
        byte[] data = null;
        try {
            inputStream = new FileInputStream(filePath);
            data = new byte[inputStream.available()];
            inputStream.read(data);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 加密
        BASE64Encoder encoder = new BASE64Encoder();
        assert data != null;
        return encoder.encode(data);
    }

    /*
     * 测试代码
     */
    public static void main(String[] args) {
        String imageStr = getImageStr("E:\\A工作相关文档\\项目\\山东\\济南\\济南一网通办\\济南一网通办\\前台页面\\a-服务-1.0.png");
        System.out.println(imageStr);
        boolean generateImage = generateImage(imageStr, "001.png");
        System.out.println(generateImage);
    }
}
