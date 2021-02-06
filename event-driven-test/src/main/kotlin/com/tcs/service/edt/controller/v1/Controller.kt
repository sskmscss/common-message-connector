package com.tcs.service.edt.controller.v1

import com.tcs.service.edt.integration.Kafka
import com.tcs.service.edt.integration.UM
import com.tcs.service.edt.integration.WebmethodsTopic
import org.springframework.web.bind.annotation.*

@RestController
class Controller(private val kafka: Kafka, private val um: UM, private val webmTopic: WebmethodsTopic) {
    @RequestMapping(value = ["/api/postEvents/{type}"], method = [RequestMethod.POST])
    fun getMessage(@PathVariable type: String, @RequestBody payload: String) {
        when(type) {
            "kafka" ->  kafka.publishMessage(payload)
            "um"    ->  um.publishMessage(payload)
            "webmTopic"  ->  webmTopic.publishMessage(payload)
            else    ->  "No publisher found"
        }
    }

    // Not required. In case of issues we can use this endpoint
    @RequestMapping(value = ["/api/getEvents/{type}"], method = [RequestMethod.GET])
    @ResponseBody
    fun getMessage(@PathVariable type: String): String? {
        return when(type) {
            "kafka" ->  kafka.subscribeMessage(type)
            "um"    ->  um.subscribeMessage(type)
            else    ->  "No subscriber found"
        }
    }
}
