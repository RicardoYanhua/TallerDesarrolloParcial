package com.yanhua.ms.producto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class ProductoApplication {

    private static final Logger logger = LoggerFactory.getLogger(ProductoApplication.class);

	public static void main(String[] args) {
 		SpringApplication.run(ProductoApplication.class, args);
        logger.info("ProductoApplication arrancada");
	}

}
