package com.Project.QuickHost.Service.sentiment.ai;

import java.util.List;

public record SummaryResult(String narrative,
                            List<String> pros,
                            List<String> cons) {}

