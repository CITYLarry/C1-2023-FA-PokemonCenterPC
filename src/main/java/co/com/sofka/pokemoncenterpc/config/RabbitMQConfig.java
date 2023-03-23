package co.com.sofka.pokemoncenterpc.config;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String POKEMON_QUEUE = "pokemon.pc.queue";
    public static final String POKEMON_EXCHANGE = "pokemon.exchange";
    public static final String POKEMON_ROUTING_KEY = "pokemon.routing.key";

    public static final String GENERAL_QUEUE = "general.queue";
    public static final String ROUTING_KEY_GENERAL = "events.#";

    @Bean
    public RabbitAdmin rabbitAdmin(RabbitTemplate rabbitTemplate) {
        var admin =  new RabbitAdmin(rabbitTemplate);
        admin.declareExchange(new TopicExchange(POKEMON_EXCHANGE));
        return admin;
    }

    @Bean
    public Queue queue() {
        return new Queue(POKEMON_QUEUE);
    }

    @Bean
    public Queue generalQueue(){
        return new Queue(GENERAL_QUEUE);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(POKEMON_EXCHANGE);
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue())
                .to(exchange())
                .with(POKEMON_ROUTING_KEY);
    }

    @Bean
    public Binding generalBinding(){
        return BindingBuilder.bind(generalQueue())
                .to(exchange())
                .with(ROUTING_KEY_GENERAL);
    }

    @Bean
    public MessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate template(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }
}
