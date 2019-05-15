create table blog
(
    id                 int auto_increment primary key,
    user_id            bigint (10)  null,
    title              varchar(100) null,
    description         varchar(100) null,
    content             text null,
    created_at         datetime     null,
    updated_at         datetime     null
)