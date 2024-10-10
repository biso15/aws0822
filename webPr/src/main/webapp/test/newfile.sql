SET SESSION FOREIGN_KEY_CHECKS=0;

/* Drop Tables */

DROP TABLE IF EXISTS BOARD;
DROP TABLE IF EXISTS MEMBER;
DROP TABLE IF EXISTS SAMPLE;




/* Create Tables */

CREATE TABLE BOARD
(
	-- 인덱스
	didx int NOT NULL AUTO_INCREMENT COMMENT '인덱스',
	-- 제목
	subject varchar(100) NOT NULL COMMENT '제목',
	-- 내용
	contents text NOT NULL COMMENT '내용',
	-- 작성자 이름
	writer varchar(80) NOT NULL COMMENT '작성자 이름',
	-- 추천수
	recom int DEFAULT 0 COMMENT '추천수',
	-- 조회수
	viewcnt int DEFAULT 0 COMMENT '조회수',
	-- 첨부파일
	filename varchar(100) COMMENT '첨부파일',
	-- 작성일
	writeday datetime DEFAULT NOW(), SYSDATE() NOT NULL COMMENT '작성일',
	-- 삭제여부
	delyn char(1) DEFAULT 'N' COMMENT '삭제여부',
	-- 작성자 ip
	ip varchar(30) COMMENT '작성자 ip',
	-- 인덱스
	midx int NOT NULL COMMENT '인덱스',
	PRIMARY KEY (didx)
);


CREATE TABLE MEMBER
(
	-- 인덱스
	midx int NOT NULL AUTO_INCREMENT COMMENT '인덱스',
	-- 아이디
	memberid varchar(100) NOT NULL COMMENT '아이디',
	-- 비밀번호
	memverpwd varchar(100) NOT NULL COMMENT '비밀번호',
	-- 이름
	membername varchar(100) NOT NULL COMMENT '이름',
	-- 성별
	membergender char(1) NOT NULL COMMENT '성별',
	-- 생일
	memberbirth varchar(10) COMMENT '생일',
	-- 주소
	memberaddr varchar(10) COMMENT '주소',
	-- 전화번호
	memberphone varchar(20) COMMENT '전화번호',
	-- 이메일
	memberemail varchar(100) COMMENT '이메일',
	-- 취미
	memberhoby varchar(100) COMMENT '취미',
	-- 탈퇴여부
	delyn char(1) DEFAULT 'N' NOT NULL COMMENT '탈퇴여부',
	-- 가입일
	writeday datetime DEFAULT NOW(), SYSDATE() NOT NULL COMMENT '가입일',
	-- ip
	memberip varchar(20) COMMENT 'ip',
	PRIMARY KEY (midx)
);


CREATE TABLE SAMPLE
(
	sampleidx int NOT NULL AUTO_INCREMENT,
	-- 이름
	name varchar(50) COMMENT '이름',
	-- 등록일
	writeday datetime DEFAULT NOW(), SYSDATE() COMMENT '등록일',
	-- 삭제여부
	delyn char(1) DEFAULT 'N' COMMENT '삭제여부',
	PRIMARY KEY (sampleidx)
);



