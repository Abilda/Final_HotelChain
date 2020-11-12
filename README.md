# Final_HotelChain
Backend for Full-Stack Web Application
The Project was developed by 5 very ambituous CS students of whom u will definetely here more about in the coming future. The purpose of the project was to exercise frontend, backend development with agile method. The Web application is a full stack app for a hotel chain which gives opportunities for bookings, managing and many more feautures. BackEnd was developed on Spring Boot, FrontEnd on React, designed using Figma and database was hosted locally on MySQL

User Cases of the project:

As a user, I want to access a home page that provides general information about the hotel chain, provides links to services offered by hotel web application, and give users the ability to log in
As a guest, I want to query the system so that I can find available rooms (by date, destination, occupancy) and create a booking
As a guest, I want to create a profile so that I can manage my past and upcoming bookings
As a desk clerk, I want to cancel, create, and change, bookings so that I can fulfill guest requests
As a manager, I want to review the schedules of all hotel employees so that I can make payroll and adjust hours
As a manager, I want to create and cancel seasonal rates and issue advisories so that I can ensure that guests and employees have up-to-date information API documentation.
User Case 4:

Firstly, the desk clerk has access to all the guests. The request to query them is a GET request: http://localhost:8080/api/clerkdesk/getAllGuests. The method does not take any parameters, and requires an authentication token of a user with Role "ROLE_MODERATOR". We decided to hard code such a user directly in the database, which means that the user in Role "ROLE_MODERATOR" should be created directly inside the database.

Then, the desk clerk selects one particular users and has access towards all of his active reservations. The GET type request - http://localhost:8080/api/clerkdesk/getActiveReservationsForUser?Id={IdofUser} responds with the list of Reservation Ids. The method requires authentication token of a user in Role of moderator. Besides the authorization token, the request takes one PathParamater Id (with capital 'I' :) ) which represents the Id of the user for which active reservations are requested. P.s. In order to get the reservation itself perform a GET request to http://localhost:8080/api/bookings?Id={reservationId}

In order to delete a reservation, the clerk should perform POST request to http://localhost:8080/api/bookings/delete. The method requires authentication token in the header, and Body with "Id" parameter. Example of RequestBody - { "Id": 6}. To create a reservation refer to user case 3 :).

Editing - http://localhost:8080/api/bookings/editBooking Post request. Requires authentication and requestBody. RequestBody example - { "from" : "2020-05-11", "to": "2020-12-12", "reservationId": "15" }

User Case 5:

5th user case was implemented with 2 GET (get all employees, get the schedule of employee) methods and 1 POST (adjust our to the employee schedule). All 3 methods require authentication as an Admin. The user with the role is as well hardcoded in the database:

http://localhost:8080/api/employee/getAll?hotelId={hotelId} - returns a list of Employees. http://localhost:8080/api/employee/getschedule?employeeId=2. Example that returned a list of schedule for employee with employeeId = 2 [{ "dayOfTheWeek": "Monday is a cool day", "startTime": "10:00", "endTime": "18:00", "id": 1 }]. startTune and endTime are just simple Strings :) http://localhost:8080/api/employee/adjustSchedule. Requires schedule object in the Request Body. Example - { "dayOfTheWeek": "Tuesday", "startTime": "09:00", "endTime": "18:00", "id": 2 }

User Case 6:

http://localhost:8080/api/season/getSeasons - Get Request with no parameters and headers. Responds with all seasons

{ "From 2020-01-31 06:00:00.0 to 2021-02-06 06:00:00.0, discount rate - 15.0": [ "AmsterdamHotel, at city Amsterdam" ], "From 2020-12-01 06:00:00.0 to 2020-12-31 06:00:00.0, discount rate - 10.0": [ "AmsterdamHotel, at city Amsterdam", "Hotel1, at city Nur-Sultan" ] }

http://localhost:8080/api/season/create - Post request that creates a season. Requires authentication as admin and Season object in requestBody in the following pattern: { "seasonalRate": 15.0, "startDate": "2020-01-31T00:00:00.000+00:00", "endDate": "2021-02-06T00:00:00.000+00:00" }

Every hotel has its own seasons and in order to add a season to particular hotel use Post request - http://localhost:8080/api/season/addSeasonToHotel?hotelId={hotelId}&seasonId={seasonId} the method requires authentication token as admin and 2 path params.

Presumably, advisories will be issued after the guest signs in. Issusing advisories is based on the current date. The logic was immplemented in the following way: Firstly, the method loooks for all active seasons in the current time. After that, the method compares seasonalRates among active seasons and choses the best one among them. Finally, the method responds with a string. Example: GET Method with no params and no authentication required - http://localhost:8080/api/season/getadvisory Its response : {"message": "There is currently a 15.0 % discount for rooms at AmsterdamHotel - Amsterdam city"}
