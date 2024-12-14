create table category (
  id bigserial primary key,
  user_id bigint references users on delete cascade,
  name text,
  color text
);

alter table links add category_id bigint references category;
