package com.increff.pos;

import com.increff.pos.spring.SpringConfig;
import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@ComponentScan(//
        value = "com.increff.pos.*", excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = {SpringConfig.class})//
)
@PropertySources({ //
        @PropertySource(value = "classpath:test.properties") }//
)

public class QaConfig {


}
