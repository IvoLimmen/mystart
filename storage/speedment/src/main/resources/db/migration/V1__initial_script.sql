drop table if exists ms_link;
drop table if exists ms_user;

create table ms_user (
  id serial primary key,
  name text,
  email text,
  password text
);

create table ms_link (
  id serial primary key,
  user_id integer references ms_user,

  description text,
  source text,
  title text,
  url text,
  labels text,
  private_network bool,
  last_visit timestamp,
  creation_date timestamp
);