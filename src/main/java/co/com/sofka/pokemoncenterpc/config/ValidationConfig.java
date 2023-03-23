package co.com.sofka.pokemoncenterpc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.mapping.event.ValidatingMongoEventListener;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
@EnableMongoRepositories("co.com.sofka.pokemoncenterpc")
public class ValidationConfig {

    @Bean
    public ValidatingMongoEventListener validatingMongoEventListener(
            final LocalValidatorFactoryBean factory) {
        return new ValidatingMongoEventListener(factory);
    }
}
