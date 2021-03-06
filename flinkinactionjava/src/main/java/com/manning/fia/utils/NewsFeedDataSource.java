package com.manning.fia.utils;

import org.apache.flink.api.java.utils.ParameterTool;
import com.manning.fia.utils.custom.CustomDataSource;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer09;
import org.apache.flink.streaming.util.serialization.SimpleStringSchema;

/**
 * Created by hari on 7/17/16.
 */
public class NewsFeedDataSource {

    public static SourceFunction<String> getKafkaDataSource(ParameterTool parameterTool) {
        return new FlinkKafkaConsumer09<String>(
                parameterTool.getRequired("topic"),
                new SimpleStringSchema(),
                parameterTool.getProperties());
    }

    public static SourceFunction<String> getCustomDataSource(ParameterTool parameterTool) {
        return new CustomDataSource(parameterTool);
    }


}