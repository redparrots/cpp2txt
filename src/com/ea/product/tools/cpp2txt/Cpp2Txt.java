package com.ea.product.tools.cpp2txt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;

public class Cpp2Txt {

	/**
	 * 把目标目录底下的所有文件内容写到一个文件中
	 */
	
	private final static String USAGE = "Usage: java -jar cpp2txt.jar [dir] [file filter] [output file]." + 
										"For example : java -jar cpp2txt.jar ./demo *.h,*.cpp ./output.txt";
	
	public static void main(String[] args) {
		
		if(args.length < 3)  {
			usage();
			return;
		}
		
		String dir = args[0];
		String filters = args[1];
		String outputFile = args[2];
		
		Cpp2Txt cpp2Txt = new Cpp2Txt();
		cpp2Txt.cpp2txt(dir, filters, outputFile);
		
		System.out.println("success.");
	}

	public static void usage() {
		System.out.println(USAGE);
	}
	
	public void cpp2txt(String dir,String filters,String outputFile) {
		
		//打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
        FileWriter writer;
		try {
			writer = new FileWriter(outputFile,false);
			
			FileFilter filter = new FileFilter(filters);
			
			listFile(dir,filter,writer);
			
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
	}
	
	private void listFile(String path,FilenameFilter filter,FileWriter writer) {
		File dir = new File(path); 
		File[] files = dir.listFiles();
		File[] filterFiles = dir.listFiles(filter);
		
		if(filterFiles != null) {
			for(int i = 0; i < filterFiles.length; i++) {			
				String fileName = filterFiles[i].getAbsolutePath().toLowerCase();
				System.out.println(filterFiles[i].getAbsolutePath());
				String content = getFileContent(fileName);
				writeContent(writer, content);
			}
		}
		
		if(files != null) {
			for(int i = 0; i < files.length; i++) {			
				if(files[i].isDirectory()) {
					
					System.out.println(files[i]);
					listFile(files[i].getAbsolutePath(),filter,writer);
					
				}
			}
		}
	}
	
	private String getFileContent(String fileName) {
		File file = new File(fileName);
        BufferedReader reader = null;
        String content = "";
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
            		content += tempString + "\n";
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        
        return content;
	}
	
	private void writeContent(FileWriter writer,String content) {
		try {
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
}
