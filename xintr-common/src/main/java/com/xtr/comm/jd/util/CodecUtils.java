package com.xtr.comm.jd.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.digest.DigestUtils;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * 编码工具类.
 * 编码HEX, Base64
 * 数据摘要MD5, SHA-1, SHA-256
 * User: wychenzhe@chinabank.com.cn
 * Date: 13-12-26
 *
 * @version 1.0
 * @since 1.0
 */
public class CodecUtils {
    private static final String[] hexStrings;

    static {
        hexStrings = new String[256];
        for (int i = 0; i < 256; i++) {
            StringBuilder d = new StringBuilder(2);
            char ch = Character.forDigit(((byte) i >> 4) & 0x0F, 16);
            d.append(Character.toUpperCase(ch));
            ch = Character.forDigit((byte) i & 0x0F, 16);
            d.append(Character.toUpperCase(ch));
            hexStrings[i] = d.toString();
        }
    }

    /**
     * 将字节数组转换成HEX String
     *
     * @param b
     * @return HEX String
     */
    public static String hexString(byte[] b) {
        StringBuilder d = new StringBuilder(b.length * 2);
        for (byte aB : b) {
            d.append(hexStrings[(int) aB & 0xFF]);
        }
        return d.toString();
    }

    /**
     * 将字节数组转换成HEX String
     *
     * @param b
     * @param offset 起始位置
     * @param len    长度
     * @return HEX String
     */
    public static String hexString(byte[] b, int offset, int len) {
        StringBuilder d = new StringBuilder(len * 2);
        len += offset;
        for (int i = offset; i < len; i++) {
            d.append(hexStrings[(int) b[i] & 0xFF]);
        }
        return d.toString();
    }


    /**
     * 将HEX String转换为字节数组
     *
     * @param s HEX String
     * @return 字节数组
     */
    public static byte[] hex2byte(String s) {
        int len = s.length();
        if (len % 2 != 0) {
            s = '0' + s;
            len++;
        }
        byte[] d = new byte[len >> 1];
        byte[] b = s.getBytes();
        for (int i = 0; i < len; i++) {
            int shift = i % 2 != 0 ? 0 : 4;
            d[i >> 1] |= Character.digit((char) b[i], 16) << shift;
        }
        return d;
    }

    /**
     * 对两个字节数组异或操作
     *
     * @param op1 byteArray1
     * @param op2 byteArray2
     * @return 异或结果，长度等于两个数组中短的
     */
    public static byte[] xor(byte[] op1, byte[] op2) {
        byte[] result = null;
        // Use the smallest array
        if (op2.length > op1.length) {
            result = new byte[op1.length];
        } else {
            result = new byte[op2.length];
        }
        for (int i = 0; i < result.length; i++) {
            result[i] = (byte) (op1[i] ^ op2[i]);
        }
        return result;
    }

    /**
     * 对两个HEX String以异或方式操作返回HEX String
     *
     * @param op1 hexstring 1
     * @param op2 hexstring 2
     * @return 异或结果，长度等于两个String中短的
     */
    public static String hexor(String op1, String op2) {
        byte[] xor = xor(hex2byte(op1), hex2byte(op2));
        return hexString(xor);
    }

    /**
     * 对字节数组截取长度
     *
     * @param array  the byte[] to be trimmed
     * @param length the wanted length
     * @return the trimmed byte[]
     */
    public static byte[] trim(byte[] array, int length) {
        byte[] trimmedArray = new byte[length];
        System.arraycopy(array, 0, trimmedArray, 0, length);
        return trimmedArray;
    }

    /**
     * 拼接两个字节数组
     *
     * @param array1
     * @param array2
     * @return the concatenated array
     */
    public static byte[] concat(byte[] array1, byte[] array2) {
        byte[] concatArray = new byte[array1.length + array2.length];
        System.arraycopy(array1, 0, concatArray, 0, array1.length);
        System.arraycopy(array2, 0, concatArray, array1.length, array2.length);
        return concatArray;
    }

