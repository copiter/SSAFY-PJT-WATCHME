create table member_qna
(
    qna_id        bigint auto_increment
        primary key,
    qun_text      text         null,
    created_at    datetime(6)  null,
    qna_title     varchar(255) null,
    updated_at    datetime(6)  null,
    answerer_id   bigint       null,
    questioner_id bigint       null,
    constraint FKeupsd3c60bxo7q8hdjsbg1w5s
        foreign key (answerer_id) references member (member_id),
    constraint FKn5c06eg8bqkddmi8nvcpt4vgo
        foreign key (questioner_id) references member (member_id)
);

