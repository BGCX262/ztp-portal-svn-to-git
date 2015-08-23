create table REGISTEREDUSER (

ID int8,
NAME varchar (255),
SURNAME varchar (255),
LOGIN varchar (255),
PASSWORD varchar (255),
EMAIL varchar (255),
REGION varchar (255),
TOWN varchar (255),
HANDICAPTYPE varchar (255),
ADMIN boolean,
STATE boolean,
CHANGEDATE timestamp,
REGISTRATIONDATE timestamp,
PREFERREGION boolean,
primary key(ID));

CREATE TABLE Job ( 
	id int8 NOT NULL,
	region varchar(255),
	town varchar(255),
	county int8,
	sector integer NOT NULL,
	position integer,
	contract_type integer NOT NULL,
	start_date timestamp,
	job_specif varchar(255) NOT NULL,
	hour_payment integer NULL,
	work_duration integer NULL,
	creator_id int8 NOT NULL,
	type_of_advert integer NOT NULL,
	import_date timestamp,
	state boolean,
	active boolean,
	change_date timestamp,
	urlLink varchar(255),
	cms_content varchar(2048),
	primary key(ID)
);

CREATE TABLE Country(
id int8,
name varchar(255),
primary key(ID)
);

Insert into Country values( 1, 'Bratislavsk� kraj' );
Insert into Country values( 2, 'Trnavsk� kraj' );
Insert into Country values( 3, 'Tren�iansky kraj' );
Insert into Country values( 4, 'Nitriansky kraj' );
Insert into Country values( 5, 'Banskobystrick� kraj' );
Insert into Country values( 6, '�ilinsk� kraj' );
Insert into Country values( 7, 'Pre�ovsk� kraj' );
Insert into Country values( 8, 'Ko�ick� kraj' );



CREATE TABLE County(
id int8,
name varchar(255),
country int8,
primary key(ID)
);

