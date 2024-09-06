# facilitybooking
Facility Booking Backend

------Admin(generate facility and timeslot)

url/api/v1/admin/facility?name="Dewan Gemilang"&description="Dewan serbaguna"&slotduration=120&start=05:00&end=22:00

name(required)</br>
description(not required)</br>
slotduration(required) more than 30 , less than 120</br>
start(required) more than 05:00</br>
end(required)less than 23:00</br>

url/api/v1/admin/timeslot?facilityname=dewan gemilang&date=2024-09-04

facilityname(required) generated facility</br>
date(required) (yyyy-mm-dd) not yesterday or past 


------fetch all facility or byname</br>
url/api/v1/facility?facilityname

facilityname(not required)




------fetch alltimeslot by name or byname and date<br>
url/api/v1/timeslot?facilityname=dewan gemilang&date=2024-09-06

facilityname(required)</br>
date(not required) need to be present or future


------show user timeslot</br>
url/api/v1/user?username="ikhwan"

username(required)

-----book and unbook<br>
url/api/v1/booking?action=book&timeslotid=25&username="ikhwan"

action(required)"book" or"unbook"
timeslotid(required)
username (not required)






