package com.solvd.navigator.model;

public class Location {
    private Long id;
    private String name;
    private Double x;
    private Double y;
    private String type;
    private String description;

    private Location(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.x = builder.x;
        this.y = builder.y;
        this.type = builder.type;
        this.description = builder.description;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getX() {
        return x;
    }

    public Double getY() {
        return y;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public static class Builder {
        private Long id;
        private String name;
        private Double x;
        private Double y;
        private String type;
        private String description;

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withX(Double x) {
            this.x = x;
            return this;
        }

        public Builder withY(Double y) {
            this.y = y;
            return this;
        }

        public Builder withType(String type) {
            this.type = type;
            return this;
        }

        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public Location build() {
            return new Location(this);
        }
    }
}
