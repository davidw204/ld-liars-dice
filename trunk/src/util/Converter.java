package util;

public final class Converter {

	private Converter() {
	}

	private static final String HEXES = "0123456789ABCDEF";

	public static byte[] hexStringToByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) 
					+ Character.digit(s.charAt(i + 1), 16));
		}
		return data;
	}

	public static String getHex(byte[] raw) {
		if (raw == null) {
			return null;
		}
		final StringBuilder hex = new StringBuilder(2 * raw.length);
		for (final byte b : raw) {
			hex.append(HEXES.charAt((b & 0xF0) >> 4)).append(
					HEXES.charAt((b & 0x0F)));
		}
		return hex.toString();
	}

	public static boolean match(byte[] a, byte[] b) {
		boolean matches = false;
		int l = a.length;
		if (l == b.length) {
			matches = true;
			for (int i = 0; i < l; i++) {
				if (a[i] != b[i]) {
					matches = false;
					break;
				}
			}
		}
		return matches;
	}

	/* converts 2 or 1 bytes to integer */
	public static int toInt(byte[] bytes) {
		int value = 0;
		if (bytes.length == 2) {
			value = (int) (bytes[1] & 0xFF) + (int) ((bytes[0] << 8) & 0xFF00);
		} else if (bytes.length == 1) {
			value = (int) (bytes[0] & 0xFF);
		}
		return value;
	}

	/* convert ip address from byte representation to string */
	public static String getIpAddress(byte[] rawBytes) {
		int i = 4;
		String ipAddress = "";
		for (byte raw : rawBytes) {
			ipAddress += (raw & 0xFF);
			if (--i > 0) {
				ipAddress += ".";
			}
		}
		return ipAddress;
	}

}
