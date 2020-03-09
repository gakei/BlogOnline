create table blog
(
	id bigint auto_increment,
	user_Id bigint null,
	title varchar(100) null,
	description varchar(100) null,
	content text null,
	created_at datetime null,
	updated_at datetime null,
	constraint blog_pk
		primary key (id)
);

