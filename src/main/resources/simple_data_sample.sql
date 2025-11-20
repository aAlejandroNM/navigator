USE navigator_db;

-- === INSERT LOCATIONS ===
INSERT INTO location (name, x, y, type, description) VALUES
('Central Park', 40.785091, -73.968285, 'Park', 'A large public park in the center of the city.'),
('Times Square', 40.758896, -73.985130, 'Landmark', 'Major commercial intersection and tourist destination.'),
('Empire State Building', 40.748817, -73.985428, 'Building', '102-story skyscraper located in Midtown Manhattan.'),
('Grand Central Station', 40.752726, -73.977229, 'Station', 'Historic train terminal and transportation hub.'),
('Brooklyn Bridge', 40.706086, -73.996864, 'Bridge', 'Iconic suspension bridge connecting Manhattan and Brooklyn.'),
('Statue of Liberty', 40.689247, -74.044502, 'Monument', 'Famous landmark symbolizing freedom.'),
('Wall Street', 40.706567, -74.009000, 'District', 'Financial district of New York City.'),
('Battery Park', 40.703277, -74.017028, 'Park', 'Public park at the southern tip of Manhattan.'),
('Chinatown', 40.715751, -73.997031, 'District', 'Neighborhood known for Chinese culture and cuisine.'),
('Central Library', 40.672501, -73.968056, 'Building', 'Main branch of the city public library system.'),
('High Line Park', 40.7480, -74.0048, 'Park', 'Elevated linear park built on a historic freight line.'),
('Madison Square Garden', 40.7505, -73.9934, 'Arena', 'Famous multi-purpose indoor arena in Manhattan.'),
('Columbus Circle', 40.7681, -73.9819, 'Landmark', 'Major landmark and traffic circle at the southwest corner of Central Park.'),
('Union Square', 40.7359, -73.9911, 'Square', 'Public plaza known for shops, park, and subway hub.'),
('Roosevelt Island', 40.7612, -73.9497, 'Island', 'Narrow island in the East River.'),
('Hudson Yards', 40.7540, -74.0027, 'District', 'Modern commercial and residential development.'),
('Chelsea Market', 40.7424, -74.0060, 'Market', 'Indoor food hall and shopping mall.'),
('Broadway Junction', 40.6799, -73.9050, 'Station', 'Major transit hub in Brooklyn.'),
('Abandoned Warehouse', 40.7000, -74.1000, 'Building', 'Old warehouse with no connections.');  -- ISOLATED NODE


-- === INSERT EDGES (YOUR ORIGINAL CONNECTIONS) ===
INSERT INTO edge (from_location, to_location, weight, directed, name, active) VALUES
(1, 2, 2.5, FALSE, 'Broadway Ave', TRUE),
(2, 3, 1.2, FALSE, '5th Ave', TRUE),
(3, 4, 0.8, FALSE, '42nd Street', TRUE),
(4, 5, 3.1, FALSE, 'Main St Connector', TRUE),
(5, 7, 2.0, FALSE, 'Brooklyn Access', TRUE),
(7, 8, 1.1, FALSE, 'Waterfront Road', TRUE),
(8, 6, 4.5, FALSE, 'Liberty Route', TRUE),
(2, 9, 1.9, FALSE, 'Canal Street', TRUE),
(9, 7, 1.0, FALSE, 'Market Street', TRUE),
(1, 10, 3.0, FALSE, 'Library Avenue', TRUE),
(11, 3, 1.4, FALSE, 'High Line Connector', TRUE),
(11, 16, 1.2, FALSE, 'High Line to Hudson Yards', TRUE),
(12, 3, 0.9, FALSE, 'MSG Link', TRUE),
(12, 14, 1.3, FALSE, 'MSG-Union Passage', TRUE),
(13, 1, 1.7, FALSE, 'Circle Drive', TRUE),
(13, 2, 2.0, FALSE, 'Circle Ave', TRUE),
(14, 9, 1.8, FALSE, 'Union Street', TRUE),
(14, 17, 2.2, FALSE, 'Union to Chelsea', TRUE),
(15, 4, 2.9, FALSE, 'Roosevelt Cableway', TRUE),
(16, 7, 2.6, FALSE, 'Hudson Trade Route', TRUE),
(17, 11, 1.1, FALSE, 'Chelsea Walkway', TRUE),
(18, 10, 4.3, FALSE, 'Brooklyn Connector', TRUE),
(18, 5, 3.9, FALSE, 'Junction Bridge Route', TRUE);


-- === INSERT ROUTES (sample paths) ===
INSERT INTO route (name, description) VALUES
('Tourist Trail', 'A route connecting main tourist attractions in the city.'),
('Downtown Commute', 'Efficient path for commuters between Wall Street and Central Park.'),
('Historic Walk', 'Walking route covering key landmarks and cultural districts.');

-- === LINK ROUTES TO LOCATIONS ===
-- Route 1: Tourist Trail
INSERT INTO route_location (route_id, location_id, position) VALUES
(1, 1, 1),  -- Central Park
(1, 2, 2),  -- Times Square
(1, 3, 3),  -- Empire State Building
(1, 4, 4),  -- Grand Central Station
(1, 5, 5),  -- Brooklyn Bridge
(1, 6, 6);  -- Statue of Liberty

-- Route 2: Downtown Commute
INSERT INTO route_location (route_id, location_id, position) VALUES
(2, 7, 1),  -- Wall Street
(2, 8, 2),  -- Battery Park
(2, 5, 3),  -- Brooklyn Bridge
(2, 3, 4),  -- Empire State Building
(2, 1, 5);  -- Central Park

-- Route 3: Historic Walk
INSERT INTO route_location (route_id, location_id, position) VALUES
(3, 9, 1),  -- Chinatown
(3, 7, 2),  -- Wall Street
(3, 8, 3),  -- Battery Park
(3, 6, 4);  -- Statue of Liberty
