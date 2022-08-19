create table member
(
    member_id     bigint auto_increment
        primary key,
    email         varchar(255) not null,
    nick_name     varchar(255) not null,
    provider_type varchar(255) null,
    pwd           varchar(255) not null,
    role          varchar(255) null,
    status        varchar(255) null
);

INSERT INTO WatchMe.member (member_id, email, nick_name, provider_type, pwd, role, status) VALUES (2, 'p1@naver.com', '춘식이입니당', 'EMAIL', '$2a$10$5ql1D6Ei4fLG3B1zoT494.2T2KtoUc5qAlO6fXtIa4LskNiDzhewu', 'MEMBER', 'YES');
INSERT INTO WatchMe.member (member_id, email, nick_name, provider_type, pwd, role, status) VALUES (3, 'p2@naver.com', 'DONGHAR', 'EMAIL', '$2a$10$5ql1D6Ei4fLG3B1zoT494.2T2KtoUc5qAlO6fXtIa4LskNiDzhewu', 'MEMBER', 'YES');
INSERT INTO WatchMe.member (member_id, email, nick_name, provider_type, pwd, role, status) VALUES (4, 'p3@naver.com', '수능400점', 'EMAIL', '$2a$10$5ql1D6Ei4fLG3B1zoT494.2T2KtoUc5qAlO6fXtIa4LskNiDzhewu', 'MEMBER', 'YES');
INSERT INTO WatchMe.member (member_id, email, nick_name, provider_type, pwd, role, status) VALUES (5, 'p4@naver.com', 'biiou', 'EMAIL', '$2a$10$5ql1D6Ei4fLG3B1zoT494.2T2KtoUc5qAlO6fXtIa4LskNiDzhewu', 'MEMBER', 'YES');
INSERT INTO WatchMe.member (member_id, email, nick_name, provider_type, pwd, role, status) VALUES (6, 'p5@naver.com', 'ardor', 'EMAIL', '$2a$10$5ql1D6Ei4fLG3B1zoT494.2T2KtoUc5qAlO6fXtIa4LskNiDzhewu', 'MEMBER', 'YES');
INSERT INTO WatchMe.member (member_id, email, nick_name, provider_type, pwd, role, status) VALUES (7, 'p6@naver.com', '석인짱123', 'EMAIL', '$2a$10$5ql1D6Ei4fLG3B1zoT494.2T2KtoUc5qAlO6fXtIa4LskNiDzhewu', 'MEMBER', 'YES');
INSERT INTO WatchMe.member (member_id, email, nick_name, provider_type, pwd, role, status) VALUES (8, 'p7@naver.com', '동훤짱123', 'EMAIL', '$2a$10$5ql1D6Ei4fLG3B1zoT494.2T2KtoUc5qAlO6fXtIa4LskNiDzhewu', 'MEMBER', 'YES');
INSERT INTO WatchMe.member (member_id, email, nick_name, provider_type, pwd, role, status) VALUES (9, 'p8@naver.com', '철민짱123', 'EMAIL', '$2a$10$5ql1D6Ei4fLG3B1zoT494.2T2KtoUc5qAlO6fXtIa4LskNiDzhewu', 'MEMBER', 'YES');
INSERT INTO WatchMe.member (member_id, email, nick_name, provider_type, pwd, role, status) VALUES (10, 'p9@naver.com', '태경짱123', 'EMAIL', '$2a$10$5ql1D6Ei4fLG3B1zoT494.2T2KtoUc5qAlO6fXtIa4LskNiDzhewu', 'MEMBER', 'YES');
INSERT INTO WatchMe.member (member_id, email, nick_name, provider_type, pwd, role, status) VALUES (11, 'p10@naver.com', '영훈짱123', 'EMAIL', '$2a$10$5ql1D6Ei4fLG3B1zoT494.2T2KtoUc5qAlO6fXtIa4LskNiDzhewu', 'MEMBER', 'YES');
INSERT INTO WatchMe.member (member_id, email, nick_name, provider_type, pwd, role, status) VALUES (12, 'p11@naver.com', '그냥아무거나', 'EMAIL', '$2a$10$5ql1D6Ei4fLG3B1zoT494.2T2KtoUc5qAlO6fXtIa4LskNiDzhewu', 'MEMBER', 'YES');
INSERT INTO WatchMe.member (member_id, email, nick_name, provider_type, pwd, role, status) VALUES (13, 'p12@naver.com', '대충', 'EMAIL', '$2a$10$5ql1D6Ei4fLG3B1zoT494.2T2KtoUc5qAlO6fXtIa4LskNiDzhewu', 'MEMBER', 'YES');
INSERT INTO WatchMe.member (member_id, email, nick_name, provider_type, pwd, role, status) VALUES (14, 'p13@naver.com', '지은이', 'EMAIL', '$2a$10$5ql1D6Ei4fLG3B1zoT494.2T2KtoUc5qAlO6fXtIa4LskNiDzhewu', 'MEMBER', 'YES');
INSERT INTO WatchMe.member (member_id, email, nick_name, provider_type, pwd, role, status) VALUES (15, 'p14@naver.com', '전문가1', 'EMAIL', '$2a$10$5ql1D6Ei4fLG3B1zoT494.2T2KtoUc5qAlO6fXtIa4LskNiDzhewu', 'MEMBER', 'YES');
INSERT INTO WatchMe.member (member_id, email, nick_name, provider_type, pwd, role, status) VALUES (16, 'p15@naver.com', 'asdf223', 'EMAIL', '$2a$10$5ql1D6Ei4fLG3B1zoT494.2T2KtoUc5qAlO6fXtIa4LskNiDzhewu', 'MEMBER', 'YES');
INSERT INTO WatchMe.member (member_id, email, nick_name, provider_type, pwd, role, status) VALUES (17, 'p16@naver.com', '샛별', 'EMAIL', '$2a$10$5ql1D6Ei4fLG3B1zoT494.2T2KtoUc5qAlO6fXtIa4LskNiDzhewu', 'MEMBER', 'YES');
INSERT INTO WatchMe.member (member_id, email, nick_name, provider_type, pwd, role, status) VALUES (18, 'p17@naver.com', '별똥별', 'EMAIL', '$2a$10$5ql1D6Ei4fLG3B1zoT494.2T2KtoUc5qAlO6fXtIa4LskNiDzhewu', 'MEMBER', 'YES');
INSERT INTO WatchMe.member (member_id, email, nick_name, provider_type, pwd, role, status) VALUES (19, 'p18@naver.com', 'elinnile', 'EMAIL', '$2a$10$5ql1D6Ei4fLG3B1zoT494.2T2KtoUc5qAlO6fXtIa4LskNiDzhewu', 'MEMBER', 'YES');
INSERT INTO WatchMe.member (member_id, email, nick_name, provider_type, pwd, role, status) VALUES (20, 'p19@naver.com', 'caksdqpw', 'EMAIL', '$2a$10$5ql1D6Ei4fLG3B1zoT494.2T2KtoUc5qAlO6fXtIa4LskNiDzhewu', 'MEMBER', 'YES');
INSERT INTO WatchMe.member (member_id, email, nick_name, provider_type, pwd, role, status) VALUES (21, 'p20@naver.com', '안녕', 'EMAIL', '$2a$10$5ql1D6Ei4fLG3B1zoT494.2T2KtoUc5qAlO6fXtIa4LskNiDzhewu', 'MEMBER', 'YES');
INSERT INTO WatchMe.member (member_id, email, nick_name, provider_type, pwd, role, status) VALUES (65, 'megahawk99@gmail.com', '컨님', 'EMAIL', '$2a$10$9GSecK4WpB7xR5e58wsnC.ZqJzetBOXF.UiAU6feypNlkPicB1Q1y', 'MEMBER', 'YES');
INSERT INTO WatchMe.member (member_id, email, nick_name, provider_type, pwd, role, status) VALUES (66, 'dha@naver.com', '당코치님', 'EMAIL', '$2a$10$CRL6NUw6S5sLLhDmYsOvHegDuYab8.K86UNeHcw13BXpU0P1zYKLe', 'MEMBER', 'YES');
INSERT INTO WatchMe.member (member_id, email, nick_name, provider_type, pwd, role, status) VALUES (67, 'jhj@naver.com', '장코치님', 'EMAIL', '$2a$10$/yJ38piMhL.tznk9wEksiuMj4f36oS67FACzAt4YkLl9v1yVI7KsS', 'MEMBER', 'YES');
INSERT INTO WatchMe.member (member_id, email, nick_name, provider_type, pwd, role, status) VALUES (68, 'pcm0720@gmail.com', '박박철민', 'KAKAO', '$2a$10$5ql1D6Ei4fLG3B1zoT494.2T2KtoUc5qAlO6fXtIa4LskNiDzhewu', 'MEMBER', 'YES');
