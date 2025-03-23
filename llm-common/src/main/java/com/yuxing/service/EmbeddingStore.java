package com.yuxing.service;

import dev.langchain4j.model.embedding.EmbeddingModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmbeddingStore {
    @Autowired
    public EmbeddingModel embeddingModel;


}