    /**
     * 生成显示友好的字节数组
     *
     * @param b a byte[] buffer
     * @return hexdump
     */
    public static String hexdump(byte[] b) {
        return hexdump(b, 0, b.length);
    }

    /**
     * 生成显示友好的字节数组
     *
     * @param b      a byte[] buffer
     * @param offset starting offset
     * @param len    the Length
     * @return hexdump
     */
    public static String hexdump(byte[] b, int offset, int len) {
        StringBuilder sb = new StringBuilder();
        StringBuilder hex = new StringBuilder();
        StringBuilder ascii = new StringBuilder();
        String sep = "  ";
        String lineSep = System.getProperty("line.separator");

        for (int i = offset; i < len; i++) {
            hex.append(hexStrings[(int) b[i] & 0xFF]);
            hex.append(' ');
            char c = (char) b[i];
            ascii.append((c >= 32 && c < 127) ? c : '.');

            int j = i % 16;
            switch (j) {
                case 7:
                    hex.append(' ');
                    break;
                case 15:
                    sb.append(hexOffset(i));
                    sb.append(sep);
                    sb.append(hex.toString());
                    sb.append(' ');
                    sb.append(ascii.toString());
                    sb.append(lineSep);
                    hex = new StringBuilder();
                    ascii = new StringBuilder();
                    break;
            }
        }
        if (hex.length() > 0) {
            while (hex.length() < 49)
                hex.append(' ');

            sb.append(hexOffset(len));
            sb.append(sep);
            sb.append(hex.toString());
            sb.append(' ');
            sb.append(ascii.toString());
            sb.append(lineSep);
        }
        return sb.toString();
    }

    private static String hexOffset(int i) {
        i = (i >> 4) << 4;
        int w = i > 0xFFFF ? 8 : 4;
        return StringUtils.padleft(Integer.toString(i, 16), w, '0');
    }

    /**
     * 对数据进行MD5算法做摘要，
     *
     * @param data 待计算的字节数组
     * @return 摘要结果
     */
    public static byte[] md5(byte[] data) {
        return DigestUtils.md5(data);
    }

    /**
     * 对数据进行MD5算法做摘要
     *
     * @param data 待计算的字符串，默认是UTF8编码
     * @return 摘要结果
     */
    public static String md5Hex(String data) throws UnsupportedEncodingException {
        return md5Hex(data, "UTF-8");
    }

    /**
     * 对数据进行MD5算法做摘要
     *
     * @param data    待计算的字符串
     * @param charset 字符串的编码
     * @return 摘要结果
     */
    public static String md5Hex(String data, String charset) throws UnsupportedEncodingException {
        byte[] digest = md5(data.getBytes(charset));
        return hexString(digest);
    }

    /**
     * 对数据进行SHA1算法做摘要
     *
     * @param data 待计算的数据
     * @return 摘要结果
     */
    public static byte[] sha1(byte[] data) {
        return DigestUtils.sha1(data);
    }

    /**
     * 对数据进行SHA1算法做摘要
     *
     * @param data 待计算的数据
     * @return 摘要结果
     */
    public static String sha1Hex(String data) throws UnsupportedEncodingException {
        return sha1Hex(data, "UTF-8");
    }

    /**
     * 对数据进行SHA1算法做摘要
     *
     * @param data    待计算的数据
     * @param charset 字符串的编码
     * @return 摘要结果
     */
    public static String sha1Hex(String data, String charset) throws UnsupportedEncodingException {
        byte[] digest = sha1(data.getBytes(charset));
        return hexString(digest);
    }


    /**
     * 对数据进行指定算法的数据摘要
     *
     * @param algorithm 算法名，如MD2, MD5, SHA-1, SHA-256, SHA-512
     * @param data      待计算的数据
     * @return 摘要结果
     */
    public static byte[] digest(String algorithm, byte[] data) {
        return DigestUtils.getDigest(algorithm).digest(data);
    }


