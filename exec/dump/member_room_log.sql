create table member_room_log
(
    room_log_id bigint auto_increment
        primary key,
    joined_at   datetime(6)  null,
    start_at    datetime(6)  null,
    status      varchar(255) null,
    study_time  int          null,
    member_id   bigint       null,
    room_id     bigint       null,
    constraint FKcjjv07bfxig249s7g9cxca12l
        foreign key (member_id) references member (member_id),
    constraint FKk4ar0tu4fxtk2tv1vbdisiuds
        foreign key (room_id) references room (room_id)
);

INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (1, '2022-08-19 03:11:34.142000', '2022-08-19 00:30:43.859000', 'YES', 158, 65, 1);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (2, '2022-08-19 00:36:27.321000', '2022-08-19 00:32:54.281000', 'YES', 121, 67, 1);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (3, '2022-08-19 00:33:01', '2022-08-19 00:33:15', 'YES', 120, 66, 1);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (4, '2022-08-19 00:35:42.204000', '2022-08-19 00:35:42.205000', 'YES', 0, 2, 11);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (5, '2022-08-19 02:14:11.011000', '2022-08-19 00:37:08.354000', 'YES', 75, 3, 10);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (6, '2022-08-19 00:38:02.738000', '2022-08-19 00:38:02.738000', 'YES', 0, 3, 8);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (7, '2022-08-19 00:42:36.712000', '2022-08-19 00:42:36.712000', 'YES', 1, 13, 12);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (8, '2022-08-19 00:49:38.580000', '2022-08-19 00:42:51.771000', 'YES', 4, 7, 5);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (9, '2022-08-19 00:47:46.558000', '2022-08-19 00:45:27.422000', 'YES', 1, 9, 14);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (10, '2022-08-19 01:10:48.618000', '2022-08-19 00:45:32.699000', 'YES', 1, 13, 2);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (11, '2022-08-19 00:47:49.905000', '2022-08-19 00:47:49.905000', 'YES', 0, 13, 4);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (12, '2022-08-19 00:48:37.429000', '2022-08-19 00:48:37.430000', 'YES', 0, 8, 13);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (13, '2022-08-19 00:50:32.726000', '2022-08-19 00:50:32.726000', 'YES', 0, 13, 13);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (14, '2022-08-19 00:51:09.207000', '2022-08-19 00:51:09.207000', 'YES', 2, 14, 9);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (15, '2022-08-19 00:54:13.293000', '2022-08-19 00:54:13.293000', 'YES', 0, 14, 7);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (16, '2022-08-19 00:54:44.414000', '2022-08-19 00:54:44.414000', 'YES', 0, 14, 4);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (17, '2022-08-19 00:55:17.732000', '2022-08-19 00:55:17.732000', 'YES', 0, 15, 7);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (18, '2022-08-19 00:55:45.057000', '2022-08-19 00:55:45.058000', 'YES', 0, 15, 11);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (19, '2022-08-19 00:56:36.827000', '2022-08-19 00:56:36.827000', 'YES', 0, 15, 4);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (20, '2022-08-19 00:56:47.778000', '2022-08-19 00:56:47.778000', 'YES', 0, 15, 5);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (21, '2022-08-19 00:57:32.046000', '2022-08-19 00:57:32.046000', 'YES', 0, 16, 4);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (22, '2022-08-19 00:57:48.199000', '2022-08-19 00:57:48.199000', 'YES', 0, 16, 11);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (23, '2022-08-19 02:15:34.089000', '2022-08-19 00:58:03.572000', 'YES', 60, 16, 10);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (24, '2022-08-19 00:58:32.277000', '2022-08-19 00:58:32.277000', 'YES', 0, 16, 7);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (25, '2022-08-01 01:27:20', '2022-08-01 01:27:20', 'YES', 53, 68, 216);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (26, '2022-08-02 01:27:23', '2022-08-02 01:27:23', 'YES', 51, 68, 217);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (27, '2022-08-03 01:27:32', '2022-08-03 01:27:32', 'YES', 56, 68, 218);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (28, '2022-08-04 01:27:36', '2022-08-04 01:27:36', 'YES', 70, 68, 219);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (29, '2022-08-05 01:27:39', '2022-08-05 01:27:39', 'YES', 80, 68, 220);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (30, '2022-08-06 01:27:43', '2022-08-06 01:27:43', 'YES', 80, 68, 221);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (31, '2022-08-07 01:27:47', '2022-08-07 01:27:47', 'YES', 56, 68, 222);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (32, '2022-08-08 01:27:50', '2022-08-08 01:27:50', 'YES', 120, 68, 223);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (33, '2022-08-09 01:27:54', '2022-08-09 01:27:54', 'YES', 130, 68, 225);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (34, '2022-08-10 01:27:58', '2022-08-10 01:27:58', 'YES', 180, 68, 226);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (35, '2022-08-11 01:28:04', '2022-08-11 01:28:04', 'YES', 220, 68, 227);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (36, '2022-08-12 01:28:07', '2022-08-12 01:28:07', 'YES', 255, 68, 228);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (37, '2022-08-13 01:28:13', '2022-08-13 01:28:13', 'YES', 350, 68, 229);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (38, '2022-08-14 01:28:18', '2022-08-14 01:28:18', 'YES', 299, 68, 230);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (39, '2022-08-15 01:28:21', '2022-08-15 01:28:21', 'YES', 180, 68, 231);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (40, '2022-08-16 01:28:25', '2022-08-16 01:28:25', 'YES', 200, 68, 232);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (41, '2022-08-17 01:28:29', '2022-08-17 01:28:29', 'YES', 140, 68, 233);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (42, '2022-08-18 01:28:32', '2022-08-18 01:28:32', 'YES', 180, 68, 234);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (43, '2022-08-19 01:28:39', '2022-08-19 01:28:39', 'YES', 220, 68, 235);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (44, '2022-08-20 01:28:41', '2022-08-20 01:28:41', 'YES', 180, 68, 236);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (45, '2022-08-19 01:33:20.167000', '2022-08-19 01:33:20.164000', 'YES', 0, 12, 250);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (46, '2022-08-19 01:35:15.354000', '2022-08-19 01:35:15.352000', 'YES', 0, 12, 251);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (47, '2022-08-19 01:36:57.824000', '2022-08-19 01:36:57.822000', 'YES', 0, 12, 252);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (48, '2022-08-01 01:27:20', '2022-08-01 01:27:20', 'YES', 53, 65, 216);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (49, '2022-08-02 01:27:23', '2022-08-02 01:27:23', 'YES', 51, 65, 217);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (50, '2022-08-03 01:27:32', '2022-08-03 01:27:32', 'YES', 56, 65, 218);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (51, '2022-08-04 01:27:36', '2022-08-04 01:27:36', 'YES', 70, 65, 219);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (52, '2022-08-05 01:27:39', '2022-08-05 01:27:39', 'YES', 80, 65, 220);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (53, '2022-08-06 01:27:43', '2022-08-06 01:27:43', 'YES', 80, 65, 221);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (54, '2022-08-07 01:27:47', '2022-08-07 01:27:47', 'YES', 56, 65, 222);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (55, '2022-08-08 01:27:50', '2022-08-08 01:27:50', 'YES', 120, 65, 223);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (56, '2022-08-09 01:27:54', '2022-08-09 01:27:54', 'YES', 130, 65, 225);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (57, '2022-08-10 01:27:58', '2022-08-10 01:27:58', 'YES', 180, 65, 226);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (58, '2022-08-11 01:28:04', '2022-08-11 01:28:04', 'YES', 220, 65, 227);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (59, '2022-08-12 01:28:07', '2022-08-12 01:28:07', 'YES', 255, 65, 228);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (60, '2022-08-13 01:28:13', '2022-08-13 01:28:13', 'YES', 350, 65, 229);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (61, '2022-08-14 01:28:18', '2022-08-14 01:28:18', 'YES', 299, 65, 230);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (62, '2022-08-15 01:28:21', '2022-08-15 01:28:21', 'YES', 180, 65, 231);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (63, '2022-08-16 01:28:25', '2022-08-16 01:28:25', 'YES', 200, 65, 232);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (64, '2022-08-17 01:28:29', '2022-08-17 01:28:29', 'YES', 140, 65, 233);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (65, '2022-08-18 01:28:32', '2022-08-18 01:28:32', 'YES', 180, 65, 234);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (66, '2022-08-19 01:28:39', '2022-08-19 01:28:39', 'YES', 220, 65, 235);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (67, '2022-08-20 01:28:39', '2022-08-20 01:28:41', 'YES', 180, 65, 236);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (68, '2022-08-01 01:27:20', '2022-08-01 01:27:20', 'YES', 53, 66, 216);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (69, '2022-08-02 01:27:23', '2022-08-02 01:27:23', 'YES', 51, 66, 217);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (70, '2022-08-03 01:27:32', '2022-08-03 01:27:32', 'YES', 56, 66, 218);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (71, '2022-08-04 01:27:36', '2022-08-04 01:27:36', 'YES', 70, 66, 219);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (72, '2022-08-05 01:27:39', '2022-08-05 01:27:39', 'YES', 80, 66, 220);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (73, '2022-08-06 01:27:43', '2022-08-06 01:27:43', 'YES', 80, 66, 221);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (74, '2022-08-07 01:27:47', '2022-08-07 01:27:47', 'YES', 56, 66, 222);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (75, '2022-08-08 01:27:50', '2022-08-08 01:27:50', 'YES', 120, 66, 223);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (76, '2022-08-09 01:27:54', '2022-08-09 01:27:54', 'YES', 130, 66, 225);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (77, '2022-08-10 01:27:58', '2022-08-10 01:27:58', 'YES', 180, 66, 226);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (78, '2022-08-11 01:28:04', '2022-08-11 01:28:04', 'YES', 220, 66, 227);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (79, '2022-08-12 01:28:07', '2022-08-12 01:28:07', 'YES', 255, 66, 228);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (80, '2022-08-13 01:28:13', '2022-08-13 01:28:13', 'YES', 350, 66, 229);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (81, '2022-08-14 01:28:18', '2022-08-14 01:28:18', 'YES', 299, 66, 230);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (82, '2022-08-15 01:28:21', '2022-08-15 01:28:21', 'YES', 180, 66, 231);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (83, '2022-08-16 01:28:25', '2022-08-16 01:28:25', 'YES', 200, 66, 232);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (84, '2022-08-17 01:28:29', '2022-08-17 01:28:29', 'YES', 140, 66, 233);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (85, '2022-08-18 01:28:32', '2022-08-18 01:28:32', 'YES', 180, 66, 234);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (86, '2022-08-19 01:28:39', '2022-08-19 01:28:39', 'YES', 220, 66, 235);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (87, '2022-08-20 01:28:39', '2022-08-20 01:28:41', 'YES', 180, 66, 236);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (88, '2022-08-01 01:27:20', '2022-08-01 01:27:20', 'YES', 53, 67, 216);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (89, '2022-08-02 01:27:23', '2022-08-02 01:27:23', 'YES', 51, 67, 217);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (90, '2022-08-03 01:27:32', '2022-08-03 01:27:32', 'YES', 56, 67, 218);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (91, '2022-08-04 01:27:36', '2022-08-04 01:27:36', 'YES', 70, 67, 219);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (92, '2022-08-05 01:27:39', '2022-08-05 01:27:39', 'YES', 80, 67, 220);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (93, '2022-08-06 01:27:43', '2022-08-06 01:27:43', 'YES', 80, 67, 221);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (94, '2022-08-07 01:27:47', '2022-08-07 01:27:47', 'YES', 56, 67, 222);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (95, '2022-08-08 01:27:50', '2022-08-08 01:27:50', 'YES', 120, 67, 223);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (96, '2022-08-09 01:27:54', '2022-08-09 01:27:54', 'YES', 130, 67, 225);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (97, '2022-08-10 01:27:58', '2022-08-10 01:27:58', 'YES', 180, 67, 226);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (98, '2022-08-11 01:28:04', '2022-08-11 01:28:04', 'YES', 220, 67, 227);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (99, '2022-08-12 01:28:07', '2022-08-12 01:28:07', 'YES', 255, 67, 228);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (100, '2022-08-13 01:28:13', '2022-08-13 01:28:13', 'YES', 350, 67, 229);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (101, '2022-08-14 01:28:18', '2022-08-14 01:28:18', 'YES', 299, 67, 230);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (102, '2022-08-15 01:28:21', '2022-08-15 01:28:21', 'YES', 180, 67, 231);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (103, '2022-08-16 01:28:25', '2022-08-16 01:28:25', 'YES', 200, 67, 232);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (104, '2022-08-17 01:28:29', '2022-08-17 01:28:29', 'YES', 140, 67, 233);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (105, '2022-08-18 01:28:32', '2022-08-18 01:28:32', 'YES', 180, 67, 234);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (106, '2022-08-19 01:28:39', '2022-08-19 01:28:39', 'YES', 220, 67, 235);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (108, '2022-08-19 01:40:27.634000', '2022-08-19 01:40:27.632000', 'YES', 0, 12, 253);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (109, '2022-08-19 02:13:09.440000', '2022-08-19 02:10:29.089000', 'YES', 92, 68, 10);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (110, '2022-08-19 10:08:37.997000', '2022-08-19 02:31:52.259000', 'YES', 0, 68, 254);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (111, '2022-08-19 02:56:51.744000', '2022-08-19 02:52:03.836000', 'YES', 1, 68, 21);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (112, '2022-08-19 03:10:50.300000', '2022-08-19 02:59:06.855000', 'YES', 0, 68, 18);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (113, '2022-08-19 03:04:19.306000', '2022-08-19 03:04:19.305000', 'YES', 6, 68, 259);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (114, '2022-08-19 03:11:57.246000', '2022-08-19 03:11:57.244000', 'YES', 7, 68, 260);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (115, '2022-08-19 03:14:07.153000', '2022-08-19 03:12:09.301000', 'YES', 0, 9, 21);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (116, '2022-08-19 03:26:12.424000', '2022-08-19 03:14:59.755000', 'DELETE', 0, 20, 21);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (117, '2022-08-19 03:51:14.274000', '2022-08-19 03:31:23.230000', 'DELETE', 1, 20, 18);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (118, '2022-08-19 06:50:37.753000', '2022-08-19 06:50:37.813000', 'YES', 0, 13, 18);
INSERT INTO WatchMe.member_room_log (room_log_id, joined_at, start_at, status, study_time, member_id, room_id) VALUES (119, '2022-08-19 09:16:37', '2022-08-19 09:16:37', 'YES', 0, 67, 18);
