package com.example.demo.utils;

import org.springframework.data.domain.ExampleMatcher;

public class ExampleMatcherUtils {

    public static ExampleMatcher generateStringContaningAndNullIgnoreMatcher(){
       return ExampleMatcher
                .matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreNullValues();
    }
}
