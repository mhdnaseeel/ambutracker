ALTER TABLE emergencies
ADD COLUMN emergency_type VARCHAR(50),
ADD COLUMN patient_name VARCHAR(100),
ADD COLUMN patient_age INT,
ADD COLUMN patient_condition TEXT,
ADD COLUMN pickup_location TEXT,
ADD COLUMN pickup_latitude DOUBLE,
ADD COLUMN pickup_longitude DOUBLE,
ADD COLUMN destination_location TEXT,
ADD COLUMN destination_latitude DOUBLE,
ADD COLUMN destination_longitude DOUBLE; 