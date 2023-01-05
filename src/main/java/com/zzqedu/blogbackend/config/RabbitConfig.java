package com.zzqedu.blogbackend.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public final static String DIRECT_NAME = "blog-update-article";

    public final static String ROUNTING_KEY = "updateKey";

    public final static String UPDATE_QUEUE = "updateArticleQueue";

    @Bean
    Queue queue() {
        return new Queue(UPDATE_QUEUE);
    }

    @Bean
    DirectExchange directExchange() {
        return new DirectExchange(DIRECT_NAME, true, false);
    }

    @Bean
    Binding binding() {
        return BindingBuilder.bind(queue())
                .to(directExchange()).with(ROUNTING_KEY);
    }

}
