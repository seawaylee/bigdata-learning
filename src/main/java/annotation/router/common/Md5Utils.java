package annotation.router.common;

import java.security.MessageDigest;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author NikoBelic
 * @create 2018/2/6 15:51
 */
public class Md5Utils {

    private static final ThreadLocal<MessageDigest> MESSAGE_DIGEST_THREAD_LOCAL = new ThreadLocal();

    public static String md5Hex(final String data) {
        return Hex.encodeHexString(md5(StringUtils.getBytesUtf8(data)));
    }


    public static byte[] md5(final byte[] data) {
        return DigestUtils.md5(data);
    }
}
