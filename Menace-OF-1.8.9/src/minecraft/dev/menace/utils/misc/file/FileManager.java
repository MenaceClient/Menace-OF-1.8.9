package dev.menace.utils.misc.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import com.google.gson.Gson;

import dev.menace.Menace;

public class FileManager {

	private static Gson gson = new Gson();

	private static File menaceFolder = new File("Menace");
	private static File hudFolder = new File(menaceFolder, "hud");
	private static File moduleFolder = new File(menaceFolder, "module");
	private static File betaFolder = new File(menaceFolder, "beta");

	public static void init() {

		if (!menaceFolder.exists()) {menaceFolder.mkdirs(); Menace.instance.isFirstLaunch = true;}
		if (!hudFolder.exists()) {hudFolder.mkdirs();}
		if (!moduleFolder.exists()) {moduleFolder.mkdirs();}
		if (!betaFolder.exists()) {betaFolder.mkdirs();}

	}

	public static Gson getGson() {
		return gson;
	}

	public static File getMenaceFolder() {
		return menaceFolder;
	}

	public static File getHudFolder() {
		return hudFolder;
	}
	
	public static File getModuleFolder() {
		return moduleFolder;
	}

	public static boolean writeJsonToFile(File file, Object o) {

		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			
			FileOutputStream outputStream = new FileOutputStream(file);
			outputStream.write(gson.toJson(o).getBytes());
			outputStream.flush();
			outputStream.close();
			
			
			return true;
		} 
		catch (IOException e) {
			e.printStackTrace();
			return false;
		}

	}
	
	public static <T extends Object> T readFromJson(File file, Class<T> c) {
		
		try {
			
			FileInputStream inputStream = new FileInputStream(file);
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			
			StringBuilder stringBuilder = new StringBuilder();
			String line;
			
			while ((line = bufferedReader.readLine()) != null) {
				stringBuilder.append(line);
			}
			
			bufferedReader.close();
			inputStreamReader.close();
			inputStream.close();
			
			return gson.fromJson(stringBuilder.toString(), c);
			
		}
		catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
	}

	public static File getBetaFolder() {
		return betaFolder;
	}

}
