# facilitybooking
Facility Booking Backend<br>

------Admin(generate facility and timeslot)<br>

url/api/v1/admin/facility?name="Dewan Gemilang"&description="Dewan serbaguna"&slotduration=120&start=05:00&end=22:00<br>

name(required)</br>
description(not required)</br>
slotduration(required) more than 30 , less than 120</br>
start(required) more than 05:00</br>
end(required)less than 23:00</br>

url/api/v1/admin/timeslot?facilityname=dewan gemilang&date=2024-09-04<br>

facilityname(required) generated facility</br>
date(required) (yyyy-mm-dd) not yesterday or past <br>


------fetch all facility or byname</br>
url/api/v1/facility?facilityname<br>

facilityname(not required)<br>




------fetch alltimeslot by name or byname and date<br>
url/api/v1/timeslot?facilityname=dewan gemilang&date=2024-09-06<br>

facilityname(required)</br>
date(not required) need to be present or future<br>


------show user timeslot</br>
url/api/v1/user?username="ikhwan"<br>

username(required)<br>

-----book and unbook<br>
url/api/v1/booking?action=book&timeslotid=25&username="ikhwan"<br>

action(required)"book" or"unbook"<br>
timeslotid(required)<br>
username (not required)<br>






