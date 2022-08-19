create table group_apply_log
(
    group_log_id bigint auto_increment
        primary key,
    apply_date   datetime(6) null,
    status       int         not null,
    update_date  datetime(6) null,
    group_id     bigint      null,
    member_id    bigint      null,
    constraint FK6tddeoxexsdv5wxqrnyo20gyu
        foreign key (member_id) references member (member_id),
    constraint FKd536dqujn99sokotogmhx8del
        foreign key (group_id) references groupss (group_id)
);

INSERT INTO WatchMe.group_apply_log (group_log_id, apply_date, status, update_date, group_id, member_id) VALUES (1, '2022-08-18 23:51:25.488000', 1, null, 1, 65);
INSERT INTO WatchMe.group_apply_log (group_log_id, apply_date, status, update_date, group_id, member_id) VALUES (2, '2022-08-18 23:51:44.257000', 1, '2022-08-18 23:52:29.571000', 1, 66);
INSERT INTO WatchMe.group_apply_log (group_log_id, apply_date, status, update_date, group_id, member_id) VALUES (3, '2022-08-18 23:51:57.660000', 1, '2022-08-18 23:52:33.590000', 1, 67);
INSERT INTO WatchMe.group_apply_log (group_log_id, apply_date, status, update_date, group_id, member_id) VALUES (4, '2022-08-18 23:56:32.094000', 1, null, 2, 2);
INSERT INTO WatchMe.group_apply_log (group_log_id, apply_date, status, update_date, group_id, member_id) VALUES (5, '2022-08-18 23:56:38.100000', 1, null, 3, 12);
INSERT INTO WatchMe.group_apply_log (group_log_id, apply_date, status, update_date, group_id, member_id) VALUES (6, '2022-08-18 23:57:02.147000', 1, '2022-08-19 00:09:14.149000', 2, 3);
INSERT INTO WatchMe.group_apply_log (group_log_id, apply_date, status, update_date, group_id, member_id) VALUES (7, '2022-08-18 23:57:36.840000', 1, null, 4, 3);
INSERT INTO WatchMe.group_apply_log (group_log_id, apply_date, status, update_date, group_id, member_id) VALUES (8, '2022-08-18 23:58:39.836000', 1, null, 5, 4);
INSERT INTO WatchMe.group_apply_log (group_log_id, apply_date, status, update_date, group_id, member_id) VALUES (9, '2022-08-18 23:58:56.738000', 1, null, 6, 13);
INSERT INTO WatchMe.group_apply_log (group_log_id, apply_date, status, update_date, group_id, member_id) VALUES (10, '2022-08-19 00:00:19.432000', 1, '2022-08-19 00:12:22.020000', 4, 4);
INSERT INTO WatchMe.group_apply_log (group_log_id, apply_date, status, update_date, group_id, member_id) VALUES (11, '2022-08-19 00:00:23.156000', 1, '2022-08-19 00:12:36.621000', 3, 4);
INSERT INTO WatchMe.group_apply_log (group_log_id, apply_date, status, update_date, group_id, member_id) VALUES (12, '2022-08-19 00:00:27.306000', 1, '2022-08-19 00:09:23.425000', 2, 4);
INSERT INTO WatchMe.group_apply_log (group_log_id, apply_date, status, update_date, group_id, member_id) VALUES (13, '2022-08-19 00:00:33.793000', 1, null, 7, 14);
INSERT INTO WatchMe.group_apply_log (group_log_id, apply_date, status, update_date, group_id, member_id) VALUES (14, '2022-08-19 00:01:24.619000', 1, null, 8, 7);
INSERT INTO WatchMe.group_apply_log (group_log_id, apply_date, status, update_date, group_id, member_id) VALUES (15, '2022-08-19 00:01:42.123000', 1, null, 9, 4);
INSERT INTO WatchMe.group_apply_log (group_log_id, apply_date, status, update_date, group_id, member_id) VALUES (16, '2022-08-19 00:02:01.429000', 1, null, 10, 15);
INSERT INTO WatchMe.group_apply_log (group_log_id, apply_date, status, update_date, group_id, member_id) VALUES (17, '2022-08-19 00:03:18.584000', 1, null, 11, 5);
INSERT INTO WatchMe.group_apply_log (group_log_id, apply_date, status, update_date, group_id, member_id) VALUES (18, '2022-08-19 00:03:30.974000', 1, null, 12, 7);
INSERT INTO WatchMe.group_apply_log (group_log_id, apply_date, status, update_date, group_id, member_id) VALUES (19, '2022-08-19 00:03:54.345000', 1, null, 13, 16);
INSERT INTO WatchMe.group_apply_log (group_log_id, apply_date, status, update_date, group_id, member_id) VALUES (20, '2022-08-19 00:04:56.012000', 1, '2022-08-19 00:09:19.847000', 2, 6);
INSERT INTO WatchMe.group_apply_log (group_log_id, apply_date, status, update_date, group_id, member_id) VALUES (21, '2022-08-19 00:05:01.457000', 1, '2022-08-19 00:12:39.557000', 3, 6);
INSERT INTO WatchMe.group_apply_log (group_log_id, apply_date, status, update_date, group_id, member_id) VALUES (22, '2022-08-19 00:05:05.638000', 1, '2022-08-19 00:12:24.577000', 4, 6);
INSERT INTO WatchMe.group_apply_log (group_log_id, apply_date, status, update_date, group_id, member_id) VALUES (23, '2022-08-19 00:05:10.995000', 1, '2022-08-19 00:18:35.840000', 5, 6);
INSERT INTO WatchMe.group_apply_log (group_log_id, apply_date, status, update_date, group_id, member_id) VALUES (24, '2022-08-19 00:05:15.052000', 1, '2022-08-19 00:39:13.403000', 11, 6);
INSERT INTO WatchMe.group_apply_log (group_log_id, apply_date, status, update_date, group_id, member_id) VALUES (25, '2022-08-19 00:05:21.626000', 1, '2022-08-19 00:19:05.141000', 13, 6);
INSERT INTO WatchMe.group_apply_log (group_log_id, apply_date, status, update_date, group_id, member_id) VALUES (26, '2022-08-19 00:06:24.355000', 1, '2022-08-19 00:12:43.076000', 3, 16);
INSERT INTO WatchMe.group_apply_log (group_log_id, apply_date, status, update_date, group_id, member_id) VALUES (27, '2022-08-19 00:06:32.934000', 0, null, 1, 16);
INSERT INTO WatchMe.group_apply_log (group_log_id, apply_date, status, update_date, group_id, member_id) VALUES (28, '2022-08-19 00:06:33.982000', 1, null, 14, 6);
INSERT INTO WatchMe.group_apply_log (group_log_id, apply_date, status, update_date, group_id, member_id) VALUES (29, '2022-08-19 00:06:43.107000', 1, '2022-08-19 00:12:27.608000', 4, 16);
INSERT INTO WatchMe.group_apply_log (group_log_id, apply_date, status, update_date, group_id, member_id) VALUES (30, '2022-08-19 00:06:49.909000', 1, '2022-08-19 00:39:18.523000', 11, 16);
INSERT INTO WatchMe.group_apply_log (group_log_id, apply_date, status, update_date, group_id, member_id) VALUES (31, '2022-08-19 00:06:55.440000', 1, '2022-08-19 00:23:14.729000', 7, 16);
INSERT INTO WatchMe.group_apply_log (group_log_id, apply_date, status, update_date, group_id, member_id) VALUES (32, '2022-08-19 00:07:07.574000', 1, '2022-08-19 00:09:21.261000', 10, 16);
INSERT INTO WatchMe.group_apply_log (group_log_id, apply_date, status, update_date, group_id, member_id) VALUES (33, '2022-08-19 00:08:30.361000', 1, '2022-08-19 00:15:37.450000', 6, 6);
INSERT INTO WatchMe.group_apply_log (group_log_id, apply_date, status, update_date, group_id, member_id) VALUES (34, '2022-08-19 00:08:38.238000', 1, '2022-08-19 00:23:17.201000', 7, 6);
INSERT INTO WatchMe.group_apply_log (group_log_id, apply_date, status, update_date, group_id, member_id) VALUES (35, '2022-08-19 00:09:37.857000', 1, '2022-08-19 00:12:46.442000', 3, 15);
INSERT INTO WatchMe.group_apply_log (group_log_id, apply_date, status, update_date, group_id, member_id) VALUES (36, '2022-08-19 00:09:44.525000', 1, '2022-08-19 00:19:07.833000', 13, 15);
INSERT INTO WatchMe.group_apply_log (group_log_id, apply_date, status, update_date, group_id, member_id) VALUES (37, '2022-08-19 00:09:51.755000', 0, null, 14, 15);
INSERT INTO WatchMe.group_apply_log (group_log_id, apply_date, status, update_date, group_id, member_id) VALUES (38, '2022-08-19 00:10:04.967000', 1, '2022-08-19 00:12:50.726000', 12, 15);
INSERT INTO WatchMe.group_apply_log (group_log_id, apply_date, status, update_date, group_id, member_id) VALUES (39, '2022-08-19 00:10:08.683000', 1, '2022-08-19 00:23:20.353000', 7, 15);
INSERT INTO WatchMe.group_apply_log (group_log_id, apply_date, status, update_date, group_id, member_id) VALUES (40, '2022-08-19 00:10:47.687000', 1, '2022-08-19 00:12:49.205000', 3, 2);
INSERT INTO WatchMe.group_apply_log (group_log_id, apply_date, status, update_date, group_id, member_id) VALUES (41, '2022-08-19 00:11:36.330000', 1, '2022-08-19 00:12:30.519000', 4, 2);
INSERT INTO WatchMe.group_apply_log (group_log_id, apply_date, status, update_date, group_id, member_id) VALUES (42, '2022-08-19 00:11:56.803000', 1, '2022-08-19 00:12:51.984000', 3, 14);
INSERT INTO WatchMe.group_apply_log (group_log_id, apply_date, status, update_date, group_id, member_id) VALUES (43, '2022-08-19 00:12:01.619000', 0, null, 1, 14);
INSERT INTO WatchMe.group_apply_log (group_log_id, apply_date, status, update_date, group_id, member_id) VALUES (44, '2022-08-19 00:12:05.555000', 1, '2022-08-19 00:20:49.569000', 10, 14);
INSERT INTO WatchMe.group_apply_log (group_log_id, apply_date, status, update_date, group_id, member_id) VALUES (45, '2022-08-19 00:12:09.619000', 1, '2022-08-19 00:39:22.040000', 11, 14);
INSERT INTO WatchMe.group_apply_log (group_log_id, apply_date, status, update_date, group_id, member_id) VALUES (46, '2022-08-19 00:12:14.475000', 1, '2022-08-19 00:19:10.484000', 13, 14);
INSERT INTO WatchMe.group_apply_log (group_log_id, apply_date, status, update_date, group_id, member_id) VALUES (47, '2022-08-19 00:13:12.075000', 1, '2022-08-19 00:13:27.567000', 12, 8);
INSERT INTO WatchMe.group_apply_log (group_log_id, apply_date, status, update_date, group_id, member_id) VALUES (48, '2022-08-19 00:14:49.605000', 0, null, 1, 12);
INSERT INTO WatchMe.group_apply_log (group_log_id, apply_date, status, update_date, group_id, member_id) VALUES (49, '2022-08-19 00:14:54.223000', 1, '2022-08-19 00:15:42.693000', 6, 12);
INSERT INTO WatchMe.group_apply_log (group_log_id, apply_date, status, update_date, group_id, member_id) VALUES (50, '2022-08-19 00:14:57.276000', 1, '2022-08-19 00:23:23.052000', 7, 12);
INSERT INTO WatchMe.group_apply_log (group_log_id, apply_date, status, update_date, group_id, member_id) VALUES (51, '2022-08-19 00:15:06.999000', 1, '2022-08-19 00:37:06.029000', 4, 12);
INSERT INTO WatchMe.group_apply_log (group_log_id, apply_date, status, update_date, group_id, member_id) VALUES (52, '2022-08-19 00:15:11.958000', 0, null, 14, 12);
INSERT INTO WatchMe.group_apply_log (group_log_id, apply_date, status, update_date, group_id, member_id) VALUES (53, '2022-08-19 00:15:19.586000', 1, '2022-08-19 00:19:13.303000', 13, 12);
INSERT INTO WatchMe.group_apply_log (group_log_id, apply_date, status, update_date, group_id, member_id) VALUES (54, '2022-08-19 00:18:06.871000', 1, '2022-08-19 00:19:39.793000', 5, 13);
INSERT INTO WatchMe.group_apply_log (group_log_id, apply_date, status, update_date, group_id, member_id) VALUES (55, '2022-08-19 00:18:11.312000', 1, '2022-08-19 00:39:16.085000', 11, 13);
INSERT INTO WatchMe.group_apply_log (group_log_id, apply_date, status, update_date, group_id, member_id) VALUES (56, '2022-08-19 00:18:18.855000', 1, null, 15, 8);
INSERT INTO WatchMe.group_apply_log (group_log_id, apply_date, status, update_date, group_id, member_id) VALUES (57, '2022-08-19 00:18:21.022000', 2, null, 7, 13);
INSERT INTO WatchMe.group_apply_log (group_log_id, apply_date, status, update_date, group_id, member_id) VALUES (58, '2022-08-19 00:18:26.783000', 1, '2022-08-19 00:19:15.912000', 13, 13);
INSERT INTO WatchMe.group_apply_log (group_log_id, apply_date, status, update_date, group_id, member_id) VALUES (59, '2022-08-19 00:18:34.735000', 1, '2022-08-19 00:19:31.106000', 15, 13);
INSERT INTO WatchMe.group_apply_log (group_log_id, apply_date, status, update_date, group_id, member_id) VALUES (60, '2022-08-19 00:18:39.747000', 0, null, 14, 13);
INSERT INTO WatchMe.group_apply_log (group_log_id, apply_date, status, update_date, group_id, member_id) VALUES (61, '2022-08-19 00:21:34.250000', 1, '2022-08-19 00:48:34.729000', 15, 4);
INSERT INTO WatchMe.group_apply_log (group_log_id, apply_date, status, update_date, group_id, member_id) VALUES (62, '2022-08-19 00:26:25.638000', 1, null, 16, 10);
INSERT INTO WatchMe.group_apply_log (group_log_id, apply_date, status, update_date, group_id, member_id) VALUES (63, '2022-08-19 00:26:42.504000', 1, '2022-08-19 00:26:48.557000', 16, 9);
INSERT INTO WatchMe.group_apply_log (group_log_id, apply_date, status, update_date, group_id, member_id) VALUES (64, '2022-08-19 00:32:59.300000', 0, null, 1, 11);
INSERT INTO WatchMe.group_apply_log (group_log_id, apply_date, status, update_date, group_id, member_id) VALUES (65, '2022-08-19 00:33:24.697000', 1, '2022-08-19 00:35:18.047000', 2, 11);
INSERT INTO WatchMe.group_apply_log (group_log_id, apply_date, status, update_date, group_id, member_id) VALUES (66, '2022-08-19 00:39:03.749000', 0, null, 12, 5);
INSERT INTO WatchMe.group_apply_log (group_log_id, apply_date, status, update_date, group_id, member_id) VALUES (67, '2022-08-19 01:48:10.448000', 1, null, 17, 68);
INSERT INTO WatchMe.group_apply_log (group_log_id, apply_date, status, update_date, group_id, member_id) VALUES (68, '2022-08-19 01:50:37.156000', 1, '2022-08-19 01:51:00.280000', 4, 68);
INSERT INTO WatchMe.group_apply_log (group_log_id, apply_date, status, update_date, group_id, member_id) VALUES (69, '2022-08-19 10:09:04.065000', 1, null, 18, 13);
