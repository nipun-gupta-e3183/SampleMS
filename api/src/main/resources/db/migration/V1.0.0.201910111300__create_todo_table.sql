create table todo (
    ID bigint not null AUTO_INCREMENT,
    TITLE varchar(255) not null,
    COMPLETED TINYINT(1),
    PRIMARY KEY (ID)
)
ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
