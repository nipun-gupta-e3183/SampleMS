create table todos (
    ID bigint not null auto_increment,
    TITLE varchar(255) not null,
    COMPLETED tinyint(1),
    primary key (ID)
)
ENGINE=InnoDB default CHARSET=utf8mb4;
