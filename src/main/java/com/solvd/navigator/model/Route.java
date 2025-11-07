package com.solvd.navigator.model;

import java.util.List;

public class Route {
    private Long id;
    private String name;
    private String description;
    private List<RouteLocation> locations;

    private Route(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.description = builder.description;
        this.locations = builder.locations;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<RouteLocation> getLocations() {
        return locations;
    }

    public static class Builder {
        private Long id;
        private String name;
        private String description;
        private List<RouteLocation> locations;

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder withLocations(List<RouteLocation> locations) {
            this.locations = locations;
            return this;
        }

        public Route build() {
            return new Route(this);
        }
    }
}
