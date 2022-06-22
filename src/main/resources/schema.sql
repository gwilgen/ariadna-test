create table if not exists event_sources (
	id int not null,
	name varchar2(255) not null,
	primary key (id)
);

create table if not exists events (
	id long not null auto_increment,
	source_id int,
	timestamp timestamp not null,
	value numeric(10,2) not null,
	primary key (id),
	foreign key (source_id) references event_sources(id) on delete cascade
);
