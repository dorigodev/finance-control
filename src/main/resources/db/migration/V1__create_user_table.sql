CREATE TABLE users(
    id SERIAL PRIMARY KEY,
    name varchar(255) not null,
    email varchar(255) not null UNIQUE,
    password varchar(255) not null,
    is_Active bool DEFAULT True,
    role VARCHAR(50) DEFAULT 'USER',
    created_at timestamp,
    updated_at timestamp
)