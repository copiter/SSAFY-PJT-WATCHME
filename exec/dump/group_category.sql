create table group_category
(
    group_ctg_id bigint auto_increment
        primary key,
    ctg_id       int    null,
    group_id     bigint null,
    constraint FKe5dljefvehxicvpppf1rq1j22
        foreign key (ctg_id) references category (ctg_id),
    constraint FKhrplonnfocxl9l9h4k8ac0ugv
        foreign key (group_id) references groupss (group_id)
);

INSERT INTO WatchMe.group_category (group_ctg_id, ctg_id, group_id) VALUES (1, 4, 1);
INSERT INTO WatchMe.group_category (group_ctg_id, ctg_id, group_id) VALUES (2, 2, 2);
INSERT INTO WatchMe.group_category (group_ctg_id, ctg_id, group_id) VALUES (3, 3, 2);
INSERT INTO WatchMe.group_category (group_ctg_id, ctg_id, group_id) VALUES (4, 1, 2);
INSERT INTO WatchMe.group_category (group_ctg_id, ctg_id, group_id) VALUES (5, 1, 3);
INSERT INTO WatchMe.group_category (group_ctg_id, ctg_id, group_id) VALUES (6, 4, 4);
INSERT INTO WatchMe.group_category (group_ctg_id, ctg_id, group_id) VALUES (7, 6, 4);
INSERT INTO WatchMe.group_category (group_ctg_id, ctg_id, group_id) VALUES (8, 6, 5);
INSERT INTO WatchMe.group_category (group_ctg_id, ctg_id, group_id) VALUES (9, 6, 6);
INSERT INTO WatchMe.group_category (group_ctg_id, ctg_id, group_id) VALUES (10, 2, 7);
INSERT INTO WatchMe.group_category (group_ctg_id, ctg_id, group_id) VALUES (11, 3, 7);
INSERT INTO WatchMe.group_category (group_ctg_id, ctg_id, group_id) VALUES (13, 3, 9);
INSERT INTO WatchMe.group_category (group_ctg_id, ctg_id, group_id) VALUES (14, 4, 9);
INSERT INTO WatchMe.group_category (group_ctg_id, ctg_id, group_id) VALUES (15, 3, 10);
INSERT INTO WatchMe.group_category (group_ctg_id, ctg_id, group_id) VALUES (16, 6, 10);
INSERT INTO WatchMe.group_category (group_ctg_id, ctg_id, group_id) VALUES (17, 2, 8);
INSERT INTO WatchMe.group_category (group_ctg_id, ctg_id, group_id) VALUES (18, 2, 11);
INSERT INTO WatchMe.group_category (group_ctg_id, ctg_id, group_id) VALUES (19, 3, 11);
INSERT INTO WatchMe.group_category (group_ctg_id, ctg_id, group_id) VALUES (20, 4, 11);
INSERT INTO WatchMe.group_category (group_ctg_id, ctg_id, group_id) VALUES (21, 2, 12);
INSERT INTO WatchMe.group_category (group_ctg_id, ctg_id, group_id) VALUES (22, 3, 13);
INSERT INTO WatchMe.group_category (group_ctg_id, ctg_id, group_id) VALUES (23, 4, 13);
INSERT INTO WatchMe.group_category (group_ctg_id, ctg_id, group_id) VALUES (24, 3, 14);
INSERT INTO WatchMe.group_category (group_ctg_id, ctg_id, group_id) VALUES (25, 1, 14);
INSERT INTO WatchMe.group_category (group_ctg_id, ctg_id, group_id) VALUES (26, 4, 14);
INSERT INTO WatchMe.group_category (group_ctg_id, ctg_id, group_id) VALUES (27, 4, 15);
INSERT INTO WatchMe.group_category (group_ctg_id, ctg_id, group_id) VALUES (28, 6, 16);
INSERT INTO WatchMe.group_category (group_ctg_id, ctg_id, group_id) VALUES (29, 5, 17);
INSERT INTO WatchMe.group_category (group_ctg_id, ctg_id, group_id) VALUES (30, 3, 18);
INSERT INTO WatchMe.group_category (group_ctg_id, ctg_id, group_id) VALUES (31, 1, 18);
