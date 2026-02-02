create table if not exists "tasks" (
    id uuid primary key,
    title varchar(200) not null,
    status_id uuid not null references  statuses(id) on delete restrict,
    position int not null,
    created_at timestamp not null default now(),
    updated_at timestamp not null default now()
);

create index if not exists ix_tasks__status_id on tasks(status_id);
create unique index if not exists ux_tasks_status_position on tasks(status_id, position);