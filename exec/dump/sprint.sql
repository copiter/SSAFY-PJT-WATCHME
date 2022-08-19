create table sprint
(
    sprint_id   bigint auto_increment
        primary key,
    sprint_name varchar(255) null,
    status      varchar(255) null,
    sum_point   int          null,
    group_id    bigint       null,
    room_id     bigint       null,
    constraint FKcafgt0l5xhc35gpmf2cov51du
        foreign key (room_id) references room (room_id),
    constraint FKlnhv8r6un04rvn92cpi8x6k5i
        foreign key (group_id) references groupss (group_id)
);

INSERT INTO WatchMe.sprint (sprint_id, sprint_name, status, sum_point, group_id, room_id) VALUES (1, '서울 1반을 위한 스프린트', 'ING', 0, 1, 1);
INSERT INTO WatchMe.sprint (sprint_id, sprint_name, status, sum_point, group_id, room_id) VALUES (2, 'We Run', 'ING', 0, 5, 2);
INSERT INTO WatchMe.sprint (sprint_id, sprint_name, status, sum_point, group_id, room_id) VALUES (3, '취업드가자', 'ING', 0, 11, 3);
INSERT INTO WatchMe.sprint (sprint_id, sprint_name, status, sum_point, group_id, room_id) VALUES (4, '하반기 목표로 달려봐요', 'ING', 0, 13, 4);
INSERT INTO WatchMe.sprint (sprint_id, sprint_name, status, sum_point, group_id, room_id) VALUES (5, '공무원, 공기업 노리시는분들 환영합니다', 'ING', 0, 12, 5);
INSERT INTO WatchMe.sprint (sprint_id, sprint_name, status, sum_point, group_id, room_id) VALUES (6, '진짜 스프린트 길게 가보자', 'ING', 0, 14, 6);
INSERT INTO WatchMe.sprint (sprint_id, sprint_name, status, sum_point, group_id, room_id) VALUES (7, '실은 PEET 는 없어졌어요', 'ING', 0, 10, 7);
INSERT INTO WatchMe.sprint (sprint_id, sprint_name, status, sum_point, group_id, room_id) VALUES (8, '춘식이의 8월 3주차 스프린트', 'ING', 0, 2, 8);
INSERT INTO WatchMe.sprint (sprint_id, sprint_name, status, sum_point, group_id, room_id) VALUES (9, '9급 2차까지', 'ING', 0, 7, 9);
INSERT INTO WatchMe.sprint (sprint_id, sprint_name, status, sum_point, group_id, room_id) VALUES (10, '백준 코테 20문제 푼다', 'NO', 0, 4, 10);
INSERT INTO WatchMe.sprint (sprint_id, sprint_name, status, sum_point, group_id, room_id) VALUES (11, '9월 모고 끝내부러', 'ING', 0, 3, 11);
INSERT INTO WatchMe.sprint (sprint_id, sprint_name, status, sum_point, group_id, room_id) VALUES (12, '마마무 다음 콘서트까지 같이 해봐용', 'ING', 0, 6, 12);
INSERT INTO WatchMe.sprint (sprint_id, sprint_name, status, sum_point, group_id, room_id) VALUES (13, 'Python 초급반', 'ING', 0, 15, 13);
INSERT INTO WatchMe.sprint (sprint_id, sprint_name, status, sum_point, group_id, room_id) VALUES (14, '내일까지만 힘내', 'ING', 0, 16, 14);
INSERT INTO WatchMe.sprint (sprint_id, sprint_name, status, sum_point, group_id, room_id) VALUES (15, '네카라쿠배 이번엔 당토까지', 'ING', 0, 4, 254);
INSERT INTO WatchMe.sprint (sprint_id, sprint_name, status, sum_point, group_id, room_id) VALUES (16, '카카오 하반기 CS면접 빡세게', 'YES', 0, 4, 255);
