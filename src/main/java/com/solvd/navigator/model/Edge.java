package com.solvd.navigator.model;

public class Edge {
    private Long id;
    private Location from;
    private Location to;
    private Double weight;
    private Boolean directed;
    private String name;
    private Boolean active;

    private Edge(Builder builder) {
        this.id = builder.id;
        this.from = builder.from;
        this.to = builder.to;
        this.weight = builder.weight;
        this.directed = builder.directed;
        this.name = builder.name;
        this.active = builder.active;
    }

    public Long getId() {
        return id;
    }

    public Location getFrom() {
        return from;
    }

    public Location getTo() {
        return to;
    }

    public Double getWeight() {
        return weight;
    }

    public Boolean isDirected() {
        return directed;
    }

    public String getName() {
        return name;
    }

    public Boolean isActive() {
        return active;
    }

    public static class Builder {
        private Long id;
        private Location from;
        private Location to;
        private Double weight;
        private Boolean directed;
        private String name;
        private Boolean active;

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withFrom(Location from) {
            this.from = from;
            return this;
        }

        public Builder withTo(Location to) {
            this.to = to;
            return this;
        }

        public Builder withWeight(Double weight) {
            this.weight = weight;
            return this;
        }

        public Builder withDirected(Boolean directed) {
            this.directed = directed;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withActive(Boolean active) {
            this.active = active;
            return this;
        }

        public Edge build() {
            return new Edge(this);
        }
    }
}
