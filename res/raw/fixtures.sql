INSERT INTO subject (_id, name, name_short)
VALUES (1, 'Fundamentals of computer architectures', 'ARCHS'),
VALUES (2, 'Calculus and vectors', 'CALC'),
VALUES (3, 'Mathematical Techniques for Computer Science', 'TECH'),
VALUES (4, 'Fundamentals of computer Engineering', 'ENGI');

INSERT INTO slot (subject_id, row, column)
VALUES (1, 0, 0),
VALUES (1, 1, 0),
VALUES (2, 1, 1),
VALUES (2, 2, 3),
VALUES (2, 4, 3),
VALUES (4, 2, 4),
VALUES (3, 5, 3),
VALUES (4, 3, 2),
VALUES (4, 3, 0),
VALUES (3, 2, 5),
VALUES (3, 2, 4);

INSERT INTO homework (subject_id, description, due)
VALUES (1, 'Boring task #1', '2012-10-21'),
VALUES (1, 'Boring task #2', '2012-10-22'),
VALUES (1, 'Boring task #3', '2013-10-23'),
VALUES (1, 'Boring task #4', '2012-11-24'),
VALUES (1, 'Boring task #5', '2012-04-25'),
VALUES (1, 'Boring task #6', '2012-08-26'),
VALUES (1, 'Boring task #7', '2012-10-27');

INSERT INTO "test" (subject_id, description, score, "date")
VALUES (1, 'Oral', '10+', '2008-10-13'),
VALUES (2, 'Written', 'Great', '2009-09-23'),
VALUES (3, 'Mixed', 'BBB-', '2012-05-26');

