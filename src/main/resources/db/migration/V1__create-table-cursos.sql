CREATE TABLE cursos (
    id bigint not null auto_increment primary key,
    nome varchar(100) not null unique,
    periodo VARCHAR(20) NOT NULL CHECK (periodo IN ('MATUTINO', 'VESPERTINO', 'NOTURNO', 'INTEGRAL')),
    ativo tinyint not null default 1
);