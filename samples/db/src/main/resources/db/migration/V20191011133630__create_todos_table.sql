create table todos (
    id bigint not null auto_increment,
    title varchar(191) not null,
    completed tinyint(1),
    primary key (id)
)
ENGINE=InnoDB default CHARSET=utf8mb4;
