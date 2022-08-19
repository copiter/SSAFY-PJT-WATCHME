create table member_email_key
(
    member_id  bigint       not null
        primary key,
    created_at datetime(6)  null,
    email_key  varchar(255) null,
    constraint FKatbdbhkyvu5k6f54v1goy9a8n
        foreign key (member_id) references member (member_id)
);

INSERT INTO WatchMe.member_email_key (member_id, created_at, email_key) VALUES (68, '2022-08-19 01:45:32.886000', '28b028c7-2469-474a-b96e-5f989cae1878');
