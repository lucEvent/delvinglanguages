package com.delvinglanguages.net.internal;

public interface NetWork {

	public static final int FAILED = 0;
	public static final int OK = 1;
	public static final int OTHER = 2;

	public void datagram(int code, String message, Object packet);

}
