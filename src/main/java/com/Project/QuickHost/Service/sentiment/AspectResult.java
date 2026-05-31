package com.Project.QuickHost.Service.sentiment;

import java.util.LinkedHashMap;
import java.util.Map;


public record AspectResult(
        double cleanliness, double service, double location,
        double value, double room) {
    // Must return a MUTABLE Map — this is stored into Review.aspectScores
    // (@ElementCollection). Hibernate's merge calls .clear() on the existing
    // map, and Map.of(...) is immutable → throws UnsupportedOperationException.
    public Map<String, Double> asMap() {
        Map<String, Double> m = new LinkedHashMap<>();
        m.put("cleanliness", cleanliness);
        m.put("service", service);
        m.put("location", location);
        m.put("value", value);
        m.put("room", room);
        return m;
    }

}