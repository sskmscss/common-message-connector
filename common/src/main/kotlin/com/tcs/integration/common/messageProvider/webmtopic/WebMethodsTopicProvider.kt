package com.tcs.integration.common.messageProvider.webmtopic

import com.tcs.integration.common.configuration.ConfigProperties
import com.tcs.integration.common.messageProvider.AbstractMessageProvider
import com.tcs.logging.logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jms.annotation.EnableJms
import org.springframework.jms.annotation.JmsListener
import org.springframework.jms.core.JmsTemplate
import javax.jms.BytesMessage
import javax.jms.TextMessage


@EnableJms
class WebMethodsTopicProvider(
        private val configProperties: ConfigProperties
) : AbstractMessageProvider()  {
    @Autowired
    var topicJmsTemplate: JmsTemplate? = null

    @JmsListener(containerFactory = "topicJmsListenerContainerFactory", destination = "\${webmethods.topic.name}")
    override fun receive(payload: Any) {
        if (payload is TextMessage) {
            val messageText = payload.text
            println("A new slug has arrived: $messageText")

        } else if (payload is BytesMessage) {
            var message = payload
            val charset = Charsets.UTF_8
            println(message.bodyLength.toInt())
            val data = ByteArray(message.bodyLength.toInt())
            message.readBytes(data)
            println(data.toString(charset))
            var messageText = String(data)
            println(messageText)
        }
    }

    override fun sendMessage(destination: String, payload: Any) {
        logger.info("Sending '{}' to topic - '{}'", payload, destination)
        topicJmsTemplate!!.convertAndSend(destination, payload)
    }

    override fun subscribeMessage(): String {
        return ""
    }
}
