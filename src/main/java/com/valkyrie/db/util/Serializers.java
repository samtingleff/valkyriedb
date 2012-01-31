package com.valkyrie.db.util;

/**
 * Primitive serializers copied from DataOutputStream/DataInputStream.
 * 
 */
public class Serializers {

	private Serializers() { }

	public static byte[] shortToBytes(short v) {
		return new byte[] {
				(byte) ((v >>> 8) & 0xFF),
				(byte) ((v >>> 0) & 0xFF)};
	}

	public static short bytesToShort(byte[] bytes, int offset) {
        return (short) ((bytes[offset] << 8) + (bytes[offset + 1] << 0));
	}

	public static byte[] intToBytes(int v) {
		return new byte[] {
				(byte) ((v >>> 24) & 0xFF),
				(byte) ((v >>> 16) & 0xFF),
				(byte) ((v >>>  8) & 0xFF),
				(byte) ((v >>>  0) & 0xFF)};
	}

	public static int bytesToInt(byte[] bytes, int offset) {
		return ((bytes[offset] << 24)
				+ (bytes[offset + 1] << 16)
				+ (bytes[offset + 2] << 8)
				+ (bytes[offset + 3] << 0));
	}

	public static byte[] longToBytes(long v) {
		return new byte[] {
				(byte)(v >>> 56),
				(byte)(v >>> 48),
				(byte)(v >>> 40),
				(byte)(v >>> 32),
				(byte)(v >>> 24),
				(byte)(v >>> 16),
				(byte)(v >>>  8),
				(byte)(v >>>  0)};
	}

	public static long bytesToLong(byte[] bytes, int offset) {
		return (((long)bytes[offset] << 56) +
				((long)(bytes[offset + 1] & 255) << 48) +
				((long)(bytes[offset + 2] & 255) << 40) +
				((long)(bytes[offset + 3] & 255) << 32) +
				((long)(bytes[offset + 4] & 255) << 24) +
				((bytes[offset + 5] & 255) << 16) +
				((bytes[offset + 6] & 255) <<  8) +
				((bytes[offset + 7] & 255) <<  0));
		
	}

	public static byte[] floatToBytes(float v) {
		return intToBytes(Float.floatToIntBits(v));
	}

	public static float bytesToFloat(byte[] bytes, int offset) {
		return Float.intBitsToFloat(bytesToInt(bytes, offset));
	}

	public static byte[] doubleToBytes(double v) {
		return longToBytes(Double.doubleToLongBits(v));
	}

	public static double bytesToDouble(byte[] bytes, int offset) {
		return Double.longBitsToDouble(bytesToLong(bytes, offset));
	}
}
