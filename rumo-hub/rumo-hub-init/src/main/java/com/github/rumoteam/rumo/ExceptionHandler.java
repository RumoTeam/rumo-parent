package com.github.rumoteam.rumo;

public class ExceptionHandler implements Thread.UncaughtExceptionHandler {

	public static void main(String[] args) {
		System.out.println();
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());
		for (int i = 0; i < 1000; i++) {
			int ii = i;
			new Thread(() -> {
				throw new NullPointerException("testmEs:" + ii);
			}).start();
		}
	}

	@Override
	public void uncaughtException(Thread t, Throwable e) {
		long time = System.currentTimeMillis() / 1000;
		Class<? extends Throwable> exceptionClass = e.getClass();

		StringBuilder stringBuilder = new StringBuilder();
		StringBuilder pathBuilder = new StringBuilder();

		StackTraceElement[] stackTraceElements = e.getStackTrace();
		for (int i = stackTraceElements.length - 1; i >= 0; i--) {
			StackTraceElement stackTraceElement = stackTraceElements[i];

			String className = stackTraceElement.getClassName();
			String methodName = stackTraceElement.getMethodName();
			String fileName = stackTraceElement.getFileName();
			int lineNumber = stackTraceElement.getLineNumber();

			pathBuilder.append(className).append('.').append(methodName);
			pathBuilder.append('(');
			pathBuilder.append(fileName).append(':').append(lineNumber);
			pathBuilder.append(')');
			if (i != 0) {
				pathBuilder.append(" -> ");
			}
		}
		stringBuilder.append("time[").append(time).append("] thread[").append(t).append("] Exception:")
				.append(exceptionClass).append(" path:[");

		stringBuilder.append(pathBuilder).append(']');
	}

}
