package cn.studyjams.s1.sj46.tablayoutdemo;

import java.lang.reflect.Array;

/**
 * 日志封装类，提供建议log方法
 * @author 未知.
 * @since 1.0
 */
public class Log {
	
	/**
	 * 是否关闭日志输出
	 */
	private static boolean closeLog = false;
	
	public static final int LOG_LEVEL_V = 0X01;
	public static final int LOG_LEVEL_D = 0X02;
	public static final int LOG_LEVEL_I = 0X04;
	public static final int LOG_LEVEL_W = 0X08;
	public static final int LOG_LEVEL_E = 0X10;
	
	public static final int LOG_LEVEL_NONE = 0;
	public static final int LOG_LEVEL_ALL = 0X1F;
	
	private static int logLevel = LOG_LEVEL_ALL;
	private static String THIS_CLASS = Log.class.getName();
	
	public static void print(Object... os) {
		if(closeLog){
			return;
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < os.length; i++) {
			if (i != 0)
				sb.append(", ");
			sb.append(os[i]);
		}
		android.util.Log.v("print", sb.toString());
	}
	
	public static void v() {
		v(null);
	}
	
	/**
	 * 以V形式打印log
	 * @param o
	 */
	public static void v(Object o) {
		if(closeLog){
			return;
		}
		if ((logLevel & LOG_LEVEL_V) != 0)
			log(android.util.Log.VERBOSE, o);
	}

	public static void d() {
		d(null);
	}
	/**
	 * 以d形式打印log
	 * @param o
	 */
	public static void d(Object o) {
		if(closeLog){
			return;
		}
		if ((logLevel & LOG_LEVEL_D) != 0)
			log(android.util.Log.DEBUG, o);
	}

	public static void i() {
		i(null);
	}

	/**
	 * 以i形式打印log
	 * @param o
	 */
	public static void i(Object o) {
		if(closeLog){
			return;
		}
		if ((logLevel & LOG_LEVEL_I) != 0)
			log(android.util.Log.INFO, o);
	}

	public static void w() {
		w(null);
	}
	
	/**
	 * 以w形式打印log
	 * @param o
	 */
	public static void w(Object o) {
		if(closeLog){
			return;
		}
		if ((logLevel & LOG_LEVEL_W) != 0)
			log(android.util.Log.WARN, o);
	}

	public static void e() {
		e(null);
	}
	/**
	 * 以e形式打印log
	 * @param o
	 */
	public static void e(Object o) {
		if(closeLog){
			return;
		}
		if ((logLevel & LOG_LEVEL_E) != 0)
			log(android.util.Log.ERROR, o);
	}
	
	private static String[] getCaller() {
		Throwable ex = new Throwable();
		StackTraceElement[] trace = ex.getStackTrace();
		for (int i = 3; i< trace.length; i++) {
			String className = trace[i].getClassName();
			if (THIS_CLASS.equals(className))
				continue;
			className = className.substring(className.lastIndexOf('.') + 1);
			if (className.indexOf('$') > 0)
				className = className.substring(0, className.indexOf('$'));
			return new String[] {className, trace[i].getMethodName(), String.valueOf(trace[i].getLineNumber())};
		}
		return new String[] {null, null, null};
	}
	
	private static void log(int type, Object o) {
		if(closeLog){
			return;
		}
		String[] caller = getCaller();
		String tag = null;
		String msg = null;
		Throwable e = null;
		
//		tag = "[" + caller[0] + "]";
		//去除"[]",方便进行日志过滤时操作方便 update by lqq 2011-10-13
		tag = caller[0];
		
		msg = caller[2] + '[' + caller[1] + ']';
		if (o == null || o instanceof Throwable) {
			if (o != null)
				msg += '\n' + android.util.Log.getStackTraceString((Throwable)o);
		} else if (o.getClass().isArray()) {
			int len = Array.getLength(o);
			String name = o.getClass().getSimpleName();
			name = name.substring(0, name.length() - 1) + len + "]";
			msg += name;
			Object item;
			if (len > 0 && (item = Array.get(o, 0)) != null) {
				msg += ": " + item.toString();
			}
		} else {
			msg += String.valueOf(o);
		}
		log(type, tag, msg, e);
	}
	
	private static void log(int type, String tag, String msg, Throwable e) {
		if(closeLog){
			return;
		}
		android.util.Log.println(type, tag, msg);
	}
	
	public static void setLogLevel(int level) {
		logLevel = level & LOG_LEVEL_ALL;
	}
	
	private static long timePoint;
	public static void t(long time) {
		if(closeLog){
			return;
		}
		Log.timePoint = time;
		Log.d();
	}
	public static void t() {
		if(closeLog){
			return;
		}
		long now = System.currentTimeMillis();
		if (timePoint == 0)
			timePoint = now;
		Log.d(now - timePoint);
	}
	
	public static void printStackTrace() {
		if(closeLog){
			return;
		}
		Throwable ex = new Throwable();
		StackTraceElement[] trace = ex.getStackTrace();
		StringBuilder sb = new StringBuilder();
		String className;
		for (int i = 1, j = Math.min(100, trace.length); i < j; i++) {
			sb.setLength(0);
			sb.append(i).append("@").append(trace[i].getMethodName()).append('\t').append(trace[i].getLineNumber());
			className = trace[i].getClassName();
			className = className.substring(className.lastIndexOf('.') + 1);
			android.util.Log.println(i == 1 ? android.util.Log.INFO : android.util.Log.VERBOSE, className, sb.toString());
		}
	}
	
	/**
	 * 关闭日志输出.当需要关闭日志输出时调用此方法.<br>
	 * 应用中只要在入口程序中调用本方法设置一次状态即可.
	 */
	public static void closeLog(){
		closeLog = true;
	}
	
	private Log() {//不需要实例
	}
}
