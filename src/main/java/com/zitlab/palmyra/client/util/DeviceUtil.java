/**
 * <LICENSE/>
 */
package com.zitlab.palmyra.client.util;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;

/**
 * @author ksvraja
 *
 */
public class DeviceUtil {
	public static String getLowestMac() {
		String minMac = null;
		Long minMacL = null;

		try {			
			Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
			while (networkInterfaces.hasMoreElements()) {
				NetworkInterface networkInterface = networkInterfaces.nextElement();
				if (null == networkInterface.getHardwareAddress() || null != networkInterface.getParent())
					continue;
				String address = getMacAddress(networkInterface);
				if (null != address) {
					if (null == minMac) {
						minMac = address;
						minMacL = Long.parseLong(minMac, 16);
					} else {
						Long temp = Long.parseLong(address, 16);
						if (temp < minMacL) {
							minMac = address;
							minMacL = temp;
						}
					}			
				}
			}

		} catch (Throwable t) {
			t.printStackTrace();
		}
		return minMac;
	}

	public static String getMD5EncryptedValue(String value) {
		final byte[] defaultBytes = value.getBytes();
		try {
			final MessageDigest md5MsgDigest = MessageDigest.getInstance("MD5");
			md5MsgDigest.reset();

			md5MsgDigest.update(defaultBytes);
			final byte messageDigest[] = md5MsgDigest.digest();
			final StringBuffer hexString = new StringBuffer();
			for (final byte element : messageDigest) {
				final String hex = Integer.toHexString(0xFF & element);
				if (hex.length() == 1) {
					hexString.append('0');
				}
				hexString.append(hex);
			}
			value = hexString + "";
		} catch (final NoSuchAlgorithmException nsae) {
			nsae.printStackTrace();
		}
		return value;
	}

	private static String getMacAddress(NetworkInterface network) {
		String address = null;
		try {
			String name = network.getName();
			if (null == name || name.contains("docker") || name.contains("luetooth"))
				return null;

			byte[] mac = network.getHardwareAddress();
			if (isVMMac(mac))
				return null;
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < mac.length; i++) {
				sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "" : ""));
			}

			address = sb.toString();

		} catch (SocketException ex) {

			ex.printStackTrace();

		}

		return address;
	}

	private static boolean isVMMac(byte[] mac) {
		if (null == mac)
			return false;
		byte invalidMacs[][] = { { 0x00, 0x05, 0x69 }, // VMWare
				{ 0x00, 0x1C, 0x14 }, // VMWare
				{ 0x00, 0x0C, 0x29 }, // VMWare
				{ 0x00, 0x50, 0x56 }, // VMWare
				{ 0x08, 0x00, 0x27 }, // Virtualbox
				{ 0x0A, 0x00, 0x27 }, // Virtualbox
				{ 0x00, 0x03, (byte) 0xFF }, // Virtual-PC
				{ 0x00, 0x15, 0x5D } // Hyper-V
		};

		for (byte[] invalid : invalidMacs) {
			if (invalid[0] == mac[0] && invalid[1] == mac[1] && invalid[2] == mac[2])
				return true;
		}

		return false;
	}
}
