create table announcement
(
    announce_id    bigint auto_increment
        primary key,
    annonce_text   varchar(255) null,
    announce_title varchar(255) null,
    member_id      bigint       null,
    constraint FKibvknlhmqcob0njrw9cghkirb
        foreign key (member_id) references member (member_id)
);

