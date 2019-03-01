CREATE KEYSPACE animals WITH replication = {'class':'SimpleStrategy', 'replication_factor' : 1};
--
USE animals;

CREATE TABLE alpakka (
  id int PRIMARY KEY,
  name text,
  animal_type text
  );