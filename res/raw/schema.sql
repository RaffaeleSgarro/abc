CREATE TABLE "slot" (
	"_id" INTEGER PRIMARY KEY NOT NULL,
	"subject_id" INT DEFAULT -1,
	"start" TEXT,
	"end" TEXT,
	"day" INTEGER NOT NULL,
	"ord" INTEGER NOT NULL,
	"display_text" TEXT,
	"place" TEXT,
	"teacher" TEXT,
	
	FOREIGN KEY ("subject_id") REFERENCES "subject" ("_id") ON DELETE SET DEFAULT ON UPDATE CASCADE,
	UNIQUE ("day", "ord")
);

CREATE TABLE "subject" (
	"_id" INTEGER PRIMARY KEY NOT NULL,
	"name" TEXT UNIQUE,
	"name_short" TEXT,
	"default_teacher" TEXT,
	"default_place" TEXT
);

CREATE TABLE "homework" (
	"_id" INTEGER PRIMARY KEY NOT NULL,
	"subject_id" INTEGER NOT NULL,
	"description" TEXT,
	"due" TEXT,
	
	FOREIGN KEY ("subject_id") REFERENCES "subject"("_id")
);

CREATE TABLE "grade" (
	"_id" INTEGER PRIMARY KEY NOT NULL,
	"subject_id" INTEGER NOT NULL,
	"description" TEXT,
	"score" TEXT,
	"date" TEXT,
	
	FOREIGN KEY ("subject_id") REFERENCES "subject"("_id")
);

