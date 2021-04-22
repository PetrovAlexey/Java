create table if not exists aircrafts (
  aircraft_code char(3) not null,
  model VARCHAR(500) not null,
  range INT not null,
  primary key(aircraft_code)
);

create table if not exists airports (
  airport_code char(3) not null,
  airport_name VARCHAR(500) not null,
  city VARCHAR(500) not null,
  coordinates VARCHAR(500) not null,
  timezone VARCHAR(500) not null,
  primary key(airport_code)
);

create table if not exists bookings(
  book_ref char(6) not null,
  book_date timestamp not null,
  total_amount numeric(10,2),
  primary key (book_ref)
);

create table if not exists flights(
  flight_id INT not null,
  flight_no char(6) not null,
  scheduled_departure timestamp not null,
  scheduled_arrival timestamp not null,
  departure_airport  char(3) not null,
  arrival_airport char(3) not null,
  status varchar(20) not null,
  aircraft_code char(3) not null,
  actual_departure timestamp,
  actual_arrival timestamp,
  foreign key (aircraft_code) references aircrafts(aircraft_code),
  primary key (flight_id),
  foreign key (departure_airport) references airports(airport_code),
  foreign key (arrival_airport) references airports(airport_code)
);

create table if not exists seats(
  aircraft_code char(3) not null,
  seat_no varchar(4) not null,
  fare_conditions varchar(10) not null,
  foreign key (aircraft_code) references aircrafts(aircraft_code)
);

create table if not exists tickets(
  ticket_no char(13) not null,
  book_ref char(6) not null,
  passenger_id varchar(20) not null,
  passenger_name VARCHAR(500) not null,
  contact_data VARCHAR(500),
  foreign key (book_ref) references bookings(book_ref),
  primary key (ticket_no)
);

create table if not exists ticket_flights(
  ticket_no char(13) not null,
  flight_id INT not null,
  fare_conditions varchar(10) not null,
  amount numeric(10,2) not null,
  foreign key (ticket_no) references tickets(ticket_no),
  foreign key (flight_id) references flights(flight_id),
  primary key (ticket_no, flight_id)
);

create table if not exists boarding_passes(
  ticket_no char(13) not null,
  flight_id INT not null,
  boarding_no INT not null,
  seat_no varchar(4),
  primary key (ticket_no, flight_id),
  foreign key (ticket_no, flight_id) references ticket_flights(ticket_no, flight_id)
);


