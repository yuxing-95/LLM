package com.yuxing.configuration;

import com.alibaba.dashscope.assistants.Assistant;
import com.yuxing.LearningApplication;
import com.yuxing.service.ToolService;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentParser;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentByLineSplitter;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.ollama.OllamaEmbeddingModel;
import dev.langchain4j.model.ollama.OllamaStreamingChatModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Configuration
public class AiConfig {
    public interface AiAssistant {
        String chat(String message);
        TokenStream stream(String message);
    }
    @Bean
    public OllamaChatModel getModel() {
        return OllamaChatModel.builder()
                .baseUrl("http://localhost:11434/")
                .modelName("deepseek-r1:1.5b")
                .build();
    }
    @Bean
    public OllamaStreamingChatModel getStreamingModel() {
        return OllamaStreamingChatModel.builder()
                .baseUrl("http://localhost:11434/")
                .modelName("deepseek-r1:1.5b")
                .build();
    }
    @Bean
    public OllamaEmbeddingModel getEmbedding() {
        return OllamaEmbeddingModel.builder()
                .baseUrl("http://localhost:11434/")
                .modelName("bge-m3:latest")
                .build();
    }
    @Bean
    public InMemoryEmbeddingStore getEmbeddingStore() {
        return new InMemoryEmbeddingStore<>();
    }
    @Bean
    CommandLineRunner ingestTermOfServiceToVectorStore(OllamaEmbeddingModel ollamaEmbeddingModel, EmbeddingStore embeddingStore) throws URISyntaxException {
        // 读取
        Path documentPath = Paths.get(LearningApplication.class.getClassLoader().getResource("rag.txt").toURI());
        return args -> { DocumentParser documentParser = new TextDocumentParser();
            Document document = FileSystemDocumentLoader.loadDocument(documentPath, documentParser);
            DocumentByLineSplitter splitter = new DocumentByLineSplitter( 500, 200 );
            List<TextSegment> segments = splitter.split(document); // 向量化
            List<Embedding> embeddings = ollamaEmbeddingModel.embedAll(segments).content();
            // 存入
            embeddingStore.addAll(embeddings,segments);
        };
    }
    @Bean
    public AiAssistant getAssistant(ChatLanguageModel ollamaChatModel, StreamingChatLanguageModel ollamaStreamingModel, EmbeddingStore embeddingStore, EmbeddingModel ollamaEmbeddingModel) {
        MessageWindowChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

        ContentRetriever contentRetriever = EmbeddingStoreContentRetriever.builder()
                .embeddingStore(embeddingStore)
                .embeddingModel(ollamaEmbeddingModel)
                .maxResults(5) // 最相似的5个结果
                .minScore(0.6) // 只找相似度在0.6以上的内容
                .build();
        return AiServices.builder(AiAssistant.class)
                .chatLanguageModel(ollamaChatModel)
                .streamingChatLanguageModel(ollamaStreamingModel)
                .contentRetriever(contentRetriever)
                .chatMemory(memory)
                .build();
    }
}