Insert into County values( 1, 'Bratislava', (Select id from Country where name='Bratislavsk� kraj') );
Insert into County values( 2, 'Bansk� Bystrica', (Select id from Country where name='Banskobystrick� kraj') );
Insert into County values( 3, 'Bardejov', (Select id from Country where name='Pre�ovsk� kraj') );
Insert into County values( 4, 'B�novce nad Bebravou', (Select id from Country where name='Tren�iansky kraj') );
Insert into County values( 5, 'Brezno', (Select id from Country where name='Banskobystrick� kraj'));
Insert into County values( 6, 'Bansk� �tiavnica', (Select id from Country where name='Banskobystrick� kraj') );
Insert into County values( 7, 'Byt�a', (Select id from Country where name='�ilinsk� kraj') );
Insert into County values( 8, '�adca', (Select id from Country where name='�ilinsk� kraj') );
Insert into County values( 9, 'Doln� Kub�n', (Select id from Country where name='�ilinsk� kraj') );
Insert into County values( 10, 'Dunajsk� Streda', (Select id from Country where name='Trnavsk� kraj') );
Insert into County values( 11, 'Detva', (Select id from Country where name='Banskobystrick� kraj') );
Insert into County values( 12, 'Galanta', (Select id from Country where name='Trnavsk� kraj') );
Insert into County values( 13, 'Gelnica', (Select id from Country where name='Ko�ick� kraj') );
Insert into County values( 14, 'Hlohovec', (Select id from Country where name='Trnavsk� kraj') );
Insert into County values( 15, 'Humenn�', (Select id from Country where name='Pre�ovsk� kraj') );
Insert into County values( 16, 'Ilava', (Select id from Country where name='Tren�iansky kraj')  );
Insert into County values( 17, 'Krupina', (Select id from Country where name='Banskobystrick� kraj') );
Insert into County values( 18, 'Ko�ice', (Select id from Country where name='Ko�ick� kraj') );
Insert into County values( 19, 'Ke�marok', (Select id from Country where name='Pre�ovsk� kraj') );
Insert into County values( 20, 'Kysuck� Nov� Mesto', (Select id from Country where name='�ilinsk� kraj') );
Insert into County values( 21, 'Kom�rno', (Select id from Country where name='Nitriansky kraj') );
Insert into County values( 22, 'Ko�ice - okolie', (Select id from Country where name='Ko�ick� kraj') );
Insert into County values( 23, 'Lu�enec', (Select id from Country where name='Banskobystrick� kraj') );
Insert into County values( 24, 'Levo�a', (Select id from Country where name='Pre�ovsk� kraj') );
Insert into County values( 25, 'Liptovsk� Mikul�', (Select id from Country where name='�ilinsk� kraj') );
Insert into County values( 26, 'Levice', (Select id from Country where name='Nitriansky kraj') );
Insert into County values( 27, 'Malacky', (Select id from Country where name='Bratislavsk� kraj') );
Insert into County values( 28, 'Michalovce', (Select id from Country where name='Ko�ick� kraj') );
Insert into County values( 29, 'Medzilaborce', (Select id from Country where name='Pre�ovsk� kraj') );
Insert into County values( 30, 'Martin', (Select id from Country where name='�ilinsk� kraj') );
Insert into County values( 31, 'Myjava', (Select id from Country where name='Tren�iansky kraj')  );
Insert into County values( 32, 'Nov� Mesto n/V�h.', (Select id from Country where name='Tren�iansky kraj')  );
Insert into County values( 33, 'N�mestovo', (Select id from Country where name='�ilinsk� kraj') );
Insert into County values( 34, 'Nitra', (Select id from Country where name='Nitriansky kraj')  );
Insert into County values( 35, 'Nov� Z�mky', (Select id from Country where name='Nitriansky kraj') );
Insert into County values( 36, 'Pova�sk� Bystrica', (Select id from Country where name='Tren�iansky kraj')  );
Insert into County values( 37, 'Prievidza', (Select id from Country where name='Tren�iansky kraj')  );
Insert into County values( 38, 'Partiz�nske', (Select id from Country where name='Tren�iansky kraj')  );
Insert into County values( 39, 'Pezinok', (Select id from Country where name='Bratislavsk� kraj'));
Insert into County values( 40, 'Pie��any', (Select id from Country where name='Trnavsk� kraj') );
Insert into County values( 41, 'Pre�ov', (Select id from Country where name='Pre�ovsk� kraj') );
Insert into County values( 42, 'Poprad', (Select id from Country where name='Pre�ovsk� kraj') );
Insert into County values( 43, 'Polt�r', (Select id from Country where name='Banskobystrick� kraj') );
Insert into County values( 44, 'P�chov', (Select id from Country where name='Tren�iansky kraj')  );
Insert into County values( 45, 'Rev�ca', (Select id from Country where name='Banskobystrick� kraj') );
Insert into County values( 46, 'Ru�omberok', (Select id from Country where name='�ilinsk� kraj') );
Insert into County values( 47, 'Rimavsk� Sobota', (Select id from Country where name='Banskobystrick� kraj') );
Insert into County values( 48, 'Ro��ava', (Select id from Country where name='Ko�ick� kraj') );
Insert into County values( 49, '�a�a', (Select id from Country where name='Nitriansky kraj') );
Insert into County values( 50, 'Sabinov', (Select id from Country where name='Pre�ovsk� kraj') );
Insert into County values( 51, 'Senec', (Select id from Country where name='Bratislavsk� kraj') );
Insert into County values( 52, 'Senica', (Select id from Country where name='Trnavsk� kraj') );
Insert into County values( 53, 'Skalica', (Select id from Country where name='Trnavsk� kraj') );
Insert into County values( 54, 'Svidn�k', (Select id from Country where name='Pre�ovsk� kraj') );
Insert into County values( 55, 'Star� �ubov�a', (Select id from Country where name='Pre�ovsk� kraj') );
Insert into County values( 56, 'Spi�sk� Nov� Ves', (Select id from Country where name='Ko�ick� kraj') );
Insert into County values( 57, 'Sobrance', (Select id from Country where name='Ko�ick� kraj') );
Insert into County values( 58, 'Stropkov', (Select id from Country where name='Pre�ovsk� kraj') );
Insert into County values( 59, 'Snina', (Select id from Country where name='Pre�ovsk� kraj') );
Insert into County values( 60, 'Tren��n', (Select id from Country where name='Tren�iansky kraj')  );
Insert into County values( 61, 'Topo��any', (Select id from Country where name='Nitriansky kraj') );
Insert into County values( 62, 'Tur�ianske Teplice', (Select id from Country where name='�ilinsk� kraj') );
Insert into County values( 63, 'Tvrdo��n', (Select id from Country where name='�ilinsk� kraj') );
Insert into County values( 64, 'Trnava', (Select id from Country where name='Trnavsk� kraj') );
Insert into County values( 65, 'Trebi�ov', (Select id from Country where name='Ko�ick� kraj') );
Insert into County values( 66, 'Ve�k� Krt�', (Select id from Country where name='Banskobystrick� kraj') );
Insert into County values( 67, 'Vranov nad Top�ou', (Select id from Country where name='Pre�ovsk� kraj') );
Insert into County values( 68, '�ilina', (Select id from Country where name='�ilinsk� kraj') );
Insert into County values( 69, '�arnovica', (Select id from Country where name='Banskobystrick� kraj') );
Insert into County values( 70, '�iar nad Hronom', (Select id from Country where name='Banskobystrick� kraj') );
Insert into County values( 71, 'Zlat� Moravce', (Select id from Country where name='Nitriansky kraj') );
Insert into County values( 72, 'Zvolen', (Select id from Country where name='Banskobystrick� kraj') );

