package com.yuxing.controller;

import com.yuxing.configuration.AiConfig;
import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.service.TokenStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/ai")
public class returnController {
    @Autowired
    AiConfig.AiAssistant assistant;

    @RequestMapping("/chat")
    public String test01(String message) {
        return assistant.chat("你好,我的名字是吕布");
    }
    @RequestMapping(value = "/memory_stream_chat",produces ="text/stream;charset=UTF-8")
    public Flux<String> memoryStreamChat(@RequestParam(defaultValue="天气怎么样") String message) {
        TokenStream stream = assistant.stream(message);
        return Flux.create(sink ->
                stream.onPartialResponse(sink::next)
                        .onCompleteResponse(c -> sink.complete())
                        .onError(sink::error) .start());
    }
}
