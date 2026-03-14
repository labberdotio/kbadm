package io.labber.kbadm.config;

import java.io.IOException;
import java.nio.charset.Charset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;

import com.google.gson.Gson;

public class ConfigLoader {

	@Autowired
	private ResourceLoader resourceLoader;

	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	public Config loadConfig() throws IOException {
		return new Gson().fromJson(
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
		return new Gson().fromJson(
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
		return new Gson().fromJson(
			data,
			Config.class
		);
	}

}
