package remix.myplayer.util;

import org.apache.commons.lang3.StringUtils;

import java.io.File;

/**
 * 其他工具类
 *
 * @author HP
 */

public class OtherUtil {

    /**
     * @param url 文件url
     * @return 文件名
     */
    public static String getFileName(String url) {
        if (url != null) {
            String[] split = StringUtils.split(url, File.separator);
            return split[split.length - 1].replace(".mp3", "");
        }
        return "";
    }
}
