CREATE DATABASE navigator_db;
USE navigator_db;

CREATE TABLE location (
  id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) UNIQUE NOT NULL,
  x DOUBLE NOT NULL,
  y DOUBLE NOT NULL,
  type VARCHAR(50),
  description TEXT
);

CREATE TABLE edge (
  id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  from_location BIGINT UNSIGNED,
  to_location BIGINT UNSIGNED,
  weight DOUBLE NOT NULL,
  directed BOOLEAN DEFAULT FALSE,
  name VARCHAR(100),
  active BOOLEAN DEFAULT TRUE,
  CONSTRAINT fk_edge_from FOREIGN KEY (from_location) REFERENCES location(id),
  CONSTRAINT fk_edge_to FOREIGN KEY (to_location) REFERENCES location(id)
);

CREATE TABLE route (
  id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) UNIQUE NOT NULL,
  description TEXT
);

CREATE TABLE route_location (
  route_id BIGINT UNSIGNED,
  location_id BIGINT UNSIGNED,
  position INT,
  PRIMARY KEY (route_id, position),
  CONSTRAINT fk_route_loc_route FOREIGN KEY (route_id) REFERENCES route(id) ON DELETE CASCADE,
  CONSTRAINT fk_route_loc_location FOREIGN KEY (location_id) REFERENCES location(id)
);
