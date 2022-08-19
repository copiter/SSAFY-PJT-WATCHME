create table groupss
(
    group_id   bigint auto_increment
        primary key,
    created_at datetime(6)  null,
    group_name varchar(45)  null,
    secret     int          null,
    status     varchar(255) null,
    updated_at datetime(6)  null,
    view       int          null,
    leader_id  bigint       null,
    constraint FKqckpkoongrgk517mssn6uun2d
        foreign key (leader_id) references member (member_id)
);

INSERT INTO WatchMe.groupss (group_id, created_at, group_name, secret, status, updated_at, view, leader_id) VALUES (1, '2022-08-18 23:51:25.481000', '서울 1반', 0, 'YES', '2022-08-18 23:51:25.481000', 0, 65);
INSERT INTO WatchMe.groupss (group_id, created_at, group_name, secret, status, updated_at, view, leader_id) VALUES (2, '2022-08-18 23:56:32.090000', '개빡공해봅시다', 0, 'YES', '2022-08-18 23:56:32.090000', 0, 2);
INSERT INTO WatchMe.groupss (group_id, created_at, group_name, secret, status, updated_at, view, leader_id) VALUES (3, '2022-08-18 23:56:38.097000', '서울대는 들어가야제~', 0, 'YES', '2022-08-18 23:56:38.097000', 0, 12);
INSERT INTO WatchMe.groupss (group_id, created_at, group_name, secret, status, updated_at, view, leader_id) VALUES (4, '2022-08-18 23:57:36.837000', '네카라쿠배 도전할 사람', 0, 'YES', '2022-08-18 23:57:36.837000', 0, 3);
INSERT INTO WatchMe.groupss (group_id, created_at, group_name, secret, status, updated_at, view, leader_id) VALUES (5, '2022-08-18 23:58:39.834000', '웃으면서 공부한다', 0, 'YES', '2022-08-18 23:58:39.834000', 0, 4);
INSERT INTO WatchMe.groupss (group_id, created_at, group_name, secret, status, updated_at, view, leader_id) VALUES (6, '2022-08-18 23:58:56.736000', '시작했으면 무는 뽑아야제~', 0, 'YES', '2022-08-18 23:58:56.736000', 0, 13);
INSERT INTO WatchMe.groupss (group_id, created_at, group_name, secret, status, updated_at, view, leader_id) VALUES (7, '2022-08-19 00:00:33.790000', '전한길 쌤 팬클럽', 0, 'YES', '2022-08-19 00:00:33.790000', 0, 14);
INSERT INTO WatchMe.groupss (group_id, created_at, group_name, secret, status, updated_at, view, leader_id) VALUES (8, '2022-08-19 00:01:24.617000', '공무원 시험합격은', 0, 'DELETE', '2022-08-19 00:03:08.031000', 0, 7);
INSERT INTO WatchMe.groupss (group_id, created_at, group_name, secret, status, updated_at, view, leader_id) VALUES (9, '2022-08-19 00:01:42.119000', '싸피 팀빌딩 너무 어렵다', 0, 'DELETE', '2022-08-19 00:02:03.686000', 0, 4);
INSERT INTO WatchMe.groupss (group_id, created_at, group_name, secret, status, updated_at, view, leader_id) VALUES (10, '2022-08-19 00:02:01.426000', 'PEET 준비생', 0, 'YES', '2022-08-19 00:02:01.426000', 0, 15);
INSERT INTO WatchMe.groupss (group_id, created_at, group_name, secret, status, updated_at, view, leader_id) VALUES (11, '2022-08-19 00:03:18.580000', '싸피팀빌딩 어렵다 너무', 0, 'YES', '2022-08-19 00:03:18.580000', 0, 5);
INSERT INTO WatchMe.groupss (group_id, created_at, group_name, secret, status, updated_at, view, leader_id) VALUES (12, '2022-08-19 00:03:30.972000', '공무원 시험합격은', 0, 'YES', '2022-08-19 00:03:30.972000', 0, 7);
INSERT INTO WatchMe.groupss (group_id, created_at, group_name, secret, status, updated_at, view, leader_id) VALUES (13, '2022-08-19 00:03:54.342000', '삼성전자 입사 스터디', 0, 'YES', '2022-08-19 00:03:54.342000', 0, 16);
INSERT INTO WatchMe.groupss (group_id, created_at, group_name, secret, status, updated_at, view, leader_id) VALUES (14, '2022-08-19 00:06:33.978000', '들어오면 성공한다', 0, 'YES', '2022-08-19 00:06:33.978000', 0, 6);
INSERT INTO WatchMe.groupss (group_id, created_at, group_name, secret, status, updated_at, view, leader_id) VALUES (15, '2022-08-19 00:18:18.853000', 'Jump To Python', 0, 'YES', '2022-08-19 00:18:18.853000', 0, 8);
INSERT INTO WatchMe.groupss (group_id, created_at, group_name, secret, status, updated_at, view, leader_id) VALUES (16, '2022-08-19 00:26:25.636000', '철민아 여기야', 0, 'YES', '2022-08-19 00:26:25.636000', 0, 10);
INSERT INTO WatchMe.groupss (group_id, created_at, group_name, secret, status, updated_at, view, leader_id) VALUES (17, '2022-08-19 01:48:10.446000', '철민이를 좋아하는 사람 다 모여라', 0, 'YES', '2022-08-19 01:48:10.446000', 0, 68);
INSERT INTO WatchMe.groupss (group_id, created_at, group_name, secret, status, updated_at, view, leader_id) VALUES (18, '2022-08-19 10:09:04.054000', '111', 0, 'DELETE', '2022-08-19 10:09:23.425000', 0, 13);
