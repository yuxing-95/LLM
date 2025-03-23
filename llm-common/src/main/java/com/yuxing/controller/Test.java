package com.yuxing.controller;

import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.community.model.dashscope.QwenEmbeddingModel;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.ollama.OllamaEmbeddingModel;
import dev.langchain4j.model.output.Response;

public class Test {
    public static void main(String[] args) {


//        QwenChatModel params = QwenChatModel.builder()
//                .apiKey("sk-313c4116dc574c0db443105edce01273")
//                .modelName("qwen-max")
//                .build();
        OllamaChatModel model = OllamaChatModel.builder()
                .baseUrl("http://localhost:11434/")
                .modelName("deepseek-r1:1.5b")
                .build();
        OllamaEmbeddingModel build = OllamaEmbeddingModel.builder()
                .baseUrl("http://localhost:11434/")
                .modelName("bge-m3:latest")
                .build();
        Response<Embedding> embed = build.embed("你好，我叫和余弦");
        System.out.println(embed.content().toString());
        System.out.print(embed.content().vector().length);
    }
}
