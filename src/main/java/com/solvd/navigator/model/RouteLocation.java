package com.solvd.navigator.model;

public class RouteLocation {
    private Route route;
    private Location location;
    private Long position;

    private RouteLocation(Builder builder) {
        this.route = builder.route;
        this.location = builder.location;
        this.position = builder.position;
    }

    public Route getRoute() {
        return route;
    }

    public Location getLocation() {
        return location;
    }

    public Long getPosition() {
        return position;
    }

    public static class Builder {
        private Route route;
        private Location location;
        private Long position;

        public Builder withRoute(Route route) {
            this.route = route;
            return this;
        }

        public Builder withLocation(Location location) {
            this.location = location;
            return this;
        }

        public Builder withPosition(Long position) {
            this.position = position;
            return this;
        }

        public RouteLocation build() {
            return new RouteLocation(this);
        }
    }
}
