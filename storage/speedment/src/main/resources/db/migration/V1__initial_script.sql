drop table if exists ms_visits;
drop table if exists ms_link;
drop table if exists ms_user;

create table ms_user (
  id bigserial primary key,

  name text,
  email text,
  password text,
  open_in_new_tab boolean
);

create table ms_link (
  id bigserial primary key,
  user_id bigint references ms_user,

  description text,
  source text,
  title text,
  url text,
  labels text,
  private_network bool,
  check_result text,
  last_check timestamp,
  last_visit timestamp,
  creation_date timestamp
);

create table ms_visits (
  id bigserial primary key,
  link_id bigint references ms_link,
  
  visit timestamp
);