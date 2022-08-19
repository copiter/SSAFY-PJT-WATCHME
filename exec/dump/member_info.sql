create table member_info
(
    member_id        bigint       not null
        primary key,
    birth            datetime(6)  null,
    description      varchar(255) null,
    gender           varchar(255) null,
    img_link         varchar(255) null,
    name             varchar(255) null,
    point            int          not null,
    score            int          not null,
    study_time       int          null,
    study_time_day   int          null,
    study_time_month int          null,
    study_time_week  int          null,
    constraint FKbptteae7bfaa7obi1ohs523m0
        foreign key (member_id) references member (member_id)
);

INSERT INTO WatchMe.member_info (member_id, birth, description, gender, img_link, name, point, score, study_time, study_time_day, study_time_month, study_time_week) VALUES (2, '1996-10-05 09:00:00', null, 'M', 'https://popoimages.s3.ap-northeast-2.amazonaws.com/WatchMe/P1.jpg', '김공부', 90000, 0, 0, 0, 0, 0);
INSERT INTO WatchMe.member_info (member_id, birth, description, gender, img_link, name, point, score, study_time, study_time_day, study_time_month, study_time_week) VALUES (3, '1996-10-05 09:00:00', null, 'M', 'https://popoimages.s3.ap-northeast-2.amazonaws.com/WatchMe/P2.jpg', '박공부', 0, 0, 0, 75, 75, 75);
INSERT INTO WatchMe.member_info (member_id, birth, description, gender, img_link, name, point, score, study_time, study_time_day, study_time_month, study_time_week) VALUES (4, '1996-10-05 09:00:00', null, 'M', 'https://popoimages.s3.ap-northeast-2.amazonaws.com/WatchMe/P3.jpg', '최공부', 0, 0, 0, 0, 0, 0);
INSERT INTO WatchMe.member_info (member_id, birth, description, gender, img_link, name, point, score, study_time, study_time_day, study_time_month, study_time_week) VALUES (5, '1996-10-05 09:00:00', null, 'M', 'https://popoimages.s3.ap-northeast-2.amazonaws.com/WatchMe/P4.jpg', '이공부', 0, 0, 0, 0, 0, 0);
INSERT INTO WatchMe.member_info (member_id, birth, description, gender, img_link, name, point, score, study_time, study_time_day, study_time_month, study_time_week) VALUES (6, '1996-10-05 09:00:00', null, 'M', 'https://popoimages.s3.ap-northeast-2.amazonaws.com/WatchMe/P5.jpg', '홍공부', 0, 0, 0, 0, 0, 0);
INSERT INTO WatchMe.member_info (member_id, birth, description, gender, img_link, name, point, score, study_time, study_time_day, study_time_month, study_time_week) VALUES (7, '1996-10-05 09:00:00', null, 'M', 'https://popoimages.s3.ap-northeast-2.amazonaws.com/WatchMe/P6.jpg', '홍석인', 0, 0, 0, 4, 4, 4);
INSERT INTO WatchMe.member_info (member_id, birth, description, gender, img_link, name, point, score, study_time, study_time_day, study_time_month, study_time_week) VALUES (8, '1996-10-05 09:00:00', null, 'M', 'https://popoimages.s3.ap-northeast-2.amazonaws.com/WatchMe/P7.jpg', '고동훤', 0, 0, 0, 0, 0, 0);
INSERT INTO WatchMe.member_info (member_id, birth, description, gender, img_link, name, point, score, study_time, study_time_day, study_time_month, study_time_week) VALUES (9, '1996-10-05 09:00:00', null, 'M', 'https://popoimages.s3.ap-northeast-2.amazonaws.com/WatchMe/P8.jpg', '박철민', 0, 0, 0, 1, 1, 1);
INSERT INTO WatchMe.member_info (member_id, birth, description, gender, img_link, name, point, score, study_time, study_time_day, study_time_month, study_time_week) VALUES (10, '1996-10-05 09:00:00', null, 'M', 'https://popoimages.s3.ap-northeast-2.amazonaws.com/WatchMe/P9.jpg', '장태경', 0, 0, 0, 0, 0, 0);
INSERT INTO WatchMe.member_info (member_id, birth, description, gender, img_link, name, point, score, study_time, study_time_day, study_time_month, study_time_week) VALUES (11, '1996-10-05 09:00:00', null, 'M', 'https://popoimages.s3.ap-northeast-2.amazonaws.com/WatchMe/P10.jpg', '김영훈', 0, 0, 0, 0, 0, 0);
INSERT INTO WatchMe.member_info (member_id, birth, description, gender, img_link, name, point, score, study_time, study_time_day, study_time_month, study_time_week) VALUES (12, '1996-10-05 09:00:00', null, 'M', 'https://popoimages.s3.ap-northeast-2.amazonaws.com/WatchMe/P11.jpg', '김싸피', 70000, 0, 0, 0, 0, 0);
INSERT INTO WatchMe.member_info (member_id, birth, description, gender, img_link, name, point, score, study_time, study_time_day, study_time_month, study_time_week) VALUES (13, '1996-10-05 09:00:00', null, 'M', 'https://popoimages.s3.ap-northeast-2.amazonaws.com/WatchMe/P12.jpg', '이싸피', 30000, 0, 0, 2, 2, 2);
INSERT INTO WatchMe.member_info (member_id, birth, description, gender, img_link, name, point, score, study_time, study_time_day, study_time_month, study_time_week) VALUES (14, '1996-10-05 09:00:00', null, 'F', 'https://popoimages.s3.ap-northeast-2.amazonaws.com/WatchMe/P13.jpg', '최싸피', 40000, 0, 0, 2, 2, 2);
INSERT INTO WatchMe.member_info (member_id, birth, description, gender, img_link, name, point, score, study_time, study_time_day, study_time_month, study_time_week) VALUES (15, '1996-10-05 09:00:00', null, 'F', 'https://popoimages.s3.ap-northeast-2.amazonaws.com/WatchMe/P14.jpg', '홍싸피', 50000, 0, 0, 0, 0, 0);
INSERT INTO WatchMe.member_info (member_id, birth, description, gender, img_link, name, point, score, study_time, study_time_day, study_time_month, study_time_week) VALUES (16, '1996-10-05 09:00:00', null, 'F', 'https://popoimages.s3.ap-northeast-2.amazonaws.com/WatchMe/P15.jpg', '구싸피', 35000, 0, 0, 60, 60, 60);
INSERT INTO WatchMe.member_info (member_id, birth, description, gender, img_link, name, point, score, study_time, study_time_day, study_time_month, study_time_week) VALUES (17, '1996-10-05 09:00:00', null, 'F', 'https://popoimages.s3.ap-northeast-2.amazonaws.com/WatchMe/P16.jpg', '박싸피', 0, 0, 0, 0, 0, 0);
INSERT INTO WatchMe.member_info (member_id, birth, description, gender, img_link, name, point, score, study_time, study_time_day, study_time_month, study_time_week) VALUES (18, '1996-10-05 09:00:00', null, 'F', 'https://popoimages.s3.ap-northeast-2.amazonaws.com/WatchMe/P17.jpg', '주싸피', 0, 0, 0, 0, 0, 0);
INSERT INTO WatchMe.member_info (member_id, birth, description, gender, img_link, name, point, score, study_time, study_time_day, study_time_month, study_time_week) VALUES (19, '1996-10-05 09:00:00', null, 'F', 'https://popoimages.s3.ap-northeast-2.amazonaws.com/WatchMe/P18.jpg', '강싸피', 0, 0, 0, 0, 0, 0);
INSERT INTO WatchMe.member_info (member_id, birth, description, gender, img_link, name, point, score, study_time, study_time_day, study_time_month, study_time_week) VALUES (20, '1996-10-05 09:00:00', null, 'F', 'https://popoimages.s3.ap-northeast-2.amazonaws.com/WatchMe/P19.jpg', '양싸피', 0, 0, 0, 1, 1, 1);
INSERT INTO WatchMe.member_info (member_id, birth, description, gender, img_link, name, point, score, study_time, study_time_day, study_time_month, study_time_week) VALUES (21, '1996-10-05 09:00:00', null, 'F', 'https://popoimages.s3.ap-northeast-2.amazonaws.com/WatchMe/P20.jpg', '진싸피', 0, 0, 0, 0, 0, 0);
INSERT INTO WatchMe.member_info (member_id, birth, description, gender, img_link, name, point, score, study_time, study_time_day, study_time_month, study_time_week) VALUES (65, '1988-08-08 10:00:00', null, 'M', 'https://popoimages.s3.ap-northeast-2.amazonaws.com/Watchme/4524ac82-4107-469a-ba8a-bfd20b256c68morning7.jpg', '이상현', 90000, 0, 0, 738, 3258, 2162);
INSERT INTO WatchMe.member_info (member_id, birth, description, gender, img_link, name, point, score, study_time, study_time_day, study_time_month, study_time_week) VALUES (66, '2002-02-02 09:00:00', null, 'F', 'https://popoimages.s3.ap-northeast-2.amazonaws.com/Watchme/c7e092c2-f686-4187-9cd0-55620b768727morning6.jpg', '당현아', 40000, 0, 0, 700, 3220, 2124);
INSERT INTO WatchMe.member_info (member_id, birth, description, gender, img_link, name, point, score, study_time, study_time_day, study_time_month, study_time_week) VALUES (67, '2002-02-02 09:00:00', null, 'F', 'https://popoimages.s3.ap-northeast-2.amazonaws.com/Watchme/cce28049-79f7-4f27-af5b-4eb8f3c8093cmorning6.jpg', '장효정', 40000, 0, 0, 521, 3041, 1945);
INSERT INTO WatchMe.member_info (member_id, birth, description, gender, img_link, name, point, score, study_time, study_time_day, study_time_month, study_time_week) VALUES (68, '1995-07-20 09:00:00', null, 'ND', 'http://k.kakaocdn.net/dn/btt0r4/btroLDHZjMX/kzwADF4Py7qTRacjnafvlK/img_110x110.jpg', '박철민', 24000, 0, 0, 686, 3206, 2110);