    /**
     * 对数据进行指定算法的数据摘要
     *
     * @param algorithm 算法名，如MD2, MD5, SHA-1, SHA-256, SHA-512
     * @param data      待计算的数据
     * @return 摘要结果
     */
    public static String digestHex(String algorithm, String data) throws UnsupportedEncodingException {
        return digestHex(algorithm, data, "UTF-8");
    }

    /**
     * 对数据进行指定算法的数据摘要
     *
     * @param algorithm 算法名，如MD2, MD5, SHA-1, SHA-256, SHA-512
     * @param data      待计算的数据
     * @param charset   字符串的编码
     * @return 摘要结果
     */
    public static String digestHex(String algorithm, String data, String charset) throws UnsupportedEncodingException {
        byte[] digest = digest(algorithm, data.getBytes(charset));
        return hexString(digest);
    }

    /**
     * 对数据进行BASE64编码
     * @param data    待处理数据
     * @param urlSafe 是否是URL安全的，如果为true，则将会被URL编码的'+', '/'转成'-', '_'
     * @return Base64编码结果
     */
    public static byte[] encodeBase64(byte[] data, boolean urlSafe) {
        Base64 base64 = new Base64(urlSafe);
        return base64.encode(data);
    }

    /**
     * 对数据进行BASE64编码
     *
     * @param data    待处理数据
     * @param oneLine 是否是一行
     * @return
     */
    public static String encodeBase64String(String data, boolean oneLine) throws UnsupportedEncodingException {
        return encodeBase64String(data, "UTF8", false, oneLine);
    }

    /**
     * 对数据进行BASE64编码，默认是UTF8编码。
     *
     * @param data    待处理数据
     * @param charset 字符串
     * @param urlSafe 是否是URL安全的，如果为true，则将会被URL编码的'+', '/'转成'-', '_'
     * @param oneLine 是否是一行
     * @return Base64编码结果
     */
    public static String encodeBase64String(String data, String charset, boolean urlSafe, boolean oneLine) throws UnsupportedEncodingException {
        Base64 base64 = oneLine ?
                new Base64(BaseNCodec.MIME_CHUNK_SIZE, null, urlSafe) : new Base64(urlSafe);
        return base64.encodeToString(data.getBytes(charset));
    }

    public static String encodeBase64String(byte[] data, String charset, boolean urlSafe, boolean oneLine) throws UnsupportedEncodingException {
        Base64 base64 = oneLine ?
                new Base64(BaseNCodec.MIME_CHUNK_SIZE, null, urlSafe) : new Base64(urlSafe);
        return base64.encodeToString(data);
    }

    /**
     * 对数据进行BASE64解码
     *
     * @param base64Data Base64数据
     * @param urlSafe    是否是URL安全的，如果为true，则将会被URL编码的'+', '/'转成'-', '_'
     * @return 解码后数据
     */
    public static byte[] decodeBase64(byte[] base64Data, boolean urlSafe) {
        Base64 base64 = new Base64(urlSafe);
        return base64.decode(base64Data);
    }

    /**
     * 对数据进行BASE64解码，默认是UTF8编码。
     *
     * @param base64Data Base64数据
     * @param oneLine    是否是一行
     * @return
     */
    public static String decodeBase64(String base64Data, boolean oneLine) throws UnsupportedEncodingException {
        return decodeBase64(base64Data, "UTF-8", false, oneLine);
    }

    /**
     * 对数据进行BASE64解码
     *
     * @param base64Data Base64数据
     * @param charset    解码的编码格式
     * @param urlSafe    是否是URL安全的，如果为true，则将会被URL编码的'+', '/'转成'-', '_'
     * @param oneLine    是否是一行
     * @return 解码后数据
     */
    public static String decodeBase64(String base64Data, String charset, boolean urlSafe, boolean oneLine) throws UnsupportedEncodingException {
        Base64 base64 = oneLine ?
                new Base64(BaseNCodec.MIME_CHUNK_SIZE, null, urlSafe) : new Base64(urlSafe);
        byte[] binaryData = base64.decode(base64Data);
        return new String(binaryData, charset);
    }

