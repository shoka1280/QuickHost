package com.Project.QuickHost.Service.sentiment;

import com.Project.QuickHost.Entity.enums.Sentiment;

public record OverallResult(Sentiment sentiment, double score, String snippet) {}
