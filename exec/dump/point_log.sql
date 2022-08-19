create table point_log
(
    pl_id       bigint auto_increment
        primary key,
    created_at  datetime(6)  null,
    finish      int          null,
    pg_token    varchar(255) null,
    point_value int          null,
    member_id   bigint       null,
    sprint_id   bigint       null,
    constraint FKpc5s54gy4e1gcvel1ymljexv
        foreign key (member_id) references member (member_id),
    constraint FKt8893n15ioaeo4rk6428teq70
        foreign key (sprint_id) references sprint (sprint_id)
);

INSERT INTO WatchMe.point_log (pl_id, created_at, finish, pg_token, point_value, member_id, sprint_id) VALUES (1, '2022-08-18 23:59:22.829000', null, '28aac993aa3c8625c11e', 100000, 65, null);
INSERT INTO WatchMe.point_log (pl_id, created_at, finish, pg_token, point_value, member_id, sprint_id) VALUES (2, '2022-08-19 00:01:00.127000', null, 'f1021d21825eb37ecebb', 50000, 66, null);
INSERT INTO WatchMe.point_log (pl_id, created_at, finish, pg_token, point_value, member_id, sprint_id) VALUES (3, '2022-08-19 00:01:29.871000', null, '0cdb08b43c15b6eeb1ab', 50000, 67, null);
INSERT INTO WatchMe.point_log (pl_id, created_at, finish, pg_token, point_value, member_id, sprint_id) VALUES (4, '2022-08-19 00:01:45.475000', null, null, -10000, 67, 1);
INSERT INTO WatchMe.point_log (pl_id, created_at, finish, pg_token, point_value, member_id, sprint_id) VALUES (5, '2022-08-19 00:05:49.495000', null, null, -10000, 66, 1);
INSERT INTO WatchMe.point_log (pl_id, created_at, finish, pg_token, point_value, member_id, sprint_id) VALUES (6, '2022-08-19 00:06:17.308000', null, 'e3643afec7978db7fcbc', 100000, 16, null);
INSERT INTO WatchMe.point_log (pl_id, created_at, finish, pg_token, point_value, member_id, sprint_id) VALUES (7, '2022-08-19 00:06:26.142000', null, null, -10000, 65, 1);
INSERT INTO WatchMe.point_log (pl_id, created_at, finish, pg_token, point_value, member_id, sprint_id) VALUES (8, '2022-08-19 00:08:28.503000', null, '2eab6a9b3701690a1ed1', 100000, 7, null);
INSERT INTO WatchMe.point_log (pl_id, created_at, finish, pg_token, point_value, member_id, sprint_id) VALUES (9, '2022-08-19 00:09:05.254000', null, 'c7530ec8852f434609fa', 100000, 15, null);
INSERT INTO WatchMe.point_log (pl_id, created_at, finish, pg_token, point_value, member_id, sprint_id) VALUES (10, '2022-08-19 00:09:16.862000', null, null, -30000, 15, 7);
INSERT INTO WatchMe.point_log (pl_id, created_at, finish, pg_token, point_value, member_id, sprint_id) VALUES (11, '2022-08-19 00:11:47.872000', null, 'e9ec0b4d90e91014e40c', 100000, 14, null);
INSERT INTO WatchMe.point_log (pl_id, created_at, finish, pg_token, point_value, member_id, sprint_id) VALUES (12, '2022-08-19 00:13:22.946000', null, null, -15000, 3, 10);
INSERT INTO WatchMe.point_log (pl_id, created_at, finish, pg_token, point_value, member_id, sprint_id) VALUES (13, '2022-08-19 00:14:32.961000', null, '5ce76904d07cad20ecfc', 100000, 12, null);
INSERT INTO WatchMe.point_log (pl_id, created_at, finish, pg_token, point_value, member_id, sprint_id) VALUES (14, '2022-08-19 00:14:42.338000', null, null, -10000, 12, 11);
INSERT INTO WatchMe.point_log (pl_id, created_at, finish, pg_token, point_value, member_id, sprint_id) VALUES (15, '2022-08-19 00:15:29.287000', null, '4a20fd067caf7434564a', 50000, 3, null);
INSERT INTO WatchMe.point_log (pl_id, created_at, finish, pg_token, point_value, member_id, sprint_id) VALUES (16, '2022-08-19 00:17:37.630000', null, null, 0, 13, 12);
INSERT INTO WatchMe.point_log (pl_id, created_at, finish, pg_token, point_value, member_id, sprint_id) VALUES (17, '2022-08-19 00:17:58.749000', null, '693138fba44f9c9ab26a', 100000, 13, null);
INSERT INTO WatchMe.point_log (pl_id, created_at, finish, pg_token, point_value, member_id, sprint_id) VALUES (18, '2022-08-19 00:18:10.909000', null, null, -30000, 3, 8);
INSERT INTO WatchMe.point_log (pl_id, created_at, finish, pg_token, point_value, member_id, sprint_id) VALUES (19, '2022-08-19 00:19:37.320000', null, null, -10000, 16, 4);
INSERT INTO WatchMe.point_log (pl_id, created_at, finish, pg_token, point_value, member_id, sprint_id) VALUES (20, '2022-08-19 00:19:48.100000', null, null, -10000, 16, 11);
INSERT INTO WatchMe.point_log (pl_id, created_at, finish, pg_token, point_value, member_id, sprint_id) VALUES (21, '2022-08-19 00:19:56.212000', null, null, -15000, 16, 10);
INSERT INTO WatchMe.point_log (pl_id, created_at, finish, pg_token, point_value, member_id, sprint_id) VALUES (22, '2022-08-19 00:20:04.960000', null, null, -30000, 16, 7);
INSERT INTO WatchMe.point_log (pl_id, created_at, finish, pg_token, point_value, member_id, sprint_id) VALUES (23, '2022-08-19 00:20:45.259000', null, 'e25c10c0adbd0dfc0095', 50000, 8, null);
INSERT INTO WatchMe.point_log (pl_id, created_at, finish, pg_token, point_value, member_id, sprint_id) VALUES (24, '2022-08-19 00:20:59.062000', null, null, -10000, 15, 11);
INSERT INTO WatchMe.point_log (pl_id, created_at, finish, pg_token, point_value, member_id, sprint_id) VALUES (25, '2022-08-19 00:21:08.015000', null, null, -10000, 15, 4);
INSERT INTO WatchMe.point_log (pl_id, created_at, finish, pg_token, point_value, member_id, sprint_id) VALUES (26, '2022-08-19 00:21:33.807000', null, null, -50000, 8, 13);
INSERT INTO WatchMe.point_log (pl_id, created_at, finish, pg_token, point_value, member_id, sprint_id) VALUES (27, '2022-08-19 00:21:57.246000', null, null, -100000, 7, 5);
INSERT INTO WatchMe.point_log (pl_id, created_at, finish, pg_token, point_value, member_id, sprint_id) VALUES (28, '2022-08-19 00:22:03.924000', null, '3bb57348dd4d0260917f', 100000, 15, null);
INSERT INTO WatchMe.point_log (pl_id, created_at, finish, pg_token, point_value, member_id, sprint_id) VALUES (29, '2022-08-19 00:22:54.710000', null, null, -100000, 15, 5);
INSERT INTO WatchMe.point_log (pl_id, created_at, finish, pg_token, point_value, member_id, sprint_id) VALUES (30, '2022-08-19 00:23:33.802000', null, null, -10000, 14, 9);
INSERT INTO WatchMe.point_log (pl_id, created_at, finish, pg_token, point_value, member_id, sprint_id) VALUES (31, '2022-08-19 00:23:40.742000', null, null, -10000, 14, 11);
INSERT INTO WatchMe.point_log (pl_id, created_at, finish, pg_token, point_value, member_id, sprint_id) VALUES (32, '2022-08-19 00:23:47.443000', null, null, -30000, 14, 7);
INSERT INTO WatchMe.point_log (pl_id, created_at, finish, pg_token, point_value, member_id, sprint_id) VALUES (33, '2022-08-19 00:23:58.759000', null, null, -10000, 14, 4);
INSERT INTO WatchMe.point_log (pl_id, created_at, finish, pg_token, point_value, member_id, sprint_id) VALUES (34, '2022-08-19 00:24:02.875000', null, null, 10000, 14, 4);
INSERT INTO WatchMe.point_log (pl_id, created_at, finish, pg_token, point_value, member_id, sprint_id) VALUES (35, '2022-08-19 00:24:04.773000', null, null, -10000, 14, 4);
INSERT INTO WatchMe.point_log (pl_id, created_at, finish, pg_token, point_value, member_id, sprint_id) VALUES (36, '2022-08-19 00:24:35.527000', null, null, 0, 12, 12);
INSERT INTO WatchMe.point_log (pl_id, created_at, finish, pg_token, point_value, member_id, sprint_id) VALUES (37, '2022-08-19 00:24:41.714000', null, null, -10000, 12, 9);
INSERT INTO WatchMe.point_log (pl_id, created_at, finish, pg_token, point_value, member_id, sprint_id) VALUES (38, '2022-08-19 00:24:50.154000', null, null, -10000, 12, 4);
INSERT INTO WatchMe.point_log (pl_id, created_at, finish, pg_token, point_value, member_id, sprint_id) VALUES (39, '2022-08-19 00:25:19.557000', null, null, -10000, 13, 2);
INSERT INTO WatchMe.point_log (pl_id, created_at, finish, pg_token, point_value, member_id, sprint_id) VALUES (40, '2022-08-19 00:25:24.782000', null, null, -10000, 13, 4);
INSERT INTO WatchMe.point_log (pl_id, created_at, finish, pg_token, point_value, member_id, sprint_id) VALUES (41, '2022-08-19 00:25:32.127000', null, null, -50000, 13, 13);
INSERT INTO WatchMe.point_log (pl_id, created_at, finish, pg_token, point_value, member_id, sprint_id) VALUES (42, '2022-08-19 00:28:00.355000', null, null, 0, 10, 14);
INSERT INTO WatchMe.point_log (pl_id, created_at, finish, pg_token, point_value, member_id, sprint_id) VALUES (43, '2022-08-19 00:28:05.003000', null, null, 0, 9, 14);
INSERT INTO WatchMe.point_log (pl_id, created_at, finish, pg_token, point_value, member_id, sprint_id) VALUES (44, '2022-08-19 00:33:34.904000', null, 'af2721b3f3c89756bb82', 100000, 2, null);
INSERT INTO WatchMe.point_log (pl_id, created_at, finish, pg_token, point_value, member_id, sprint_id) VALUES (45, '2022-08-19 00:33:53.597000', null, null, -10000, 2, 11);
INSERT INTO WatchMe.point_log (pl_id, created_at, finish, pg_token, point_value, member_id, sprint_id) VALUES (46, '2022-08-19 00:37:42.367000', null, null, -20000, 3, null);
INSERT INTO WatchMe.point_log (pl_id, created_at, finish, pg_token, point_value, member_id, sprint_id) VALUES (47, '2022-08-19 01:53:18.773000', null, '4bbfafbecb59a0db5256', 5000, 68, null);
INSERT INTO WatchMe.point_log (pl_id, created_at, finish, pg_token, point_value, member_id, sprint_id) VALUES (48, '2022-08-19 01:53:45.931000', null, 'ef1d7b4cc1409f58d3e7', 20000, 68, null);
INSERT INTO WatchMe.point_log (pl_id, created_at, finish, pg_token, point_value, member_id, sprint_id) VALUES (49, '2022-08-19 01:54:13.089000', null, '585673264c9a2e33519d', 50000, 68, null);
INSERT INTO WatchMe.point_log (pl_id, created_at, finish, pg_token, point_value, member_id, sprint_id) VALUES (50, '2022-08-19 01:54:18.606000', null, null, -10000, 68, null);
INSERT INTO WatchMe.point_log (pl_id, created_at, finish, pg_token, point_value, member_id, sprint_id) VALUES (51, '2022-08-19 01:54:49.810000', null, null, -15000, 68, 10);
INSERT INTO WatchMe.point_log (pl_id, created_at, finish, pg_token, point_value, member_id, sprint_id) VALUES (52, '2022-08-19 02:19:59', null, null, -500, 68, 10);
INSERT INTO WatchMe.point_log (pl_id, created_at, finish, pg_token, point_value, member_id, sprint_id) VALUES (53, '2022-08-19 02:20:00', null, null, -500, 68, 10);
INSERT INTO WatchMe.point_log (pl_id, created_at, finish, pg_token, point_value, member_id, sprint_id) VALUES (54, '2022-08-19 02:20:01', null, null, -500, 68, 10);
INSERT INTO WatchMe.point_log (pl_id, created_at, finish, pg_token, point_value, member_id, sprint_id) VALUES (55, '2022-08-19 02:20:03', null, null, -500, 68, 10);
INSERT INTO WatchMe.point_log (pl_id, created_at, finish, pg_token, point_value, member_id, sprint_id) VALUES (56, '2022-08-19 02:24:57.106000', null, null, -25000, 68, 15);
INSERT INTO WatchMe.point_log (pl_id, created_at, finish, pg_token, point_value, member_id, sprint_id) VALUES (57, '2022-08-19 02:32:52.453000', null, null, -1000, 68, null);
INSERT INTO WatchMe.point_log (pl_id, created_at, finish, pg_token, point_value, member_id, sprint_id) VALUES (58, '2022-08-19 03:33:14', null, null, -1500, 68, 15);
