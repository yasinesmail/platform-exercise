# User Authentiction Api

This code is written in java using SpringBoot. The reason I picked this
SpringBoot, because this framework provides several exception classes and
response status codes that are easy to use. The is build using gradle.

The api has five endpoints. Two endpoints that update and delete a
user require token to be passed in from the header.
The endpoints are listed below:

1) Register a new user, this creates a new user in the postgresql database:<br/>
<pre>
   POST http://localhost:8080/user<br/>
   Payload : {
				"name": "Yasin Esmail",
				"email": "yasin.esmail@yahoo.com",
				"password":"password"
			 }
</pre>

2) Returns a new token, given the user name and password. This token is saved in the
   database, and returned when asked for.<br/>
<pre>
   POST http://localhost:8080/user/token
   Payload: {
				"password":"test",
				"email": "yasin.esamail@yahoo.com"
			}
</pre>

3) Clears the token from the database.<br/>
<pre>
   PUT http://localhost:8080/user/{user_id}/logout
</pre>

4) Update user fields, such as name, email and password.
   This field requires a token in header.<br/>
<pre>
  header : 24ef80774dd048708c01cd26301196b8
	PUT http://localhost:8080/user/{user_id}
	Payload: {
				"name": "Yasin Esmail",
				"email": "yasin.esmail@yahoo.com",
				"password":"test"
			 }
</pre>

	Missing fields in the payload structure will be ignored.

5) Deletes the user from the database.<br/>

<pre>
	header : 24ef80774dd048708c01cd26301196b8
	DELETE http://localhost:8080/user/{user_id}
</pre>


# Design

The code is laid out using the diagram below:

   ____________       _________       ____________       __________
  | Controller | <-> [ Service | <-> [ Repository | <-> [ Postgres ]
   ------------       ---------       ------------       ----------

  The rest end points reside in the Controller class, and all business logic
  such as enrichment and filtering data, when we get the data from database.
  Finally, all simple and difficult queries reside in Repository. This form of
  separation allows to better separate code logic and write unit tests.

  The Mapper class converts the DAO model data to DTO data. The data conversion
  mostly occurs in the Controller module, since this is where data leaves the service
  and gets converted to JSON. The image below makes an attempt to explain.

   ____________       _________       ____________       __________
  | Controller | <-> [ Service | <-> [ Repository | <-> [ Postgres ]
   ------------       ---------       ------------       ----------

               ________
              | Mapper |
               --------

  I have also added the a few Exception class, in case the platform resource is not found.

 # Things needed to run this code:

 First create a dabase "platform" in the postgres database :<br/>
 DATABASE : platform<br/>

 Then run the schema below, create the tables:<br/>

<pre>
 CREATE TABLE public.users
(
    id integer NOT NULL DEFAULT nextval('users_id_seq'::regclass),
    name character varying(40) COLLATE pg_catalog."default",
    email character varying(80) COLLATE pg_catalog."default",
    password character varying(80) COLLATE pg_catalog."default",
    token character varying(80) COLLATE pg_catalog."default",
    CONSTRAINT users_pkey PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;


CREATE INDEX token_idx
    ON public.users USING btree
    (token COLLATE pg_catalog."default")
    TABLESPACE pg_default;

</pre>

Finally, go the the platform directory in the project<br/>
 <b>cd platform</b>

 and run the command below:<br/>
 <b>./gradlew clean bootRun</b>


Keep in mind the database configurations are below, please change the user name and password
as shown below, based on your configs to connect to your local database.

<pre>
spring.datasource.platform=postgres
spring.datasource.url=jdbc:postgresql://localhost:5432/platform
spring.datasource.username=<b>yasin</b>
spring.datasource.password=<b>yasin</b>
spring.jpa.generate-ddl=true
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation=true
</pre>
