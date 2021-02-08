package com.tcs.integration.common.configuration

import com.tcs.logging.logger
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jms.annotation.EnableJms
import org.springframework.jms.config.DefaultJmsListenerContainerFactory
import org.springframework.jms.core.JmsTemplate
import org.springframework.jndi.JndiObjectFactoryBean
import java.util.Properties
import javax.jms.ConnectionFactory
import javax.naming.Context

@EnableJms
@Configuration
class WebMethodsJmsQueueAutoConfiguration(
        private val webMethodsQueueJndiProperties: WebMethodsJndiProperties
) {

    @Bean
    @ConditionalOnMissingBean(name = ["queueJmsTemplate"])
    fun queueJmsTemplate() = JmsTemplate().apply {
        connectionFactory = queueConnectionFactory()
    }

    @Bean
    @ConditionalOnMissingBean(name = ["queueJmsListenerContainerFactory"])
    fun queueJmsListenerContainerFactory() = DefaultJmsListenerContainerFactory().apply {
        setConnectionFactory(queueConnectionFactory())
    }

    @Bean
    fun queueConnectionFactory(): ConnectionFactory {
        val connectionFactory = webMethodsQueueJndiProperties.connectionFactory
        val initialContextFactory = webMethodsQueueJndiProperties.initialContextFactory
        logger.info("Building connection factory {} with initial context factory {}", connectionFactory, initialContextFactory)

        return with(JndiObjectFactoryBean()) {
            jndiName = connectionFactory
            jndiEnvironment = queueJndiProperties()
            afterPropertiesSet()
            `object` as ConnectionFactory
        }
    }

    @Bean
    fun queueJndiProperties() = Properties().apply {
        put(Context.INITIAL_CONTEXT_FACTORY, webMethodsQueueJndiProperties.initialContextFactory)
        put(Context.PROVIDER_URL, webMethodsQueueJndiProperties.providerUrl)
    }
}
