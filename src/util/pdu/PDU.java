package util.pdu;

import static util.CommandIDs.PID;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import org.ksmsa.utils.io.ByteBuilder;

public abstract class PDU {

	private ByteBuilder bytes;
	private boolean valid = false;

	public PDU() {
		bytes = new ByteBuilder();
		bytes.append(PID);
	}

	/* returns pdu as byte array */
	public byte[] getBytes() {
//		ByteBuffer bf = bytes.asByteBuffer();
//		byte[] b = new byte[1];
//		if (bf.hasArray()) {
//			b = new byte[bf.remaining()];
//			bf.get(b);
//		}
//		return b;
		return bytes.getBytes();
	}

	/* traces back pdu attributes from byte array */
	protected void reverseConstructor() {
		// Override method
	}

	/* resets the byte builder */
	protected void reset() {
		bytes = new ByteBuilder();
	}

	/* cast int to 2 bytes */
	private byte[] cit2b(int x) {
		return new byte[] { (byte) ((x >> 8) & 0xFF), (byte) (x & 0xFF) };
	}

	/* cast int to byte */
	private byte citb(int x) {
		return (byte) (0xFF & x);
	}

	/* inserts total message length field to pdu after byte#5 */
	protected void addLength() {
		bytes.insert(cit2b(bytes.getLength() - 5), 5, 0, 2);
	}

	/* adds strings to pdu with leading length specification */
	protected void addString(String s) {
		byte[] b = "player".getBytes();
		try {
			b = s.getBytes("UTF8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		bytes.append(citb(b.length));
		bytes.append(b);
	}

	/* adds integer to pdu with length of one byte */
	protected void addByte(int x) {
		bytes.append(citb(x));
	}

	/* adds integer to pdu with length of two bytes */
	protected void add2Bytes(int x) {
		bytes.append(cit2b(x));
	}

	/* adds bytes to pdu */
	protected void addBytes(byte[] b) {
		bytes.append(b);
	}

	/* converts 2 or 1 bytes to integer */
	protected int toInt(byte[] bytes) {
		int value = 0;
		if (bytes.length == 2) {
			value = (int) (bytes[1] & 0xFF) + (int) ((bytes[0] << 8) & 0xFF00);
		} else if (bytes.length == 1) {
			value = (int) (bytes[0] & 0xFF);
		}
		return value;
	}
	
	/* checks whether to byte arrays match */
	protected boolean match(byte[] a, byte[] b) {
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
	
	/* creates string from utf8 formatted byte array */
	protected String fromUTF8(byte[] bytes) {
		String s = "";
		try {
			s = new String(bytes, "UTF8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}

	/* converts ip address from byte representation to string */
	protected String getIpAddress(byte[] rawBytes) {
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

	/* converts ip address from string representation to bytes */
	protected byte[] getIpAddress(String ip) {
		byte[] addr = null;
		try {
			addr = InetAddress.getByName(ip).getAddress();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return addr;
	}
	
	/* validate pdu after creating especially from bytes */
	protected void valid() {
		this.valid = true;
	}
	
	/* check whether pdu is valid or not */
	public boolean isValid(){
		return this.valid;
	}
}
