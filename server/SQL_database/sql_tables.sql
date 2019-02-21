CREATE TABLE user_archive(
	user_id INT AUTO_INCREMENT,
	firstname VARCHAR(255) NOT NULL,
	lastname VARCHAR(255) NOT NULL,
	email VARCHAR(255) NOT NULL,
	password_hash VARCHAR(255) NOT NULL,
	account_verification_state INT DEFAULT 0,
	email_verification_state INT DEFAULT 0,
	PRIMARY KEY(user_id)
);

CREATE TABLE profile_archive(
	user_id INT,
	profilepicture_id INT,
	calendar_id INT,
	description TEXT,
	PRIMARY KEY(user_id)
);

CREATE TABLE schooltype_list(
	schooltype_id INT AUTO_INCREMENT,
	schooltype_name VARCHAR(255),
	PRIMARY KEY(schooltype_id)
);

CREATE TABLE school_list(
	school_id INT AUTO INCREMENT,
	schooltype_id INT NOT NULL,
       	schoolname VARCHAR(255) NOT NULL,
	schoolgrades INT,
	PRIMARY KEY(school_id),
	FOREIGN KEY(schooltype_id) REFERENCES schooltype_list(schooltype_id)
);	

CREATE TABLE school_conn(
	user_id INT NOT NULL,
	school_id INT NOT NULL,
	PRIMARY KEY(user_id, school_id)
	FOREIGN KEY(user_id) REFERENCES user_archive(user_id),
	FOREIGN KEY(school_id) REFERENCES school_list(school_id)
);

CREATE TABLE subject_list(
	subject_id INT AUTO_INCREMENT,
	subjectname VARCHAR(255) NOT NULL,
	color BINARY(3) NOT NULL,
	PRIMARY KEY(subject_id)
);

CREATE TABLE subject_conn(
	user_id INT NOT NULL,
	subject_id INT NOT NULL,
	PRIMARY KEY(user_id, subject_id),
	FOREIGN KEY(user_id) REFERENCES user_archive(user_id),
	FOREIGN KEY(subject_id) REFERENCES subject_list(subject_id)
);



