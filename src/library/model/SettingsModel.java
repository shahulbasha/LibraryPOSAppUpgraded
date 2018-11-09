package library.model;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SettingsModel {
	
	int finePerDay;
	int noOfDays;
	String username;
	String password;
	
	public SettingsModel() {
		finePerDay=2;
		noOfDays=14;
		username="admin";
		password="admin";
		
	}

	public int getFinePerDay() {
		return finePerDay;
	}

	public void setFinePerDay(int finePerDay) {
		this.finePerDay = finePerDay;
	}

	public int getNoOfDays() {
		return noOfDays;
	}

	public void setNoOfDays(int noOfDays) {
		this.noOfDays = noOfDays;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public static void initConfig() {
		SettingsModel model=new SettingsModel();
		ObjectMapper mapper=new ObjectMapper();
		try {
			
			FileWriter writer= new FileWriter(new File("config.txt"));
			mapper.writeValue(writer, model);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static SettingsModel getModel() {
		SettingsModel settings=new SettingsModel();
		ObjectMapper mapper=new ObjectMapper();
		try {
			settings = mapper.readValue(new FileReader(new File("config.txt")), SettingsModel.class);
			
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			initConfig();
			e.printStackTrace();
		}
		
		return settings;
		
	}

}
