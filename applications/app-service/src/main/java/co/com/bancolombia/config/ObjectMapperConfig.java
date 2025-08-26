package co.com.bancolombia.config;

import co.com.bancolombia.model.LoanApplication;
import co.com.bancolombia.r2dbc.entity.LoanApplicationEntity;
import org.reactivecommons.utils.ObjectMapper;
import org.reactivecommons.utils.ObjectMapperImp;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObjectMapperConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapperImp();
    }

}
