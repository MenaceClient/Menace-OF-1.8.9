package dev.menace.utils.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import dev.menace.Menace;

public class FileManager {

	private static final Gson gson = new Gson();

	private static final File menaceFolder = new File("Menace");
	private static final File hudFolder = new File(menaceFolder, "hud");
	private static final File configFolder = new File(menaceFolder, "configs");
	private static final File scriptFolder = new File(menaceFolder, "scripts");

	//private static File betaFolder = new File(menaceFolder, "beta");

	public static void init() {

		if (!menaceFolder.exists()) {menaceFolder.mkdirs();}
		if (!hudFolder.exists()) {hudFolder.mkdirs();}
		if (!configFolder.exists()) {configFolder.mkdirs();}
		if (!scriptFolder.exists()) {scriptFolder.mkdirs();}
		//if (!betaFolder.exists()) {betaFolder.mkdirs();}

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
	
	public static File getConfigFolder() {
		return configFolder;
	}

	public static boolean writeObjectToFile(File file, Object o) {

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
	
	public static boolean writeJsonToFile(File file, JsonObject json) {

		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			
			FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(new GsonBuilder().serializeNulls().setPrettyPrinting().create().toJson((JsonElement)json));
            fileWriter.flush();
            fileWriter.close();
			
			
			return true;
		} 
		catch (IOException e) {
			e.printStackTrace();
			return false;
		}

	}
	
	public static JsonObject readJsonFromFile(File file) {
        if (file.exists()) {
            try {
                final FileReader fileReader = new FileReader(file);
                final JsonObject json = (JsonObject)new Gson().fromJson((Reader)fileReader, (Class)JsonObject.class);
				fileReader.close();
				return json;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
        }
		return null;
	}
	
	public static <T> T readFromJson(File file, Class<T> c) {
		
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
	/*
	public static File getBetaFolder() {
		return betaFolder;
	}*/
	
	public static File getScriptFolder() {
		return scriptFolder;
	}

}
