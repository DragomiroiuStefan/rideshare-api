--------Find Rides-----------------------

-- Find rides that go from departure to arrival
SELECT r.ride_id,
       r.driver,
       r.seats,
       r.vehicle,
       rc_dep.departure_time,
       rc_arr.arrival_time
FROM ride r
         JOIN ride_connection rc_dep ON r.ride_id = rc_dep.ride_id
         JOIN ride_connection rc_arr ON r.ride_id = rc_arr.ride_id
WHERE rc_dep.departure_location = 4
  AND rc_arr.arrival_location = 7
  AND rc_dep.departure_time < rc_arr.arrival_time
  AND r.status = 'ACTIVE'
  AND r.departure_date = '2024-06-27';

-- Check available seats
select rc.connection_id, rc.price, sum(b.seats) as booked_seats
from ride_connection rc
         left join booking_connection bc on rc.connection_id = bc.connection_id
         left join booking b on b.booking_id = bc.booking_id and b.status = 'CONFIRMED'
where rc.ride_id = 21
  and rc.departure_time >= '2024-06-27 17:36:43.000000'
  and rc.arrival_time <= '2024-06-27 21:36:43.000000'
group by rc.connection_id;

-- Find total price for ride
SELECT
    rc.ride_id,
    sum(rc.price)
FROM
    ride_connection rc
GROUP BY
    rc.ride_id;

-- Find Driver Info
SELECT COUNT(rr.rating), AVG(rr.rating) AS average_rating
FROM "user" u
         JOIN ride r ON u.user_id = r.driver
         JOIN ride_rating rr ON r.ride_id = rr.ride_id
WHERE u.user_id = 2;

--------Book a ride-------------------------------
SELECT r.*,
       rc_dep.departure_time,
       rc_arr.arrival_time
FROM ride r
         JOIN ride_connection rc_dep ON r.ride_id = rc_dep.ride_id
         JOIN ride_connection rc_arr ON r.ride_id = rc_arr.ride_id
WHERE r.ride_id = 23
  AND rc_dep.departure_location = 4
  AND rc_arr.arrival_location = 6
  AND rc_dep.departure_time < rc_arr.arrival_time;

select rc.connection_id, rc.price, sum(b.seats) as booked_seats
from ride_connection rc
         left join booking_connection bc on rc.connection_id = bc.connection_id
         left join booking b on b.booking_id = bc.booking_id and b.status = 'CONFIRMED'
where rc.ride_id = 23
  and rc.departure_time >= '2024-06-27 17:36:43.000000'
  and rc.arrival_time <= '2024-06-27 20:36:43.000000'
group by rc.connection_id;

INSERT INTO "booking_connection" ("booking_id", "connection_id")
VALUES (13, 43);

