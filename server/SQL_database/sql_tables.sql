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
	
	schooltype_id AUTO_INCREMENT NOT NULL,
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
