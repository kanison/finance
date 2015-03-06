/**
 * 
 */
package com.tenpay.sm.test.photoc;

import java.io.File;
import java.io.IOException;

/**
 * @author li.hongtl
 *
 */
public class PC {
	File destFolder;
	File sourceFolder;
	
	long minFileSize = 14*1024;
	
	int copyCount = 0;
	int skipSimilarCount = 0;
	int skipErrorCount = 0;
	int skipSmallCount = 0;
	
	private boolean init(String[] args) {
		if(args==null || args.length<2) {
			System.out.println("非法的参数，PC dest source");
		}
		destFolder = new File(args[0]);
		sourceFolder = new File(args[1]);
		if(destFolder.exists() && !destFolder.isDirectory()) {
			System.out.println("非法的参数，dest不是一个文件夹:" + args[0]);
			return false;
		}
		if(!sourceFolder.exists() || !sourceFolder.isDirectory()) {
			System.out.println("非法的参数，source不是一个文件夹" + args[1]);
			return false;
		}
		if(!destFolder.exists()) {
			destFolder.mkdir();
		}
		return true;
	}
	public void execute(String[] args) {
		if(this.init(args)) {
			this.move(sourceFolder);
		}
	}
	private void move(File sourceSubFolder) {
		for(File file : sourceSubFolder.listFiles()) {
			try {
				if(file.isDirectory()) {
					move(file);
					continue;
				}
				String fileNameLower = file.getName().toLowerCase(); 
				if(fileNameLower.endsWith(".jpg") ||
						fileNameLower.endsWith(".jpeg") ||
						fileNameLower.endsWith(".bmp") ||
						fileNameLower.endsWith(".gif") ||
						fileNameLower.endsWith(")") ||
						fileNameLower.endsWith(".png")) {
					if(file.length() < minFileSize) {
						System.out.println("忽略小文件：(" + (++skipSmallCount) + ") " + file.getAbsolutePath());
						continue;
					}
					
					copyToDestFolder(file);
				}
			} catch(Exception ex) {
				System.out.println("处理出错出错，" + ex.getMessage() + " 忽略 (" + (++skipErrorCount) + ") " + file.getAbsolutePath());
			}
		}
	}
	
	private void copyToDestFolder(File file) throws IOException {
		String fileName = file.getName().endsWith(")") ? file.getName() + ".jpg" : file.getName();
		File dest = new File(destFolder.getAbsolutePath() + File.separatorChar + fileName);
		if(dest.exists()) {
			File similar = similarFile(file);
			if(similar!=null) {
				System.out.println("        忽略类似：(" + (++skipSimilarCount) + ") " +
						file.getAbsolutePath() + "  == " + similar.getAbsolutePath());
				return;			
			}
//			String tryLongName = file.getAbsolutePath().replace('/', '.').replace('\\', '.').replace(':', '.');
//			tryLongName = tryLongName.endsWith(")") ? tryLongName + ".jpg" : tryLongName;
//			dest = new File(destFolder.getAbsolutePath() + File.separatorChar + tryLongName);
//			if(!dest.isDirectory() && file.length()==dest.length()) {
//				System.out.println("        忽略类似：(" + (++skipSimilarCount) + ") " +
//						file.getAbsolutePath() + "  == " + dest.getAbsolutePath());
//				return;
//			}
			
			String suffix = "_" + new java.text.SimpleDateFormat("yyyyMMdd").format(new java.util.Date()) + 
				"_" + System.currentTimeMillis();
			int indexDot = fileName.lastIndexOf('.');
			String newName = fileName.substring(0,indexDot) + suffix + fileName.substring(indexDot);
			dest = new File(destFolder.getAbsolutePath() + File.separatorChar + newName);
		}
		
		java.io.FileInputStream is = new java.io.FileInputStream(file);
		java.io.FileOutputStream os = new java.io.FileOutputStream(dest);
		byte[] buffer = new byte[16*1024];
		int length = is.read(buffer);
		while(length>0) {
			os.write(buffer, 0, length);
			length = is.read(buffer);
		}
		os.flush();
		os.close();
		is.close();
		System.out.println("复制成功：(" + (++copyCount) + ") " + 
				file.getAbsolutePath() + "  --> " + dest.getAbsolutePath());
				
	}
	private File similarFile(File file) {
		for(File thefile : this.destFolder.listFiles()) {
			String fileName = file.getName().lastIndexOf('.')<0 ? file.getName() : 
				file.getName().substring(0,file.getName().lastIndexOf('.'));
			if(thefile.length()==file.length() && 
					thefile.getName().startsWith(fileName)) {
				return thefile;
			}
		}
		return null;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PC pc = new PC();
		pc.execute(args);
	}

}
