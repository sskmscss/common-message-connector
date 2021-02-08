package com.tcs.service.edt.controller.v1

import com.tcs.service.edt.integration.Kafka
import com.tcs.service.edt.integration.UM
import com.tcs.service.edt.integration.WebmethodsQueue
import com.tcs.service.edt.integration.WebmethodsTopic
import org.springframework.web.bind.annotation.*

@RestController
class Controller(private val kafka: Kafka, private val um: UM,
                 private val webmTopic: WebmethodsTopic, private val webmQueue: WebmethodsQueue) {
    @RequestMapping(value = ["/api/postEvents/{type}/{destination}"], method = [RequestMethod.POST])
    fun getMessage(@PathVariable type: String, @PathVariable destination: String, @RequestBody payload: String) {
        when(type) {
            "kafka" ->  kafka.publishMessage(destination, payload)
            "um"    ->  um.publishMessage(destination, payload)
            "webmTopic"  ->  webmTopic.publishMessage(destination, payload)
            "webmQueue"  ->  webmQueue.publishMessage(destination, payload)
            else    ->  "No publisher found"
        }
    }
}
