package com.tcs.service.edt.integration

import com.tcs.integration.common.messageProvider.AbstractMessageProvider
import com.tcs.service.edt.message.Reactor
import com.tcs.service.edt.service.Service
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

@Component
class WebmethodsTopic(@Qualifier("webmTopic") private val webmTopic: AbstractMessageProvider, private val service: Service, private val reactor: Reactor) {

    init {
        webmTopic.messageListener = this.reactor
    }

    fun publishMessage(destination: String, payload: Any) {
        service.publishMessage(webmTopic, destination, payload)
    }

    fun subscribeMessage(type: String): String? {
        return service.subscribeMessage(webmTopic, type)
    }
}