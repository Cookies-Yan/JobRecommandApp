package com.recommand.job.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ExtractRequestBody {

    @JsonProperty("data")  //对应POST过来json的key的名字
    public List<String> data;

    @JsonProperty("max_keywords")
    public int maxKeyWords;

    public ExtractRequestBody(List<String> data, int maxKeyWords) {
        this.data = data;
        this.maxKeyWords = maxKeyWords;
    }
}
