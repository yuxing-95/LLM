package com.yuxing.controller;

import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.model.ollama.OllamaChatModel;

public class Test {
    public static void main(String[] args) {


        QwenChatModel params = QwenChatModel.builder()
                .apiKey("sk-313c4116dc574c0db443105edce01273")
                .modelName("qwen-max")
                .build();

        System.out.print(params.chat("你好"));
    }
}
