package vip.sujianfeng.utils.comm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

public class GuidUtils {
	private static Logger logger = LoggerFactory.getLogger(GuidUtils.class);
	private String valueBeforeMD5 = "";
	private String valueAfterMD5 = "";
	private static Random myRand = null;
	private static SecureRandom mySecureRand = null;
	private static String s_id = "";
	private static final int PAD_BELOW = 0x10;
	private static final int TWO_BYTES = 0xFF;

	static {
		mySecureRand = new SecureRandom();
		long secureInitializer = mySecureRand.nextLong();
		myRand = new Random(secureInitializer);

		try {
			s_id = InetAddress.getLocalHost().toString();
		} catch (UnknownHostException e) {
			logger.error(e.toString(), e);
		}
	}

	public GuidUtils() {
		getRandomGUID(false);
	}

	public GuidUtils(boolean secure) {
		getRandomGUID(secure);
	}

	private void getRandomGUID(boolean secure) {
		MessageDigest md5 = null;
		StringBuffer sbValueBeforeMD5 = new StringBuffer(128);

		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			logger.error(e.toString(), e);
		}

		try {
			long time = System.currentTimeMillis();
			long rand = secure ? mySecureRand.nextLong() : myRand.nextLong();

			sbValueBeforeMD5.append(s_id);
			sbValueBeforeMD5.append(":");
			sbValueBeforeMD5.append(Long.toString(time));
			sbValueBeforeMD5.append(":");
			sbValueBeforeMD5.append(Long.toString(rand));

			valueBeforeMD5 = sbValueBeforeMD5.toString();
			md5.update(valueBeforeMD5.getBytes());

			byte[] array = md5.digest();
			StringBuffer sb = new StringBuffer(32);
			for (int j = 0; j < array.length; ++j) {
				int b = array[j] & TWO_BYTES;
				if (b < PAD_BELOW) {
					sb.append('0');
				}

				sb.append(Integer.toHexString(b));
			}

			valueAfterMD5 = sb.toString();

		} catch (Exception e) {
			logger.error(e.toString(), e);
		}
	}

	public String newGuid() {
		String raw = valueAfterMD5.toUpperCase();
		StringBuffer sb = new StringBuffer(64);
		sb.append(raw.substring(0, 8));
		//sb.append("-");
		sb.append(raw.substring(8, 12));
		//sb.append("-");
		sb.append(raw.substring(12, 16));
		//sb.append("-");
		sb.append(raw.substring(16, 20));
		//sb.append("-");
		sb.append(raw.substring(20));
		return sb.toString();
	}

	public static String buildGuid(){
		return new GuidUtils().newGuid();
	}

	public static void main(String[] args){
		System.out.println(new GuidUtils().newGuid());
	}
}
