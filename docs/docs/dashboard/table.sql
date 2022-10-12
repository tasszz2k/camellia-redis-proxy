CREATE TABLE `camellia_resource_info`
(
    `id`          bigint(64)    NOT NULL primary key auto_increment comment 'Auto increment field',
    `url`         varchar(1024) NOT NULL comment 'Resource url',
    `info`        varchar(1024) NOT NULL comment 'description',
    `tids`        varchar(1024) DEFAULT NULL comment 'referenced tids',
    `create_time` varchar(2000) DEFAULT NULL comment 'create time',
    `update_time` varchar(64)   DEFAULT NULL comment 'update time'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT ='Resource Information Table';

CREATE TABLE `camellia_table`
(
    `tid`         bigint(64)    NOT NULL primary key auto_increment comment 'Auto increment field',
    `detail`      varchar(4096) NOT NULL comment 'detail',
    `info`        varchar(1024) NOT NULL comment 'description',
    `valid_flag`  tinyint(4)    DEFAULT NULL comment 'Is it valid',
    `create_time` varchar(2000) DEFAULT NULL comment 'create time',
    `update_time` varchar(64)   DEFAULT NULL comment 'update time'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT ='Resource table';

CREATE TABLE `camellia_table_ref`
(
    `id`          bigint(64)    NOT NULL primary key auto_increment comment 'Auto increment field',
    `bid`         bigint(64)    NOT NULL comment 'bid',
    `bgroup`      varchar(64)   NOT NULL comment 'bgroup',
    `tid`         bigint(64)    NOT NULL comment 'tid',
    `info`        varchar(1024) NOT NULL comment 'description',
    `valid_flag`  tinyint(4)    DEFAULT NULL comment 'Is it valid',
    `create_time` varchar(2000) DEFAULT NULL comment 'create time',
    `update_time` varchar(64)   DEFAULT NULL comment 'Update time',
    unique key (`bid`, `bgroup`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT ='Resource reference table';