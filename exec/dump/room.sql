create table room
(
    room_id   bigint auto_increment
        primary key,
    mode      varchar(255) null,
    room_name varchar(45)  null,
    status    varchar(255) null,
    view      int          null,
    owner_id  bigint       null,
    room_ctg  int          null,
    constraint FKesostdj4wdhfxom08clfqxtcf
        foreign key (owner_id) references member (member_id),
    constraint FKggdrgqiajdi4xepevxcfm6mu
        foreign key (room_ctg) references category (ctg_id)
);

INSERT INTO WatchMe.room (room_id, mode, room_name, status, view, owner_id, room_ctg) VALUES (1, 'MODE2', '서울 1반을 위한 스프린트', 'SPR', 0, 65, 7);
INSERT INTO WatchMe.room (room_id, mode, room_name, status, view, owner_id, room_ctg) VALUES (2, 'MODE2', 'We Run', 'SPR', 0, 4, 7);
INSERT INTO WatchMe.room (room_id, mode, room_name, status, view, owner_id, room_ctg) VALUES (3, 'MODE2', '취업드가자', 'SPR', 0, 5, 7);
INSERT INTO WatchMe.room (room_id, mode, room_name, status, view, owner_id, room_ctg) VALUES (4, 'MODE2', '하반기 목표로 달려봐요', 'SPR', 0, 16, 7);
INSERT INTO WatchMe.room (room_id, mode, room_name, status, view, owner_id, room_ctg) VALUES (5, 'MODE4', '공무원, 공기업 노리시는분들 환영합니다', 'SPR', 0, 7, 7);
INSERT INTO WatchMe.room (room_id, mode, room_name, status, view, owner_id, room_ctg) VALUES (6, 'MODE3', '진짜 스프린트 길게 가보자', 'SPR', 0, 6, 7);
INSERT INTO WatchMe.room (room_id, mode, room_name, status, view, owner_id, room_ctg) VALUES (7, 'MODE3', '실은 PEET 는 없어졌어요', 'SPR', 0, 15, 7);
INSERT INTO WatchMe.room (room_id, mode, room_name, status, view, owner_id, room_ctg) VALUES (8, 'MODE2', '춘식이의 8월 3주차 스프린트', 'SPR', 0, 2, 7);
INSERT INTO WatchMe.room (room_id, mode, room_name, status, view, owner_id, room_ctg) VALUES (9, 'MODE2', '9급 2차까지', 'SPR', 0, 14, 7);
INSERT INTO WatchMe.room (room_id, mode, room_name, status, view, owner_id, room_ctg) VALUES (10, 'MODE2', '백준 코테 20문제 푼다', 'SPR', 0, 3, 7);
INSERT INTO WatchMe.room (room_id, mode, room_name, status, view, owner_id, room_ctg) VALUES (11, 'MODE4', '9월 모고 끝내부러', 'SPR', 0, 12, 7);
INSERT INTO WatchMe.room (room_id, mode, room_name, status, view, owner_id, room_ctg) VALUES (12, 'MODE1', '마마무 다음 콘서트까지 같이 해봐용', 'SPR', 0, 13, 7);
INSERT INTO WatchMe.room (room_id, mode, room_name, status, view, owner_id, room_ctg) VALUES (13, 'MODE2', 'Python 초급반', 'SPR', 0, 8, 7);
INSERT INTO WatchMe.room (room_id, mode, room_name, status, view, owner_id, room_ctg) VALUES (14, 'MODE3', '내일까지만 힘내', 'SPR', 0, 10, 7);
INSERT INTO WatchMe.room (room_id, mode, room_name, status, view, owner_id, room_ctg) VALUES (15, 'MODE3', '이번엔 붙어야겠지?', 'YES', 101, 15, 3);
INSERT INTO WatchMe.room (room_id, mode, room_name, status, view, owner_id, room_ctg) VALUES (16, 'MODE3', '공부가 제일 쉽다', 'YES', 10, 16, 6);
INSERT INTO WatchMe.room (room_id, mode, room_name, status, view, owner_id, room_ctg) VALUES (17, 'MODE3', '공무원 합격은?', 'YES', 17, 17, 2);
INSERT INTO WatchMe.room (room_id, mode, room_name, status, view, owner_id, room_ctg) VALUES (18, 'MODE3', '자격증 공부방', 'YES', 82, 18, 5);
INSERT INTO WatchMe.room (room_id, mode, room_name, status, view, owner_id, room_ctg) VALUES (19, 'MODE3', '이번엔 꼭 붙는다', 'YES', 38, 19, 3);
INSERT INTO WatchMe.room (room_id, mode, room_name, status, view, owner_id, room_ctg) VALUES (20, 'MODE3', 'N수하는 사람들', 'NO', 234, 20, 1);
INSERT INTO WatchMe.room (room_id, mode, room_name, status, view, owner_id, room_ctg) VALUES (21, 'MODE3', '의대 한번 가시죠', 'YES', 75, 21, 1);
INSERT INTO WatchMe.room (room_id, mode, room_name, status, view, owner_id, room_ctg) VALUES (47, 'MODE1', '헤이유', 'NO', 1, 2, 2);
INSERT INTO WatchMe.room (room_id, mode, room_name, status, view, owner_id, room_ctg) VALUES (216, 'MODE3', '이번엔 붙어야겠지?', 'NO', 101, 15, 3);
INSERT INTO WatchMe.room (room_id, mode, room_name, status, view, owner_id, room_ctg) VALUES (217, 'MODE3', '공부가 제일 쉽다', 'NO', 10, 16, 6);
INSERT INTO WatchMe.room (room_id, mode, room_name, status, view, owner_id, room_ctg) VALUES (218, 'MODE3', '공무원 합격은?', 'NO', 17, 17, 2);
INSERT INTO WatchMe.room (room_id, mode, room_name, status, view, owner_id, room_ctg) VALUES (219, 'MODE3', '자격증 공부방', 'NO', 56, 18, 5);
INSERT INTO WatchMe.room (room_id, mode, room_name, status, view, owner_id, room_ctg) VALUES (220, 'MODE3', '이번엔 꼭 붙는다', 'NO', 38, 19, 3);
INSERT INTO WatchMe.room (room_id, mode, room_name, status, view, owner_id, room_ctg) VALUES (221, 'MODE3', 'N수하는 사람들', 'NO', 234, 20, 1);
INSERT INTO WatchMe.room (room_id, mode, room_name, status, view, owner_id, room_ctg) VALUES (222, 'MODE3', '의대 한번 가시죠', 'NO', 57, 21, 1);
INSERT INTO WatchMe.room (room_id, mode, room_name, status, view, owner_id, room_ctg) VALUES (223, 'MODE1', '헤이유', 'NO', 1, 2, 2);
INSERT INTO WatchMe.room (room_id, mode, room_name, status, view, owner_id, room_ctg) VALUES (225, 'MODE2', 'We Run', 'NO', 0, 4, 7);
INSERT INTO WatchMe.room (room_id, mode, room_name, status, view, owner_id, room_ctg) VALUES (226, 'MODE3', '공무원 합격은?', 'NO', 17, 17, 2);
INSERT INTO WatchMe.room (room_id, mode, room_name, status, view, owner_id, room_ctg) VALUES (227, 'MODE3', '공무원 합격은?', 'NO', 17, 17, 2);
INSERT INTO WatchMe.room (room_id, mode, room_name, status, view, owner_id, room_ctg) VALUES (228, 'MODE4', '공무원, 공기업 노리시는분들 환영합니다', 'NO', 0, 7, 7);
INSERT INTO WatchMe.room (room_id, mode, room_name, status, view, owner_id, room_ctg) VALUES (229, 'MODE3', '공부가 제일 쉽다', 'NO', 10, 16, 6);
INSERT INTO WatchMe.room (room_id, mode, room_name, status, view, owner_id, room_ctg) VALUES (230, 'MODE3', '공부가 제일 쉽다', 'NO', 10, 16, 6);
INSERT INTO WatchMe.room (room_id, mode, room_name, status, view, owner_id, room_ctg) VALUES (231, 'MODE3', '내일까지만 힘내', 'NO', 0, 10, 7);
INSERT INTO WatchMe.room (room_id, mode, room_name, status, view, owner_id, room_ctg) VALUES (232, 'MODE1', '마마무 다음 콘서트까지 같이 해봐용', 'NO', 0, 13, 7);
INSERT INTO WatchMe.room (room_id, mode, room_name, status, view, owner_id, room_ctg) VALUES (233, 'MODE2', '백준 코테 20문제 푼다', 'NO', 0, 3, 7);
INSERT INTO WatchMe.room (room_id, mode, room_name, status, view, owner_id, room_ctg) VALUES (234, 'MODE2', '서울 1반을 위한 스프린트', 'NO', 0, 65, 7);
INSERT INTO WatchMe.room (room_id, mode, room_name, status, view, owner_id, room_ctg) VALUES (235, 'MODE3', '실은 PEET 는 없어졌어요', 'NO', 0, 15, 7);
INSERT INTO WatchMe.room (room_id, mode, room_name, status, view, owner_id, room_ctg) VALUES (236, 'MODE3', '의대 한번 가시죠', 'NO', 57, 21, 1);
INSERT INTO WatchMe.room (room_id, mode, room_name, status, view, owner_id, room_ctg) VALUES (237, 'MODE3', '의대 한번 가시죠', 'NO', 57, 21, 1);
INSERT INTO WatchMe.room (room_id, mode, room_name, status, view, owner_id, room_ctg) VALUES (238, 'MODE3', '이번엔 꼭 붙는다', 'NO', 38, 19, 3);
INSERT INTO WatchMe.room (room_id, mode, room_name, status, view, owner_id, room_ctg) VALUES (239, 'MODE3', '이번엔 꼭 붙는다', 'NO', 38, 19, 3);
INSERT INTO WatchMe.room (room_id, mode, room_name, status, view, owner_id, room_ctg) VALUES (240, 'MODE3', '이번엔 붙어야겠지?', 'NO', 101, 15, 3);
INSERT INTO WatchMe.room (room_id, mode, room_name, status, view, owner_id, room_ctg) VALUES (241, 'MODE3', '이번엔 붙어야겠지?', 'NO', 101, 15, 3);
INSERT INTO WatchMe.room (room_id, mode, room_name, status, view, owner_id, room_ctg) VALUES (250, 'MODE3', '가볍게 우영우 보면서 하실 분 모집', 'NO', 0, 12, 6);
INSERT INTO WatchMe.room (room_id, mode, room_name, status, view, owner_id, room_ctg) VALUES (251, 'MODE3', '같이 수능 공부해요', 'NO', 0, 12, 1);
INSERT INTO WatchMe.room (room_id, mode, room_name, status, view, owner_id, room_ctg) VALUES (252, 'MODE2', '의대생 모여라', 'NO', 0, 12, 6);
INSERT INTO WatchMe.room (room_id, mode, room_name, status, view, owner_id, room_ctg) VALUES (253, 'MODE4', '같이 공부해봐요!', 'NO', 0, 12, 2);
INSERT INTO WatchMe.room (room_id, mode, room_name, status, view, owner_id, room_ctg) VALUES (254, 'MODE2', '네카라쿠배 이번엔 당토까지', 'SPR', 0, 3, 7);
INSERT INTO WatchMe.room (room_id, mode, room_name, status, view, owner_id, room_ctg) VALUES (255, 'MODE2', '카카오 하반기 CS면접 빡세게', 'SPR', 0, 3, 7);
INSERT INTO WatchMe.room (room_id, mode, room_name, status, view, owner_id, room_ctg) VALUES (259, 'MODE4', '스마트폰 자리이탈', 'NO', 0, 68, 2);
INSERT INTO WatchMe.room (room_id, mode, room_name, status, view, owner_id, room_ctg) VALUES (260, 'MODE2', '스마트폰 자리이탈', 'NO', 0, 68, 4);
