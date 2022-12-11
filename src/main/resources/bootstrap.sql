CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE mediaplans(
  id uuid NOT NULL,
  name VARCHAR NOT NULL
);

INSERT INTO mediaplans VALUES
(uuid_generate_v4(), 'Mediaplan 1'),
(uuid_generate_v4(), 'Mediaplan 2');
