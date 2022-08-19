create table member_report
(
    report_id     bigint auto_increment
        primary key,
    report_reason varchar(255) null,
    rc_id         int          null,
    reportee_id   bigint       null,
    reporter_id   bigint       null,
    constraint FKex9f8w3s59ebmed17rsbdd192
        foreign key (reportee_id) references member (member_id),
    constraint FKojtg6oibs0pd1s4oi23vip8xm
        foreign key (rc_id) references report_category (rc_id),
    constraint FKqh59qob6qgguicgk6pocuiw9p
        foreign key (reporter_id) references member (member_id)
);

