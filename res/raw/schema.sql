CREATE TABLE "slot" (
	"_id" INTEGER PRIMARY KEY NOT NULL,
	"start" TEXT,
	"end" TEXT,
	"day" INTEGER NOT NULL,
	"ord" INTEGER NOT NULL,
	"display_text" TEXT,
	"place" TEXT,
	"subject_id" INT REFERENCES "subject" ("_id") ON DELETE SET NULL ON UPDATE CASCADE,
	"teacher_id" INT REFERENCES "teacher" ("_id") ON DELETE SET NULL ON UPDATE CASCADE,
	
	UNIQUE ("day", "ord")
);

CREATE TABLE "subject" (
	"_id" INTEGER PRIMARY KEY NOT NULL,
	"name" TEXT UNIQUE,
	"name_short" TEXT,
	"default_teacher_id" INT REFERENCES "teacher" ("_id") ON DELETE SET NULL ON UPDATE CASCADE,
	"default_place" TEXT
);

CREATE TABLE "homework" (
	"_id" INTEGER PRIMARY KEY NOT NULL,
	"subject_id" INTEGER NOT NULL REFERENCES "subject"("_id") ON DELETE CASCADE ON UPDATE CASCADE,
	"description" TEXT,
	"due" TEXT
);

CREATE TABLE "grade" (
	"_id" INTEGER PRIMARY KEY NOT NULL,
	"subject_id" INTEGER NOT NULL REFERENCES "subject"("_id") ON DELETE CASCADE ON UPDATE CASCADE,
	"description" TEXT,
	"score" TEXT,
	"date" TEXT
);

CREATE TABLE "teacher" (
	"_id" INTEGER PRIMARY KEY NOT NULL,
	"name" TEXT,
	"email" TEXT,
	"phone" TEXT,
	"notes" TEXT
);

