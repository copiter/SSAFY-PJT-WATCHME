create table category
(
    ctg_id   int auto_increment
        primary key,
    ctg_name varchar(45) null
);

INSERT INTO WatchMe.category (ctg_id, ctg_name) VALUES (1, '수능');
INSERT INTO WatchMe.category (ctg_id, ctg_name) VALUES (2, '공무원');
INSERT INTO WatchMe.category (ctg_id, ctg_name) VALUES (3, '취업');
INSERT INTO WatchMe.category (ctg_id, ctg_name) VALUES (4, '코딩');
INSERT INTO WatchMe.category (ctg_id, ctg_name) VALUES (5, '자격증');
INSERT INTO WatchMe.category (ctg_id, ctg_name) VALUES (6, '기타');
INSERT INTO WatchMe.category (ctg_id, ctg_name) VALUES (7, '스프린트');
