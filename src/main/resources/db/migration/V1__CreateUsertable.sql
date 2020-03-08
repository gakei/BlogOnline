create table user
(
	id bigint auto_increment,
	username varchar(100) null,
	encrypted_password varchar(100) null,
	avatar varchar(100) null,
	create_at datetime null,
	update_at varchar(100) null,
	constraint user_pk
		primary key (id)
);

create unique index user_encrypted_password_uindex
	on user (encrypted_password);

create unique index user_username_uindex
	on user (username);


