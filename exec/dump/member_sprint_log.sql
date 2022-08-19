create table member_sprint_log
(
    sprint_log_id bigint auto_increment
        primary key,
    status        varchar(255) null,
    member_id     bigint       null,
    sprint_id     bigint       null,
    constraint FKal3514lv5hdtwt4jbh5imp41j
        foreign key (sprint_id) references sprint (sprint_id),
    constraint FKnbcn8js4h2tbstb94t8gmhx4u
        foreign key (member_id) references member (member_id)
);

INSERT INTO WatchMe.member_sprint_log (sprint_log_id, status, member_id, sprint_id) VALUES (1, 'YES', 67, 1);
INSERT INTO WatchMe.member_sprint_log (sprint_log_id, status, member_id, sprint_id) VALUES (2, 'YES', 66, 1);
INSERT INTO WatchMe.member_sprint_log (sprint_log_id, status, member_id, sprint_id) VALUES (3, 'YES', 65, 1);
INSERT INTO WatchMe.member_sprint_log (sprint_log_id, status, member_id, sprint_id) VALUES (4, 'YES', 15, 7);
INSERT INTO WatchMe.member_sprint_log (sprint_log_id, status, member_id, sprint_id) VALUES (5, 'YES', 3, 10);
INSERT INTO WatchMe.member_sprint_log (sprint_log_id, status, member_id, sprint_id) VALUES (6, 'YES', 12, 11);
INSERT INTO WatchMe.member_sprint_log (sprint_log_id, status, member_id, sprint_id) VALUES (7, 'YES', 13, 12);
INSERT INTO WatchMe.member_sprint_log (sprint_log_id, status, member_id, sprint_id) VALUES (8, 'YES', 3, 8);
INSERT INTO WatchMe.member_sprint_log (sprint_log_id, status, member_id, sprint_id) VALUES (9, 'YES', 16, 4);
INSERT INTO WatchMe.member_sprint_log (sprint_log_id, status, member_id, sprint_id) VALUES (10, 'YES', 16, 11);
INSERT INTO WatchMe.member_sprint_log (sprint_log_id, status, member_id, sprint_id) VALUES (11, 'YES', 16, 10);
INSERT INTO WatchMe.member_sprint_log (sprint_log_id, status, member_id, sprint_id) VALUES (12, 'YES', 16, 7);
INSERT INTO WatchMe.member_sprint_log (sprint_log_id, status, member_id, sprint_id) VALUES (13, 'YES', 15, 11);
INSERT INTO WatchMe.member_sprint_log (sprint_log_id, status, member_id, sprint_id) VALUES (14, 'YES', 15, 4);
INSERT INTO WatchMe.member_sprint_log (sprint_log_id, status, member_id, sprint_id) VALUES (15, 'YES', 8, 13);
INSERT INTO WatchMe.member_sprint_log (sprint_log_id, status, member_id, sprint_id) VALUES (16, 'YES', 7, 5);
INSERT INTO WatchMe.member_sprint_log (sprint_log_id, status, member_id, sprint_id) VALUES (17, 'YES', 15, 5);
INSERT INTO WatchMe.member_sprint_log (sprint_log_id, status, member_id, sprint_id) VALUES (18, 'YES', 14, 9);
INSERT INTO WatchMe.member_sprint_log (sprint_log_id, status, member_id, sprint_id) VALUES (19, 'YES', 14, 11);
INSERT INTO WatchMe.member_sprint_log (sprint_log_id, status, member_id, sprint_id) VALUES (20, 'YES', 14, 7);
INSERT INTO WatchMe.member_sprint_log (sprint_log_id, status, member_id, sprint_id) VALUES (21, 'YES', 14, 4);
INSERT INTO WatchMe.member_sprint_log (sprint_log_id, status, member_id, sprint_id) VALUES (22, 'YES', 12, 12);
INSERT INTO WatchMe.member_sprint_log (sprint_log_id, status, member_id, sprint_id) VALUES (23, 'YES', 12, 9);
INSERT INTO WatchMe.member_sprint_log (sprint_log_id, status, member_id, sprint_id) VALUES (24, 'YES', 12, 4);
INSERT INTO WatchMe.member_sprint_log (sprint_log_id, status, member_id, sprint_id) VALUES (25, 'YES', 13, 2);
INSERT INTO WatchMe.member_sprint_log (sprint_log_id, status, member_id, sprint_id) VALUES (26, 'YES', 13, 4);
INSERT INTO WatchMe.member_sprint_log (sprint_log_id, status, member_id, sprint_id) VALUES (27, 'YES', 13, 13);
INSERT INTO WatchMe.member_sprint_log (sprint_log_id, status, member_id, sprint_id) VALUES (28, 'YES', 10, 14);
INSERT INTO WatchMe.member_sprint_log (sprint_log_id, status, member_id, sprint_id) VALUES (29, 'YES', 9, 14);
INSERT INTO WatchMe.member_sprint_log (sprint_log_id, status, member_id, sprint_id) VALUES (30, 'YES', 2, 11);
INSERT INTO WatchMe.member_sprint_log (sprint_log_id, status, member_id, sprint_id) VALUES (31, 'YES', 68, 10);
INSERT INTO WatchMe.member_sprint_log (sprint_log_id, status, member_id, sprint_id) VALUES (32, 'YES', 68, 15);
