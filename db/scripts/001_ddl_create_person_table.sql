create table if not exists person (
                        id serial primary key not null,
                        login unique varchar(2000),
                        password varchar(2000)
);