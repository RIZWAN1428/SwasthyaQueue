package com.swasthyaqueue.backend.dto;

import java.util.List;

public record OcrResult(String filename, List<Page> pages) {

    public record Page(int page, String method, double confidence, String text) {
    }
} 