package com.unbank;

import java.io.File;
import java.io.FilenameFilter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/***
 * 
 * @ClassName: ImageCleaner
 * @Description: 定时清理本地服务器图片 ， 大于 2* 365 天的可以清理掉
 * @author: Administrator
 * @date: 2016-12-7 上午11:41:00
 */
public class ImageCleaner {
	private static Log logger = LogFactory.getLog(ImageCleaner.class);

	public void cleanImageByTime(File shareDir) {

		FilenameFilter filter = new FilenameFilter() {

			public boolean accept(File dir, String name) {
				if (dir.isFile()) {
					return false;
				}
				Date fileDate = stringToDate(name);
				Date date = getMyDate(new Date(), -365 * 2 - 35);
				if (fileDate.after(date)) {
					return false;
				}
				return true;
			}
		};
		File[] files = shareDir.listFiles(filter);
		ExecutorService executorService = Executors.newFixedThreadPool(5);
		for (File file : files) {
			executorService.execute(new DeleteFileThread(file));
		}
		executorService.shutdown();
	}

	class DeleteFileThread implements Runnable {

		private File file;

		public DeleteFileThread(File file) {
			super();
			this.file = file;
		}

		public void run() {
			deleteAllFile(file);
		}

	}

	/***
	 * 删除某个文件夹或文件 ，包括他的下级
	 * 
	 * @param file
	 */

	public void deleteAllFile(File file) {
		logger.info(file.getAbsolutePath() + " 正在被清理");
		if (file.isDirectory()) {
			deleteDirectory(file);
		} else {
			deleteFile(file);
		}
	}

	public void deleteDirectory(File file) {

		File[] files = file.listFiles();
		for (int i = 0; i < files.length; i++) {
			deleteAllFile(files[i]);
		}
		if (file.list().length == 0) {
			file.delete();
		}

	}

	public void deleteFile(File file) {
		if (file.exists()) {
			file.delete();
		}
	}

	/***
	 * 获取和当前时间相差的时间 格式 yyyyMMdd
	 * 
	 * @param date
	 * @param num
	 * @return
	 */
	public Date getMyDate(Date date, int num) {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(GregorianCalendar.DATE, num);
		date = calendar.getTime();
		date = stringToDate(dateToString(date, "yyyyMMdd"));
		return date;
	}

	/***
	 * 将 yyyyMMdd 格式的字符串转换为当前时间
	 * 
	 * @param time
	 * @return
	 */
	public Date stringToDate(String time) {
		SimpleDateFormat formatter = null;
		Date ctime = null;
		if (time.length() != 8 || time.contains(".")) {
			ctime = getMyDate(new Date(), -2 * 365 - 34);
		} else {
			try {
				String format = "yyyyMMdd";
				formatter = new SimpleDateFormat(format);
				ctime = formatter.parse(time);
			} catch (ParseException e) {
				// 如果转换失败 则转为 比较的时间，避免误删 不是时间格式的文件夹 或文件
				ctime = getMyDate(new Date(), -2 * 365 - 34);
			}
		}
		return ctime;
	}

	/***
	 * 将时间转为为字符串
	 * 
	 * @param data
	 * @param format
	 *            转换的格式
	 * @return
	 */
	public String dateToString(Date data, String format) {
		if (data == null) {
			data = new Date();
		}
		SimpleDateFormat formatter;
		formatter = new SimpleDateFormat(format);
		String ctime = formatter.format(data);
		return ctime;
	}

}
