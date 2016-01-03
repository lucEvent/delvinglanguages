package com.delvinglanguages.net.internal;

public interface NetWork {

	public static final int FAILED = 0;
	public static final int OK = 1;
	public static final int ERROR = 2;
	public static final int ACTION = 3;
	public static final int DATA = 4;
	public static final int OTHER = 5;

	public void datagram(int code, String message, Object packet);

}
