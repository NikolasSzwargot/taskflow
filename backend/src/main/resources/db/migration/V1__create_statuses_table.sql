create table if not exists statuses (
    id uuid primary key,
    name varchar(50) not null,
    position int not null,
    created_at timestamp not null default now()
    );

create unique index if not exists ux_statuses_position on statuses(position);
create unique index if not exists ux_statuses_name on statuses(name);