insert into REGISTEREDUSER (ID, NAME, SURNAME, LOGIN, PASSWORD,ADMIN,STATE,PREFERREGION) values ('0','Admin','Administrator','admin','admin','true','true','true');

CREATE TABLE Sector_of_work(
id int8,
name varchar(255),
primary key(ID)
);

Insert into sector_of_work values(	1, 'Administrat�va'	);
Insert into sector_of_work values(	2, 'Automobilov� priem.'	);
Insert into sector_of_work values(	3, 'Ban�ctvo, hutn�ctvo'	);
Insert into sector_of_work values(	4, 'Bankovn�ctvo'	);
Insert into sector_of_work values(	5, 'Bezpe�nos� a ochrana'	);
Insert into sector_of_work values(	6, 'Cestovn� ruch'	);
Insert into sector_of_work values(	7, 'Doprava, �ped�cia'	);
Insert into sector_of_work values(	8, 'Drevospracuj�ci priem.'	);
Insert into sector_of_work values(	9, 'Ekonomika, financie'	);
Insert into sector_of_work values(	10, 'Elektrotechnika'	);
Insert into sector_of_work values(	11, 'Chemick� priemysel'	);
Insert into sector_of_work values(	12, 'Informa�n� technol�gie'	);
Insert into sector_of_work values(	13, 'Leasing'	);
Insert into sector_of_work values(	14, 'Mana�ment kvality'	);
Insert into sector_of_work values(	15, 'Marketing, reklama'	);
Insert into sector_of_work values(	16, 'Obchod'	);
Insert into sector_of_work values(	17, 'Pois�ovn�ctvo'	);
Insert into sector_of_work values(	18, 'Po�nohospod�rstvo'	);
Insert into sector_of_work values(	19, 'Pomocn� pr�ce'	);
Insert into sector_of_work values(	20, 'Pr�vo a legislat�va'	);
Insert into sector_of_work values(	21, 'Prekladate�stvo'	);
Insert into sector_of_work values(	22, 'Slu�by'	);
Insert into sector_of_work values(	23, 'Stavebn�ctvo a reality'	);
Insert into sector_of_work values(	24, 'Stroj�rstvo'	);
Insert into sector_of_work values(	25, '�kolstvo, veda, v�skum'	);
Insert into sector_of_work values(	26, '�t�tna spr�va'	);
Insert into sector_of_work values(	27, 'Telekomunik�cie'	);
Insert into sector_of_work values(	28, 'Umenie a kult�ra'	);
Insert into sector_of_work values(	29, 'Vodohospod.,lesn�ctvo'	);
Insert into sector_of_work values(	30, 'V�roba'	);
Insert into sector_of_work values(	31, 'Z�kazn�cka podpora'	);
Insert into sector_of_work values(	32, 'Zdravotn�ctvo, farm�cia'	);
Insert into sector_of_work values(	33, '�urnalistika, m�di�'	);


