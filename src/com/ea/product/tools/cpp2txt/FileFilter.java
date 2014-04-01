package com.ea.product.tools.cpp2txt;

import java.io.File;
import java.io.FilenameFilter;

/**
* An implementation of the FileFilter interfaces that filters files based
* on a DOS file name pattern. DOS file name patterns are case-insensitive
* and make use of the ? and * wildcard characters.
*/

public class FileFilter implements FilenameFilter {

	private String[] patterns;
	
	public FileFilter(String s) {
		s = s.trim();
		s = toMinimalPattern(s);
		s = s.toUpperCase();
		patterns = s.split(",");
				
		for(int i = 0; i < patterns.length; i++) {
			patterns[i] = patterns[i].trim();
			
			System.out.println(patterns[i]);
		}
	}
	
	@Override
	public boolean accept(File dir, String extension) {
		for(int i = 0; i < patterns.length; i++) {
			if (match(patterns[i],extension)) {
				return true;
			}
		}
		
		return false;
	}

	/**
	* Converts the provided pattern to it's minimum equivalent. Basically
	* this method converts all touching *'s to a single *.
	*/

	public static String toMinimalPattern(String s) {
		int len = s.length();
		StringBuffer sb = new StringBuffer();
		int x = 0;
		int y = s.indexOf('*');
		while (x <= y) {
			y++;
			sb.append(s.substring(x, y));
			x = y;
			while (y < len && s.charAt(y) == '*') {
				y++;
				x = y;
			}
			y = s.indexOf('*', x);
		}
		sb.append(s.substring(x));
		return sb.toString();
	}
	
	/**
	 * Checks to see if a provided file name matches the provided DOS pattern.
	 */

	public static boolean match(String pattern, String fileName) {
		pattern = pattern.trim();
		pattern = toMinimalPattern(pattern);
		pattern = pattern.toUpperCase();
		char[] ca1 = pattern.toCharArray();
		fileName = fileName.trim();
		fileName = fileName.toUpperCase();
		char[] ca2 = fileName.toCharArray();
		return match(ca1, 0, ca2, 0);
	}

	private static boolean match(char[] pattern, int px, char[] name, int nx) {
		while (px < pattern.length) {
			char pc = pattern[px];
			if (pc == '*') {
				if (px >= (pattern.length - 1))
					return true;
				int x = nx;
				while (x < name.length) {
					if (match(pattern, px + 1, name, x))
						return true;
					x++;
				}
				px++;
				continue;
			}
			if (nx >= name.length)
				return false;
			if (pc != '?') {
				if (pc != name[nx])
					return false;
			}
			px++;
			nx++;
		}
		return (nx >= name.length);
	}
	
	public static boolean isMatch(String mask, String nick) {
		mask = mask.trim();
		if (mask.equalsIgnoreCase("*")) {
			return true;
		}
		if (mask.startsWith("*")) {
			int end = -1;
			if (mask.indexOf("*", mask.indexOf("*") + 1) == -1) {
				end = mask.length();
			} else {
				end = mask.indexOf("*", mask.indexOf("*") + 1);
			}
			if (end == -1) {
				return false;
			}
			String tmask = mask.substring(1, end).trim();
			if (nick.indexOf(tmask) == -1) {
				return false;
			} else {
				mask = mask.substring(end).trim();
				nick = nick.substring(nick.indexOf(tmask)).trim();
				return isMatch(mask, nick);
			}
		}
		int end = -1;
		if (mask.indexOf("*") == -1) {
			end = mask.length();
		} else {
			end = mask.indexOf("*");
		}
		if (end == -1) {
			return false;
		}
		String tmask = mask.substring(0, end).trim();
		if (nick.startsWith(tmask)) {
			if (end == mask.length()) {
				return true;
			}
			mask = mask.substring(end).trim();
			nick = nick.substring(tmask.length()).trim();
			return isMatch(mask, nick);
		} else {
			return false;
		}
	}
}
