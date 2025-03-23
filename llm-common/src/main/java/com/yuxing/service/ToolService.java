package com.yuxing.service;

import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Service;

@Service
public class ToolService {

    @Tool("现在的天气")
    public String getWeather() {
        return "18℃";
    }
}
