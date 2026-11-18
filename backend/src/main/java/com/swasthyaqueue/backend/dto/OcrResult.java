package com.swasthyaqueue.backend.dto;

public record OcrResult() {

    public record Page(int page, String method, double confidence, String text) {
    }
} 