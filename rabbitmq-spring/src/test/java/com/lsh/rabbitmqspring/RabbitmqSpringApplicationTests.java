package com.lsh.rabbitmqspring;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class RabbitmqSpringApplicationTests {

	@Test
	void contextLoads() {
		log.debug("debug");
		log.warn("warning");
		log.error("error");
		log.info("info");
		log.trace("trace");
	}

}
