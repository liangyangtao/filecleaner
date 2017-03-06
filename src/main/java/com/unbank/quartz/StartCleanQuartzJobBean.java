package com.unbank.quartz;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.unbank.ImageCleaner;

public class StartCleanQuartzJobBean {
	private static Log logger = LogFactory
			.getLog(StartCleanQuartzJobBean.class);
	private String filename;

	public StartCleanQuartzJobBean(String filename) {
		super();
		this.filename = filename;
	}

	public void executeInternal() {
		File shareDir = new File(filename);
		if (!shareDir.exists()) {
			logger.info(filename + " 不存在 请检查 路径");
			System.exit(0);
		}
		new ImageCleaner().cleanImageByTime(shareDir);

	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

}
