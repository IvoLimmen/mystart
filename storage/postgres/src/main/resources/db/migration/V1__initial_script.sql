drop table if exists visits;
drop table if exists links;
drop table if exists users;

create table users (
  id bigserial primary key,

  name text,
  email text,
  password text,
  open_in_new_tab boolean,
  full_name text,
  avatar_filename text
);

create table links (
  id bigserial primary key,
  user_id bigint references users on delete cascade,

  description text,
  source text,
  title text,
  url text,
  labels text[],
  private_network bool,
  check_result text,
  last_check timestamp,
  last_visit timestamp,
  creation_date timestamp
);

create table visits (
  id bigserial primary key,
  link_id bigint references links on delete cascade,
  
  visit timestamp
);