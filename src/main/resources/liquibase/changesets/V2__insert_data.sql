insert into users (name, email, password)
values ('John Doe', 'johndoe@gmail.com', '$2a$10$Xl0yhvzLIaJCDdKBS0Lld.ksK7c2Zytg/ZKFdtIYYQUv8rUfvCR4W'),
       ('Mike Smith', 'mikesmith@yahoo.com', '$2a$10$fFLij9aYgaNCFPTL9WcA/uoCRukxnwf.vOQ8nrEEOskrCNmGsxY7m');

insert into events (name, date, available_attendees_count ,description, category)
values ('RENAISSANCE WORLD TOUR', '2024-01-29',700, 'The Renaissance World Tour was the ninth concert tour ' ||
                                                             'by American singer-songwriter Beyoncé. ' ||
                                                         'Her highest-grossing tour to date, ' ||
                                                         'it was announced on February 1, 2023... ', 'Concert' ),
       ('You’ve Got What It Takes, But It Will Take Everything You’ve Got', '2023-01-31', 300,
        'This quiz competition is a pursuit of technical knowledge and actually tests the students retention ' ||
        'and accumulation of knowledge towards application of concepts. It would be greatly helpful ' ||
        'in acknowledging their position on their journey to technical excellence. ',  'Game'),
       ('Present new book To Infinity and Beyond: A Journey of Cosmic Discovery ', '2024-02-23', 500,
        'No one can make the mysteries of the universe more comprehensible and fun than Neil deGrasse Tyson. ' ||
        'Drawing on mythology, history, and literature—alongside his trademark wit and charm—Tyson and ' ||
        'StarTalk senior producer Lindsey Nyx Walker bring planetary science ' ||
        'down to Earth and principles of astrophysics within reach.', 'Conference');

insert into users_events (user_id, event_id)
values (1, 1),
       (2, 3),
       (1, 2),
       (2, 2);

