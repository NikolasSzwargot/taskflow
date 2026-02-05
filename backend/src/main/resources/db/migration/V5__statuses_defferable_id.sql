alter table statuses
    drop constraint if exists ux_statuses_position;

drop index if exists ux_statuses_position;

alter table statuses
    add constraint ux_statuses_position
        unique (position)
            deferrable initially deferred;
