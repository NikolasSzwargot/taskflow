insert into statuses (id, name, position)
values
    (gen_random_uuid(), 'TODO', 1),
    (gen_random_uuid(), 'IN_PROGRESS', 2),
    (gen_random_uuid(), 'DONE', 3)
    on conflict (name) do nothing;
