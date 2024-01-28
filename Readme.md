# Booking to Event
This API allows customers to create, find and reserve tickets for events, view and manage their reservations, and be notified before the event kickoff.
This application will help to create this microservice on java.

___
# Sequence diagram
This how would work application:

<img src="docs/sequence_diagram.png" alt=""/>

1. Client send request to Controller, which resend request to service
2. Service get request and resend to Repository
3. Repository send request to Database
4. The database processed the request and sent back
___

# Table diagrams
Simple action to edit note:

<img src="docs/tables_diagram.png" alt=""/>

We have two main classes - User and Event.

User table represents user in this application. User can auth, create event and get all event.

Event table represents event in this application. Event can be created by user.

Also on Event you can reserve the ticket for event or cancel your reserving.  

___

# Environments

To run this application you need to create `.env` file in root directory with next environments:
* `POSTGRES_HOST` - host of Postgresql database
* `POSTGRES_PORT` - port of Postgresql database
* `POSTGRES_USERNAME` - username for Postgresql database
* `POSTGRES_PASSWORD` - password for Postgresql database
* `POSTGRES_DATABASE` - name of Postgresql database
* `POSTGRES_SCHEMA` - name of Postgresql schema
* `JWT_SECRET` - secret string for JWT tokens
* `JWT_DURATION` - duration time to expire the token(in hour)
* `SPRING_MAIL_HOST` - host of mail 
* `SPRING_MAIL_PORT` - port of mail
* `SPRING_MAIL_USERNAME` - username for email, which would send notification
* `SPRING_MAIL_PASSWORD` - password fo email

___

# How to work for this application
1. You need to have Docker Engine and Docker Compose on your machine(or Docker Desktop).
2. Add .end file on application directory and wrote all above environments with variable.
3. After you finish the 1st and 2nd points you can download this application and do "docker compose up" in application directory.
4. Then you can check all application endpoint on swagger(http://localhost:8080/swagger-ui/index.html).
5. The post method "/users" endpoint create user for application with next parameters: name,email and password. 
6. The post method "/auth" endpoint allows us to get token for passing authentication process. The request need email and password.
7. The post method "/events" endpoint create event for application with parameter: id, name, date, available count, description and category.
8. The get method "events" endpoint allows us get all event from table. On this point we use three variable: name event, started date, ended date and category.
9. The post method "events/{eventId}/tickets" let us reserve the place in event.
10. The post method "events/{eventId}/cancel" allows us cancel user's reserve.
11. The get method "/users/{userId}/view" get all event of reserving by user
12. After you finish the job press "CTRL+C" for stop docker container

Also I add json file for Postman app([PostmanCollectionEndPoints.json](PostmanCollectionEndPoints.json)). If you don't like the swagger you can use Postman for this target.

P.S. Remender Service is notification user about event at 12:00, but you can change it in ReminderImpl.class on here 
<img src="docs/scheduler.png" alt=""/>

Use Cron pattern:https://crontab.guru/

Also add swagger page:
<img src="docs/swagger_page.png" alt=""/>
