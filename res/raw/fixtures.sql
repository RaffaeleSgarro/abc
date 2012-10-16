INSERT INTO subject (_id, name, name_short) VALUES (1, 'Fundamentals of computer architectures', 'ARCHS');

INSERT INTO subject (_id, name, name_short) VALUES (2, 'Calculus and vectors', 'CALC');

INSERT INTO subject (_id, name, name_short) VALUES (3, 'Mathematical Techniques for Computer Science', 'TECH');

INSERT INTO subject (_id, name, name_short) VALUES (4, 'Fundamentals of computer Engineering', 'ENGI');


INSERT INTO slot ("subject_id", "ord", "day", "display_text") VALUES (1, 1, 1, 'ARCHS');

INSERT INTO slot ("subject_id", "ord", "day", "display_text") VALUES (1, 2, 1, 'ARCHS');

INSERT INTO slot ("subject_id", "ord", "day", "display_text") VALUES (2, 2, 2, 'CALC');

INSERT INTO slot ("subject_id", "ord", "day", "display_text") VALUES (4, 3, 2, 'ENGI');

INSERT INTO slot ("subject_id", "ord", "day", "display_text") VALUES (2, 2, 3, 'CALC');

INSERT INTO slot ("subject_id", "ord", "day", "display_text") VALUES (2, 3, 3, 'CALC');

INSERT INTO slot ("subject_id", "ord", "day", "display_text") VALUES (2, 3, 4, 'CALC');

INSERT INTO slot ("subject_id", "ord", "day", "display_text") VALUES (2, 5, 4, 'CALC');

INSERT INTO slot ("subject_id", "ord", "day", "display_text") VALUES (4, 3, 5, 'ENGI');

INSERT INTO slot ("subject_id", "ord", "day", "display_text") VALUES (3, 6, 4, 'CALC');

INSERT INTO slot ("subject_id", "ord", "day", "display_text") VALUES (4, 4, 3, 'ENGI');

INSERT INTO slot ("subject_id", "ord", "day", "display_text") VALUES (4, 3, 1, 'ENGI');

INSERT INTO slot ("subject_id", "ord", "day", "display_text") VALUES (4, 4, 1, 'ENGI');

INSERT INTO slot ("subject_id", "ord", "day", "display_text") VALUES (3, 1, 5, 'CALC');

INSERT INTO slot ("subject_id", "ord", "day", "display_text") VALUES (3, 4, 5, 'CALC');

INSERT INTO slot ("subject_id", "ord", "day", "display_text") VALUES (4, 2, 4, 'ENGI');

INSERT INTO slot ("subject_id", "ord", "day", "display_text") VALUES (1, 2, 5, 'ARCHS');


INSERT INTO homework (subject_id, description, due) VALUES (1, 'Boring task #1', '2012-10-21');

INSERT INTO homework (subject_id, description, due) VALUES (1, 'Boring task #2', '2012-10-22');

INSERT INTO homework (subject_id, description, due) VALUES (1, 'Boring task #3', '2013-10-23');

INSERT INTO homework (subject_id, description, due) VALUES (1, 'Boring task #4', '2012-11-24');

INSERT INTO homework (subject_id, description, due) VALUES (1, 'Boring task #5', '2012-04-25');

INSERT INTO homework (subject_id, description, due) VALUES (1, 'Boring task #6', '2012-08-26');

INSERT INTO homework (subject_id, description, due) VALUES (1, 'Boring task #7', '2012-10-27');


INSERT INTO grade (subject_id, description, score, "date") VALUES (1, 'Oral', '10+', '2008-10-13');

INSERT INTO grade (subject_id, description, score, "date") VALUES (2, 'Oral', '10+', '2008-10-13');

INSERT INTO grade (subject_id, description, score, "date") VALUES (3, 'Oral', '10+', '2008-10-13');

INSERT INTO grade (subject_id, description, score, "date") VALUES (4, 'Oral', '10+', '2008-10-13');

INSERT INTO grade (subject_id, description, score, "date") VALUES (2, 'Written', 'Great', '2009-09-23');

INSERT INTO grade (subject_id, description, score, "date") VALUES (3, 'Mixed', 'BBB-', '2012-05-26');


