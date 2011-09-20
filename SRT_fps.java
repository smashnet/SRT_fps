/*
* SRT_fps v0.1
* 
* Use this little java tool to change timecodes in srt subtitles according to a given
* framerate.
*
* Author: Nicolas Inden (nico@smashnet.de)
*/

import java.io.*;

public class SrtFpsConverter{
	
	public static void main(String args[]){
		if(args.length < 3){
			System.out.println("Usage: java SrtFpsConverter filename srcFPS destFPS");
			System.out.println("Type e.g. 23.976 and not 23,976");
			System.out.println("Exiting...");
			System.exit(0);
		}
		try{
			double srcFPS = Double.parseDouble(args[1]);
			double destFPS = Double.parseDouble(args[2]);
			File in = new File(args[0]);
			File out = new File(args[0]+".converted.txt");
			FileReader fr = new FileReader(in);
			FileWriter fw = new FileWriter(out);
			BufferedReader br = new BufferedReader(fr);
			BufferedWriter bw = new BufferedWriter(fw);
			String tmp = "";
			String[] sep;
			String newTimeFrom;
			String newTimeTo;
			while((tmp = br.readLine()) != null){
				if(hasTimeRange(tmp)){
					sep = tmp.split(" ");
					newTimeFrom = convertTime(sep[0],srcFPS,destFPS);
					newTimeTo = convertTime(sep[2],srcFPS,destFPS);
					bw.write(newTimeFrom + " --> " + newTimeTo + '\n');
					//bw.write("OK\n");
				}else{
					bw.write(tmp+'\n');
				}
			}
			bw.close();
		}catch(IOException ioe){
			System.out.println("File not found!");
		}
		
		
	}
	
	private static String convertTime(String line, double srcFPS, double destFPS){
		double time = strToSec(line);
		int hours;
		int minutes;
		double seconds;
		String h, min, sec = "";
		
		time = (time*25.0)/23.976/60;
		minutes = (int)time;
		seconds = (time - (double)minutes)*60.0;
		hours = minutes/60;
		minutes = minutes%60;
		h = String.valueOf(hours);
		if(hours < 10) h = "0"+hours;
		if(hours == 0) h = "00";
		min = String.valueOf(minutes);
		if(minutes < 10) min = "0"+minutes;
		if(minutes == 0) min = "00";
		sec = String.valueOf(seconds);
		if(seconds < 10.0) sec = "0"+seconds;
		if(seconds == 0.0) sec = "00.000";
		sec = sec.replace('.',',');
		if(sec.length() > 6)sec = sec.substring(0,6);
		
		
		return h + ":" + min + ":" + sec;
	}
	
	private static double strToSec(String timeStr){
		String[] tmp = timeStr.split(":");
		int hours = Integer.parseInt(tmp[0]);
		int minutes = Integer.parseInt(tmp[1]);
		double secs = Double.parseDouble(tmp[2].replace(',','.'));
		return (double)hours*3600.0 + (double)minutes*60.0 + secs;
	}
	
	private static boolean hasTimeRange(String line){
		String[] tmp = line.split(" ");
		if(tmp.length > 1){
			if(tmp[1].equals("-->")) return true;
		}
		return false;
	}
}
