create table member_group
(
    mg_id      bigint auto_increment
        primary key,
    created_at datetime(6)  null,
    group_role varchar(255) null,
    group_id   bigint       null,
    member_id  bigint       null,
    constraint FKe0o479fanotmeoe55q0crr01i
        foreign key (group_id) references groupss (group_id),
    constraint FKi9080rfwtrt5jlvim4mcg4rl4
        foreign key (member_id) references member (member_id)
);

INSERT INTO WatchMe.member_group (mg_id, created_at, group_role, group_id, member_id) VALUES (1, '2022-08-18 23:51:25.483000', 'LEADER', 1, 65);
INSERT INTO WatchMe.member_group (mg_id, created_at, group_role, group_id, member_id) VALUES (2, '2022-08-18 23:52:29.574000', 'MEMBER', 1, 66);
INSERT INTO WatchMe.member_group (mg_id, created_at, group_role, group_id, member_id) VALUES (3, '2022-08-18 23:52:33.590000', 'MEMBER', 1, 67);
INSERT INTO WatchMe.member_group (mg_id, created_at, group_role, group_id, member_id) VALUES (4, '2022-08-18 23:56:32.091000', 'LEADER', 2, 2);
INSERT INTO WatchMe.member_group (mg_id, created_at, group_role, group_id, member_id) VALUES (5, '2022-08-18 23:56:38.099000', 'LEADER', 3, 12);
INSERT INTO WatchMe.member_group (mg_id, created_at, group_role, group_id, member_id) VALUES (6, '2022-08-18 23:57:36.838000', 'LEADER', 4, 3);
INSERT INTO WatchMe.member_group (mg_id, created_at, group_role, group_id, member_id) VALUES (7, '2022-08-18 23:58:39.835000', 'LEADER', 5, 4);
INSERT INTO WatchMe.member_group (mg_id, created_at, group_role, group_id, member_id) VALUES (8, '2022-08-18 23:58:56.737000', 'LEADER', 6, 13);
INSERT INTO WatchMe.member_group (mg_id, created_at, group_role, group_id, member_id) VALUES (9, '2022-08-19 00:00:33.791000', 'LEADER', 7, 14);
INSERT INTO WatchMe.member_group (mg_id, created_at, group_role, group_id, member_id) VALUES (10, '2022-08-19 00:01:24.618000', 'LEADER', 8, 7);
INSERT INTO WatchMe.member_group (mg_id, created_at, group_role, group_id, member_id) VALUES (11, '2022-08-19 00:01:42.121000', 'LEADER', 9, 4);
INSERT INTO WatchMe.member_group (mg_id, created_at, group_role, group_id, member_id) VALUES (12, '2022-08-19 00:02:01.427000', 'LEADER', 10, 15);
INSERT INTO WatchMe.member_group (mg_id, created_at, group_role, group_id, member_id) VALUES (13, '2022-08-19 00:03:18.582000', 'LEADER', 11, 5);
INSERT INTO WatchMe.member_group (mg_id, created_at, group_role, group_id, member_id) VALUES (14, '2022-08-19 00:03:30.973000', 'LEADER', 12, 7);
INSERT INTO WatchMe.member_group (mg_id, created_at, group_role, group_id, member_id) VALUES (15, '2022-08-19 00:03:54.344000', 'LEADER', 13, 16);
INSERT INTO WatchMe.member_group (mg_id, created_at, group_role, group_id, member_id) VALUES (16, '2022-08-19 00:06:33.980000', 'LEADER', 14, 6);
INSERT INTO WatchMe.member_group (mg_id, created_at, group_role, group_id, member_id) VALUES (17, '2022-08-19 00:09:14.149000', 'MEMBER', 2, 3);
INSERT INTO WatchMe.member_group (mg_id, created_at, group_role, group_id, member_id) VALUES (18, '2022-08-19 00:09:19.847000', 'MEMBER', 2, 6);
INSERT INTO WatchMe.member_group (mg_id, created_at, group_role, group_id, member_id) VALUES (19, '2022-08-19 00:09:21.261000', 'MEMBER', 10, 16);
INSERT INTO WatchMe.member_group (mg_id, created_at, group_role, group_id, member_id) VALUES (20, '2022-08-19 00:09:23.425000', 'MEMBER', 2, 4);
INSERT INTO WatchMe.member_group (mg_id, created_at, group_role, group_id, member_id) VALUES (21, '2022-08-19 00:12:22.020000', 'MEMBER', 4, 4);
INSERT INTO WatchMe.member_group (mg_id, created_at, group_role, group_id, member_id) VALUES (22, '2022-08-19 00:12:24.577000', 'MEMBER', 4, 6);
INSERT INTO WatchMe.member_group (mg_id, created_at, group_role, group_id, member_id) VALUES (23, '2022-08-19 00:12:27.608000', 'MEMBER', 4, 16);
INSERT INTO WatchMe.member_group (mg_id, created_at, group_role, group_id, member_id) VALUES (24, '2022-08-19 00:12:30.519000', 'MEMBER', 4, 2);
INSERT INTO WatchMe.member_group (mg_id, created_at, group_role, group_id, member_id) VALUES (25, '2022-08-19 00:12:36.621000', 'MEMBER', 3, 4);
INSERT INTO WatchMe.member_group (mg_id, created_at, group_role, group_id, member_id) VALUES (26, '2022-08-19 00:12:39.557000', 'MEMBER', 3, 6);
INSERT INTO WatchMe.member_group (mg_id, created_at, group_role, group_id, member_id) VALUES (27, '2022-08-19 00:12:43.076000', 'MEMBER', 3, 16);
INSERT INTO WatchMe.member_group (mg_id, created_at, group_role, group_id, member_id) VALUES (28, '2022-08-19 00:12:46.442000', 'MEMBER', 3, 15);
INSERT INTO WatchMe.member_group (mg_id, created_at, group_role, group_id, member_id) VALUES (29, '2022-08-19 00:12:49.205000', 'MEMBER', 3, 2);
INSERT INTO WatchMe.member_group (mg_id, created_at, group_role, group_id, member_id) VALUES (30, '2022-08-19 00:12:50.726000', 'MEMBER', 12, 15);
INSERT INTO WatchMe.member_group (mg_id, created_at, group_role, group_id, member_id) VALUES (31, '2022-08-19 00:12:51.984000', 'MEMBER', 3, 14);
INSERT INTO WatchMe.member_group (mg_id, created_at, group_role, group_id, member_id) VALUES (32, '2022-08-19 00:13:27.567000', 'MEMBER', 12, 8);
INSERT INTO WatchMe.member_group (mg_id, created_at, group_role, group_id, member_id) VALUES (33, '2022-08-19 00:15:37.450000', 'MEMBER', 6, 6);
INSERT INTO WatchMe.member_group (mg_id, created_at, group_role, group_id, member_id) VALUES (34, '2022-08-19 00:15:42.693000', 'MEMBER', 6, 12);
INSERT INTO WatchMe.member_group (mg_id, created_at, group_role, group_id, member_id) VALUES (35, '2022-08-19 00:18:18.854000', 'LEADER', 15, 8);
INSERT INTO WatchMe.member_group (mg_id, created_at, group_role, group_id, member_id) VALUES (36, '2022-08-19 00:18:35.840000', 'MEMBER', 5, 6);
INSERT INTO WatchMe.member_group (mg_id, created_at, group_role, group_id, member_id) VALUES (37, '2022-08-19 00:19:05.142000', 'MEMBER', 13, 6);
INSERT INTO WatchMe.member_group (mg_id, created_at, group_role, group_id, member_id) VALUES (38, '2022-08-19 00:19:07.834000', 'MEMBER', 13, 15);
INSERT INTO WatchMe.member_group (mg_id, created_at, group_role, group_id, member_id) VALUES (39, '2022-08-19 00:19:10.484000', 'MEMBER', 13, 14);
INSERT INTO WatchMe.member_group (mg_id, created_at, group_role, group_id, member_id) VALUES (40, '2022-08-19 00:19:13.303000', 'MEMBER', 13, 12);
INSERT INTO WatchMe.member_group (mg_id, created_at, group_role, group_id, member_id) VALUES (41, '2022-08-19 00:19:15.912000', 'MEMBER', 13, 13);
INSERT INTO WatchMe.member_group (mg_id, created_at, group_role, group_id, member_id) VALUES (42, '2022-08-19 00:19:31.106000', 'MEMBER', 15, 13);
INSERT INTO WatchMe.member_group (mg_id, created_at, group_role, group_id, member_id) VALUES (43, '2022-08-19 00:19:39.793000', 'MEMBER', 5, 13);
INSERT INTO WatchMe.member_group (mg_id, created_at, group_role, group_id, member_id) VALUES (44, '2022-08-19 00:20:49.569000', 'MEMBER', 10, 14);
INSERT INTO WatchMe.member_group (mg_id, created_at, group_role, group_id, member_id) VALUES (45, '2022-08-19 00:23:14.729000', 'MEMBER', 7, 16);
INSERT INTO WatchMe.member_group (mg_id, created_at, group_role, group_id, member_id) VALUES (46, '2022-08-19 00:23:17.201000', 'MEMBER', 7, 6);
INSERT INTO WatchMe.member_group (mg_id, created_at, group_role, group_id, member_id) VALUES (47, '2022-08-19 00:23:20.353000', 'MEMBER', 7, 15);
INSERT INTO WatchMe.member_group (mg_id, created_at, group_role, group_id, member_id) VALUES (48, '2022-08-19 00:23:23.052000', 'MEMBER', 7, 12);
INSERT INTO WatchMe.member_group (mg_id, created_at, group_role, group_id, member_id) VALUES (49, '2022-08-19 00:26:25.637000', 'LEADER', 16, 10);
INSERT INTO WatchMe.member_group (mg_id, created_at, group_role, group_id, member_id) VALUES (50, '2022-08-19 00:26:48.557000', 'MEMBER', 16, 9);
INSERT INTO WatchMe.member_group (mg_id, created_at, group_role, group_id, member_id) VALUES (51, '2022-08-19 00:35:18.047000', 'MEMBER', 2, 11);
INSERT INTO WatchMe.member_group (mg_id, created_at, group_role, group_id, member_id) VALUES (52, '2022-08-19 00:37:06.029000', 'MEMBER', 4, 12);
INSERT INTO WatchMe.member_group (mg_id, created_at, group_role, group_id, member_id) VALUES (53, '2022-08-19 00:39:13.403000', 'MEMBER', 11, 6);
INSERT INTO WatchMe.member_group (mg_id, created_at, group_role, group_id, member_id) VALUES (54, '2022-08-19 00:39:16.085000', 'MEMBER', 11, 13);
INSERT INTO WatchMe.member_group (mg_id, created_at, group_role, group_id, member_id) VALUES (55, '2022-08-19 00:39:18.523000', 'MEMBER', 11, 16);
INSERT INTO WatchMe.member_group (mg_id, created_at, group_role, group_id, member_id) VALUES (56, '2022-08-19 00:39:22.040000', 'MEMBER', 11, 14);
INSERT INTO WatchMe.member_group (mg_id, created_at, group_role, group_id, member_id) VALUES (57, '2022-08-19 00:48:34.729000', 'MEMBER', 15, 4);
INSERT INTO WatchMe.member_group (mg_id, created_at, group_role, group_id, member_id) VALUES (58, '2022-08-19 01:48:10.447000', 'LEADER', 17, 68);
INSERT INTO WatchMe.member_group (mg_id, created_at, group_role, group_id, member_id) VALUES (59, '2022-08-19 01:51:00.283000', 'MEMBER', 4, 68);
INSERT INTO WatchMe.member_group (mg_id, created_at, group_role, group_id, member_id) VALUES (60, '2022-08-19 10:09:04.056000', 'LEADER', 18, 13);
