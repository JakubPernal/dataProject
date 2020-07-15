package com.pernal;

import com.pernal.configuration.DataClientConfig;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;

@TestConfiguration
@Import(DataClientConfig.class)
public class DataTestConfig {
}
