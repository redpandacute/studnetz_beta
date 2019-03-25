CREATE TABLE user_archive(
	user_id INT AUTO_INCREMENT,
	uuid_bin BINARY(16),
	uuid_text VARCHAR(36) generated always as

		(insert(
			insert(
				insert(
					insert(hex(uuid_bin),9,0,'-'),
				14,0,'-'),
			19,0,'-'),
		24,0,'-')
	) virtual,

	firstname VARCHAR(255) NOT NULL,
	lastname VARCHAR(255) NOT NULL,
	email VARCHAR(255) NOT NULL,
	password_hash VARCHAR(255)  NOT NULL,
	account_verification_state INT DEFAULT 0,
	email_verification_state INT DEFAULT 0,
	creation_date DATE DEFAULT NULL,
	lastactive DATETIME() DEFAULT NULL,
	PRIMARY KEY(user_id),
	UNIQUE(uuid_bin)
);

CREATE TABLE profile_archive(
	user_id INT NOT NULL,
	profilepicture_id INT DEFAULT NULL,
	calendar_id INT DEFAULT NULL,
	description TEXT,
	PRIMARY KEY(user_id)
);

CREATE TABLE schooltype_list(
	schooltype_id INT AUTO_INCREMENT,
	uuid_bin BINARY(16) NOT NULL,
	uuid_text VARCHAR(36) generated always as

		(insert(
			insert(
				insert(
					insert(hex(uuid_bin),9,0,'-'),
				14,0,'-'),
			19,0,'-'),
		24,0,'-')
	) virtual,

	schooltype_name VARCHAR(255),
	schooltype_abrv VARCHAR(8),
	PRIMARY KEY(schooltype_id)
);

CREATE TABLE school_list(
	school_id INT AUTO_INCREMENT,
	uuid_bin BINARY(16) NOT NULL,
	uuid_text VARCHAR(36) generated always as

	  	(insert(
			insert(
				insert(
					insert(hex(uuid_bin),9,0,'-'),
				14,0,'-'),
			19,0,'-'),
		24,0,'-')
	) virtual,
	
	schooltype_id INT,
	schoolname VARCHAR(255) NOT NULL,
	school_abrv VARCHAR(8) DEFAULT NULL,
	schoolgrades INT,
	PRIMARY KEY(school_id),
	FOREIGN KEY(schooltype_id) REFERENCES schooltype_list(schooltype_id)
);

CREATE TABLE school_conn(
	user_id INT NOT NULL,
	school_id INT NOT NULL,
	grade INT DEFAULT NULL,
	PRIMARY KEY(user_id, school_id),
	FOREIGN KEY(user_id) REFERENCES user_archive(user_id),
);

CREATE TABLE schoolverification_archive(
	school_id INT NOT NULL,
	verification_id INT NOT NULL,
	email VARCHAR(255) NOT NULL,
	firstname VARCHAR(255),
	lastname VARCHAR(255),
	grade INT DEFAULT 0,
	active_state INT DEFAULT 0,
	insertion_date DATE NOT NULL,
	expiration_date DATE DEFAULT NULL,
	PRIMARY KEY(school_id),
	FOREIGN KEY(school_id) REFERENCES school_list(school_id)
);

CREATE TABLE subject_list(
	subject_id INT AUTO_INCREMENT,
	uuid_bin BINARY(16) NOT NULL,
	uuid_text VARCHAR(36) generated always as

	  	(insert(
			insert(
				insert(
					insert(hex(uuid_bin),9,0,'-'),
				14,0,'-'),
			19,0,'-'),
		24,0,'-')
	) virtual,

	subjectname VARCHAR(255) NOT NULL,
	color BINARY(3) NOT NULL,
	PRIMARY KEY(subject_id),
	UNIQUE(uuid_bin)
);

CREATE TABLE subject_conn(
	user_id INT NOT NULL,
	subject_id INT NOT NULL,
	PRIMARY KEY(user_id),
	FOREIGN KEY(user_id) REFERENCES user_archive(user_id) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY(subject_id) REFERENCES subject_list(subject_id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE rating_sum(
	user_id INT NOT NULL,
	byesian_rating INT DEFAULT 0,
	last_updated DATETIME()
	FOREIGN KEY(user_id) REFERENCES user_archive(user_id) ON UPDATE CASCADE ON DELETE CASCADE	
);

CREATE TABLE rating_archive(
	rating_id INT AUTO_INCREMENT NOT NULL,
	user_id INT NOT NULL,
	tutoring_id INT NOT NULL,
	rating INT DEFAULT 0 MAX 5,
	issued_by INT NOT NULL,
	issued_at DATETIME() NOT NULL,
	rating_comment DEFAULT NULL,
	legitimacy INT DEFAULT NULL,
	PRIMARY KEY(rating_id),
	FOREIGN KEY(user_id) REFERENCES user_archive(user_id) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY(tutoring_id) REFERENCES tutoring_archive(tutoring_id) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY(issued_by) REFERENCES user_archive(user_id) ON UPDATE CASCADE ON DELETE CASCADE

);

CREATE TABLE tutoring_archive(
	tutoring_id INT AUTO_INCREMENT NOT NULL,
	uuid_bin BINARY(16) NOT NULL,
	uuid_text VARCHAR(36) generated always as
	  	(insert(
			insert(
				insert(
					insert(hex(uuid_bin),9,0,'-'),
				14,0,'-'),
			19,0,'-'),
		24,0,'-')
	) virtual,
	tutor_id INT NOT NULL,
	student_id INT NOT NULL,
	issued_by INT NOT NULL,
	issued_at DATETIME() NOT NULL,
	issued_for DATETIME() NOT NULL,
	tutor_agr INT DEFAULT NULL,
	student_agr INT DEFAULT NULL,
	cancellation DEFAULT NULL,
	tutor_conf INT DEFAULT NULL,
	student_conf INT DEFAULT NULL, --0: not confirmed, 1: confirmed and 2: dismissed (which should just get counted as a 4* rating) IF it is null, the message should get diplayed to the user on startup of the app
	rating_id INT DEFAULT NULL,
	PRIMARY KEY(tutoring_id),
	FOREIGN KEY(tutor_id) REFERENCES user_archive(user_id),
	FOREIGN KEY(student_id) REFERENCES user_archive(user_id),
	FOREIGN KEY(cancellation) REFERENCES cancellation_archive(cancellation_id),
	FOREIGN KEY(rating_id) REFERENCES rating_archive(rating_id)
);
--if smb chooses to unconfirm a tutoring it should ask wether the person (tutor or student) wants to write a report for the other user (TODO)

CREATE TABLE cancelltation_archive(
	cancellation_id INT NOT NULL,
	cancelled_by INT NOT NULL,
	cancelled_for INT NOT NULL,
	issued_at DATETIME() NOT NULL,
	noticed INT DEFAULT 0,
	cancellation_comment VARCHAR(255) DEFAULT NULL,
	PRIMARY KEY(cancellation_id),
	FOREIGN KEY(cancelled_by) REFERENCES user_archive(user_id),
	FOREIGN KEY(cancelled_for) REFERENCES user_archive(user_id)
);
