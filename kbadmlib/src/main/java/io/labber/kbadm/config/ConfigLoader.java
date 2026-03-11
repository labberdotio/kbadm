package io.labber.kbadm.config;

import java.io.IOException;
import java.nio.charset.Charset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.ToNumberPolicy;

public class ConfigLoader {

	@Autowired
	private ResourceLoader resourceLoader;

	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	public Config loadConfig() throws IOException {
		Gson gson = new GsonBuilder()
			.setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE)
			.create();
		// return new Gson().fromJson(
		return gson.fromJson(
			resourceLoader.getResource(
				"classpath:" + "config.json"
			).getContentAsString(
				Charset.defaultCharset()
			),
			Config.class
		);
	}

	/**
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public Config loadConfigFromFile(String file) throws IOException {
		Gson gson = new GsonBuilder()
			.setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE)
			.create();
		// return new Gson().fromJson(
		return gson.fromJson(
			resourceLoader.getResource(
				"classpath:" + file
			).getContentAsString(
				Charset.defaultCharset()
			),
			Config.class
		);
	}

	/**
	 * 
	 * @param data
	 * @return
	 * @throws IOException
	 */
	public Config loadConfigFromData(String data) throws IOException {
		Gson gson = new GsonBuilder()
			.setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE)
			.create();
		// return new Gson().fromJson(
		return gson.fromJson(
			data,
			Config.class
		);
	}

	

}
