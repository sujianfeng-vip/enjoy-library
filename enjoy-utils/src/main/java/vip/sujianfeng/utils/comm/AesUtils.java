package vip.sujianfeng.utils.comm;


import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

public class AesUtils {

    public static String AESEncode(String seed, String content) throws Exception  {
        //1.Construct a key generator, specifying the AES algorithm as case insensitive
        KeyGenerator keygen = KeyGenerator.getInstance("AES");
        //2.Initialize key generator according to ecnodeRules rules
        //Generate a 128 bit random source based on the passed in byte array
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");

        random.setSeed(seed.getBytes());
        keygen.init(128, random);
        //3.Generate original symmetric key
        SecretKey original_key = keygen.generateKey();
        //4.Obtain the byte array of the original symmetric key
        byte[] raw = original_key.getEncoded();
        //5.Generate AES key based on byte array
        SecretKey key = new SecretKeySpec(raw, "AES");
        //6.AES self generated cipher according to specified algorithm
        Cipher cipher = Cipher.getInstance("AES");
        //7.Initialize the cipher, with the first parameter being the encryption (Encrypt_mode) or decryption (Decrypt_mode) operation, and the second parameter being the KEY used
        cipher.init(Cipher.ENCRYPT_MODE, key);
        //8.Obtain the byte array of encrypted content (set to utf-8 here), otherwise if there is a mixture of Chinese and English in the content, it will be decrypted as garbled code
        byte[] byte_encode = content.getBytes(StandardCharsets.UTF_8);
        //9.According to the initialization method of the cipher - encryption: encrypt the data
        byte[] byte_AES = cipher.doFinal(byte_encode);
        //10.Convert encrypted data into a string
        //If you use Base64Encoder here, you won't be able to find the package
        //Solution:
        //In the Build path of the project, first remove the JRE System Library, then add the JRE System Library. After recompiling, everything will be normal.
        //11.Return string
        return Base64UtilsEx.encodeStr(byte_AES);

    }

    public static byte[] aesEncryptToBytes(String content, String encryptKey) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128, new SecureRandom(encryptKey.getBytes()));

        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(kgen.generateKey().getEncoded(), "AES"));

        return cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
    }

    public static String AESDecode(String seed, String content) throws Exception {
        //1.Construct a key generator, specifying the AES algorithm as case insensitive
        KeyGenerator keygen = KeyGenerator.getInstance("AES");
        //2.Initialize key generator according to ecnodeRules rules
        //Generate a 128 bit random source based on the passed in byte array
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(seed.getBytes());
        keygen.init(128, random);
        //3.Generate original symmetric key
        SecretKey original_key = keygen.generateKey();
        //4.Obtain the byte array of the original symmetric key
        byte[] raw = original_key.getEncoded();
        //5.Generate AES key based on byte array
        SecretKey key = new SecretKeySpec(raw, "AES");
        //6.AES self generated cipher according to specified algorithm
        Cipher cipher = Cipher.getInstance("AES");
        //7.Initialize the cipher, with the first parameter being the encryption (Encrypt_mode) or decryption (Decrypt_mode) operation, and the second parameter being the KEY used
        cipher.init(Cipher.DECRYPT_MODE, key);
        //8.Decode the encrypted and encoded content into a byte array
        byte[] byte_content = Base64UtilsEx.decode(content);
        //decrypt
        byte[] byte_decode = cipher.doFinal(byte_content);
        return new String(byte_decode, StandardCharsets.UTF_8);
    }

    public static String aesDecryptByBytes(byte[] encryptBytes, String decryptKey) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128, new SecureRandom(decryptKey.getBytes()));

        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(kgen.generateKey().getEncoded(), "AES"));
        byte[] decryptBytes = cipher.doFinal(encryptBytes);

        return new String(decryptBytes);
    }

    public static String aesDecrypt(String encryptStr, String decryptKey) throws Exception {
        return StringUtilsEx.isEmpty(encryptStr) ? null : aesDecryptByBytes( Base64UtilsEx.encode(encryptStr.getBytes()), decryptKey);
    }

    public static String aesEncrypt(String content, String encryptKey) throws Exception {
        return Base64UtilsEx.encodeStr(aesEncryptToBytes(content, encryptKey));
    }

    public static void main(String[] args) {
        String content = "feature remove someone quick insect earth receive click double spot harvest wonder churn attack this endless voyage outdoor unveil stuff kiss robot sausage tooth";
        String seed = "123";
        System.out.println("content:" + content);
        String encryptString = null;
        try {
            encryptString = AESEncode(seed, content);
            System.out.println(encryptString);
            String decryptString = AESDecode(seed, encryptString);
            System.out.println(decryptString);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
