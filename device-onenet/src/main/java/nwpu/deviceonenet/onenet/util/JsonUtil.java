package nwpu.deviceonenet.onenet.util;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Junho
 * @date 2022/7/16 16:00
 */
public class JsonUtil {

    /**
     * 读取json文件
     *
     * @param fileName 文件路径
     * @return 文件内容
     */
    public static String readJsonFile(String fileName) {
        String jsonStr = "";
        try {
            InputStream inputStream = JsonUtil.class.getResourceAsStream(fileName);
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = inputStream.read()) != -1) {
                sb.append((char) ch);
            }
            inputStream.close();
            jsonStr = sb.toString();
            return jsonStr;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