CREATE TABLE Contract_type(
id int8,
name varchar(255),
primary key(ID)
);

Insert into contract_type values(	1, 'Pln� �v�zok'	);
Insert into contract_type values(	2, 'Skr�ten� �v�zok'	);
Insert into contract_type values(	3, '�ivnos�'	);
Insert into contract_type values(	4, 'Na dohodu'	);
Insert into contract_type values(	5, 'Brig�da'	);

CREATE TABLE ADVERT_TYPE(
id int8,
name varchar(255),
primary key (ID)
);

Insert into advert_type values(1, 'Ponuka');
Insert into advert_type values(2, 'H�ad�m');

CREATE TABLE Handicap_type(
id int8,
name varchar(255),
primary key(ID)
);

Insert into handicap_type values(1, 'Bez postihnutia');
Insert into handicap_type values(2, 'Nevidiaci / kr�tkozrak�');
Insert into handicap_type values(3, 'Motorick� obmedzenie');
Insert into handicap_type values(4, 'Zdravotn� postihnutie');
Insert into handicap_type values(5, 'In�');

CREATE TABLE CMS_CONTENT(
id int8,
name varchar(255),
content varchar(2000),
primary key(ID)
);

Insert into cms_content values(1, 'job','');
Insert into cms_content values(2, 'main','');
Insert into cms_content values(3, 'registration','');
Insert into cms_content values(4, 'forum','');
Insert into cms_content values(5, 'event','');
Insert into cms_content values(6, 'dayCare','');

CREATE TABLE GLOBAL_SETTING(
id int8,
name varchar(255),
value varchar(255)  NOT NULL,
primary key(ID)
);

Insert into global_setting values(1, 'jobDeactivation','1');

CREATE TABLE Thread ( 
	id int8 NOT NULL,
	thread_name varchar(255) NOT NULL,
	owner int8 NOT NULL,
	import_date timestamp,
	state boolean,
	active boolean,
	theme int8 NOT NULL,
	change_date timestamp,
	primary key(ID)
);


CREATE TABLE Comment ( 
	id int8 NOT NULL,
	comment_name varchar(255) NOT NULL,
	owner int8 NOT NULL,
	import_date timestamp,
	state boolean,
	active boolean,
	thread int8,
	change_date timestamp,
	notice int8 DEFAULT 0,
	event int8,
	primary key(ID)
);


CREATE TABLE Theme ( 
	id int8 NOT NULL,
	name varchar(255) NOT NULL,
	import_date timestamp,
	change_date timestamp,
	state boolean,
	primary key(ID)
);

--Insert into Theme values(1, 'Cestovanie MHD','','','true');

CREATE TABLE Events ( 
	id int8 NOT NULL,
	name varchar(255),
	user_id int8,
	type int8,
	town int8,
	address varchar(255),
	start_date timestamp,
	end_date timestamp,
	events_length int8,
	notes varchar(255),
	guest_count int8,
	state boolean,
	active boolean,
	import_date timestamp,
	change_date timestamp,
	primary key(ID)
);

CREATE TABLE EVENT_TYPE(
id int8,
name varchar(255),
primary key (ID)
);

CREATE TABLE PICTURE(
id int8,
import_date timestamp,
change_date timestamp,
link varchar(255),
active boolean,
state boolean,
image bytea,
owner int8,
event int8,
primary key (ID)
);


Insert into event_type values(1, 'Stretnutie');
Insert into event_type values(2, 'Kult�ra');

create sequence hibernate_sequence;