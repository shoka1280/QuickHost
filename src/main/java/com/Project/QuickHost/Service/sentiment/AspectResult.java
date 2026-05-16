package com.Project.QuickHost.Service.sentiment;

import java.util.Map;


public record AspectResult(
        double cleanliness, double service, double location,
        double value, double room) {
    public Map<String, Double> asMap() {
        return Map.of("cleanliness", cleanliness, "service", service,
                "location", location, "value", value, "room", room);
    }

}