    /**
     * 获取密钥
     *
     * @param hexKeyValue
     * @return
     * @throws InvalidKeyException
     */
    public static SecretKey getDESKey(String hexKeyValue) throws InvalidKeyException {
        if (hexKeyValue == null) {
            throw new InvalidKeyException("key value is null. ");
        }
        switch (hexKeyValue.length()) {
            case 16:
                return new SecretKeySpec(hex2byte(hexKeyValue), "DES");
            case 32:
                return new SecretKeySpec(hex2byte(hexKeyValue + hexKeyValue.substring(0, 16)), "DESede");
            case 48:
                return new SecretKeySpec(hex2byte(hexKeyValue), "DESede");
            default:
                throw new InvalidKeyException("invalid key: " + hexKeyValue);
        }
    }

    /**
     * DES解密
     *
     * @param encryptedData
     * @param hexKeyValue
     * @return
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public static byte[] decryptDES_ECB_PCSC5(byte[] encryptedData, String hexKeyValue)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        SecretKey key = getDESKey(hexKeyValue);
        String transformation = key.getAlgorithm() + "/ECB/PKCS5Padding";
//        System.out.println("transformation = " + transformation);
        Cipher cipher = Cipher.getInstance(transformation);
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(encryptedData);
    }

    /**
     * DES加密
     *
     * @param data
     * @param hexKeyValue
     * @return
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public static byte[] encryptDES_ECB_PCSC5(byte[] data, String hexKeyValue)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        SecretKey key = getDESKey(hexKeyValue);
        String transformation = key.getAlgorithm() + "/ECB/PKCS5Padding";
//        System.out.println("transformation = " + transformation);
        Cipher cipher = Cipher.getInstance(transformation);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(data);
    }

    public static void main(String[] args) throws Exception {
        String base64String = "fx8NzklezWeb4/fCKTGe6SZDaHwLZ573";
//        System.out.println("base64String = " + base64String);
        // 将Base64转为HEX编码的字符串
        String hexString = hexString(decodeBase64(base64String.getBytes(), false));
//        System.out.println("hexString = " + hexString);
        // 将HEX转为Base64编码的字符串
        base64String = new String(encodeBase64(hex2byte(hexString), false));
//        System.out.println("base64String = " + base64String);
//        System.out.println("7F1F0DCE495ECD67");

        String b = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<DATA>\n" +
                "    <CARD>\n" +
                "        <BANK>CMS</BANK>\n" +
                "        <TYPE>D</TYPE>\n" +
                "        <NO>6225885710399221</NO>\n" +
                "        <EXP></EXP>\n" +
                "        <CVV2></CVV2>\n" +
                "        <NAME>杭一</NAME>\n" +
                "        <IDTYPE>I</IDTYPE>\n" +
                "        <IDNO>330109199001010551</IDNO>\n" +
                "        <PHONE>15121212121</PHONE>\n" +
                "    </CARD>\n" +
                "    <TRADE>\n" +
                "        <TYPE>V</TYPE>\n" +
                "        <ID>1389601619097</ID>\n" +
                "        <AMOUNT>500</AMOUNT>\n" +
                "        <CURRENCY>CNY</CURRENCY>\n" +
                "    </TRADE>\n" +
                "</DATA>";
        hexString = "7F1F0DCE495ECD67";
        byte[] bytes = b.getBytes("UTF8");
//        System.out.println( hexdump(bytes) );
        bytes = encryptDES_ECB_PCSC5(bytes, hexString);
//        System.out.println( hexdump(bytes) );
//        System.out.println("new String(encodeBase64(bytes, false)) = " + new String(encodeBase64(bytes, false)));
        bytes = decryptDES_ECB_PCSC5(bytes, hexString);
        String xml = new String(bytes, "UTF-8");
//        System.out.println("xml = " + xml);
    }
}
