create table group_info
(
    group_id    bigint       not null
        primary key,
    curr_member int          null,
    description varchar(255) null,
    image_link  varchar(255) null,
    max_member  int          null,
    constraint FKkwb7udch67ojte434s2r67313
        foreign key (group_id) references groupss (group_id)
);

INSERT INTO WatchMe.group_info (group_id, curr_member, description, image_link, max_member) VALUES (1, 3, '서울 1반을 위한 공간', 'https://popoimages.s3.ap-northeast-2.amazonaws.com/Watchme/b55a27b8-c1c2-477f-826b-879848266632morning7.jpg', 25);
INSERT INTO WatchMe.group_info (group_id, curr_member, description, image_link, max_member) VALUES (2, 5, '#공무원 #취업 #수능 #아무나', 'https://popoimages.s3.ap-northeast-2.amazonaws.com/Watchme/0a47d2f7-46d2-467f-9702-65c010e614d1G3.jpg', 15);
INSERT INTO WatchMe.group_info (group_id, curr_member, description, image_link, max_member) VALUES (3, 7, '서울대 입학을 위한 수능 준비 ', 'https://popoimages.s3.ap-northeast-2.amazonaws.com/Watchme/caa2ee14-708c-442d-b113-68d7c68a9465%EC%84%9C%EC%9A%B8%EB%8C%80_%EB%A1%9C%EA%B3%A0.png', 7);
INSERT INTO WatchMe.group_info (group_id, curr_member, description, image_link, max_member) VALUES (4, 7, '네카라쿠배 도전할 사람', 'https://popoimages.s3.ap-northeast-2.amazonaws.com/Watchme/f842cda4-c0c9-4418-a8a5-46497f4217fdG7.png', 10);
INSERT INTO WatchMe.group_info (group_id, curr_member, description, image_link, max_member) VALUES (5, 3, '웃지마', 'https://popoimages.s3.ap-northeast-2.amazonaws.com/Watchme/1a4a2aba-cc3a-4035-a0b3-e082983bd83dG8.jpg', 20);
INSERT INTO WatchMe.group_info (group_id, curr_member, description, image_link, max_member) VALUES (6, 3, '마마무 덕질하면서 공부하실 분 모집', 'https://popoimages.s3.ap-northeast-2.amazonaws.com/Watchme/c348ce45-ca0d-4d92-bb1b-f5951adb167b%EB%AC%B4%EB%AC%B4.jfif', 5);
INSERT INTO WatchMe.group_info (group_id, curr_member, description, image_link, max_member) VALUES (7, 5, '한국사 빡공하실분 모집합니다! 니 그라면 안 돼!!', 'https://popoimages.s3.ap-northeast-2.amazonaws.com/Watchme/bf4a2643-86a2-4cac-9a3d-2083dacf9cd2%EB%8B%A4%EC%9A%B4%EB%A1%9C%EB%93%9C.jfif', 5);
INSERT INTO WatchMe.group_info (group_id, curr_member, description, image_link, max_member) VALUES (8, 1, '에듀', 'https://popoimages.s3.ap-northeast-2.amazonaws.com/Watchme/9800771b-20da-45ec-b97f-71519e5cfcbb%23QQKp_e.jpg', 22);
INSERT INTO WatchMe.group_info (group_id, curr_member, description, image_link, max_member) VALUES (9, 1, '싸피 팀빌딩 너무 어렵다', 'https://popoimages.s3.ap-northeast-2.amazonaws.com/Watchme/478fdae5-1e7a-4a2c-8196-f83569a66fe9sprint.jpg', 18);
INSERT INTO WatchMe.group_info (group_id, curr_member, description, image_link, max_member) VALUES (10, 3, '이 문제 못 풀면 들어옵시다', 'https://popoimages.s3.ap-northeast-2.amazonaws.com/Watchme/0e63428e-0518-428a-ab24-479426259f9bimg.png', 10);
INSERT INTO WatchMe.group_info (group_id, curr_member, description, image_link, max_member) VALUES (11, 5, '싸피팀빌딩 어렵다 너무', 'https://popoimages.s3.ap-northeast-2.amazonaws.com/Watchme/1e022ac1-8d74-46af-8bc0-9d88928dd829G5.jpg', 20);
INSERT INTO WatchMe.group_info (group_id, curr_member, description, image_link, max_member) VALUES (12, 3, '에듀', 'https://popoimages.s3.ap-northeast-2.amazonaws.com/Watchme/96bf7995-e63d-44a9-b578-a5acef8cc965%EC%A0%9C%EB%AA%A9%20%EC%97%86%EC%9D%8C.png', 12);
INSERT INTO WatchMe.group_info (group_id, curr_member, description, image_link, max_member) VALUES (13, 6, 'DS / IT / CS, 코테 같이 준비해봐요', 'https://popoimages.s3.ap-northeast-2.amazonaws.com/Watchme/4b2451aa-9589-4bea-9e2c-b9dc222d560dunnamed.jpg', 7);
INSERT INTO WatchMe.group_info (group_id, curr_member, description, image_link, max_member) VALUES (14, 1, '들어오면 성공한다고', 'https://popoimages.s3.ap-northeast-2.amazonaws.com/Watchme/12324761-f787-4b44-9e87-db5254753f39G2.jpg', 10);
INSERT INTO WatchMe.group_info (group_id, curr_member, description, image_link, max_member) VALUES (15, 3, '대학 파이썬 동아리 스터디방입니다', 'https://popoimages.s3.ap-northeast-2.amazonaws.com/Watchme/5dd0eac7-c5c7-41ef-a5e8-f1bfffddc29d%EC%A0%9C%EB%AA%A9%20%EC%97%86%EC%9D%8C.png', 6);
INSERT INTO WatchMe.group_info (group_id, curr_member, description, image_link, max_member) VALUES (16, 2, 'PPT 잘부탁해', 'https://popoimages.s3.ap-northeast-2.amazonaws.com/Watchme/f35f4aab-816f-4a20-938c-6673203a56d3PPT%EB%81%9D%EB%82%B4%EA%B8%B0.png', 2);
INSERT INTO WatchMe.group_info (group_id, curr_member, description, image_link, max_member) VALUES (17, 1, '철민이를 좋아하는 사람 다 모여라', 'https://popoimages.s3.ap-northeast-2.amazonaws.com/Watchme/c96ecdc2-c6a9-4c1b-852d-137202e03183P6.jpg', 20);
INSERT INTO WatchMe.group_info (group_id, curr_member, description, image_link, max_member) VALUES (18, 1, '111', 'https://popoimages.s3.ap-northeast-2.amazonaws.com/WatchMe/groups.jpg', 10);
