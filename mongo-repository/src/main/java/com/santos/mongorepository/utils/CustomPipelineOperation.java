package com.santos.mongorepository.utils;

import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public abstract class CustomPipelineOperation {

    public static List<AggregationOperation> pipeline(String ...pipeline){
        return Arrays.stream(pipeline).map(CustomAggregationOperation::new).collect(Collectors.toList());
    }

    public static List<AggregationOperation> pipeline(List<String> pipeline){
        return pipeline.stream().map(CustomAggregationOperation::new).collect(Collectors.toList());
    }

    public Aggregation aggregation(String ...pipeline){
        Assert.notNull(pipeline, "Pipeline not be null");
        var operations = pipeline(pipeline);
        return Aggregation.newAggregation(operations);
    }

    public Aggregation aggregation(List<String> pipeline){
        Assert.notNull(pipeline, "Pipeline not be null");
        var operations = pipeline(pipeline);
        return Aggregation.newAggregation(operations);
    }
}
