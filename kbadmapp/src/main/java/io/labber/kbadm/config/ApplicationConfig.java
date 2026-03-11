
//
// Copyright (c) 2019, 2023, 2024, 2025, John Grundback
// All rights reserved.
//

package io.labber.kbadm.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EntityScan(basePackages = { "io.labber", "io.labber.app", "io.labber.kbadm" })
@ComponentScan(basePackages = { "io.labber", "io.labber.app", "io.labber.kbadm" })
public class ApplicationConfig {

	@Bean
	ConfigLoader configLoader() {
		return new ConfigLoader();
	}

}
