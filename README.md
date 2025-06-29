# EatClub

### Assumptions

- input time is in 24 hour format
- Deals have a start and end time or an open and close time
- Data returned from the end point is not changing at a frequent rate so storing data as a class variable as an arbitrary form of caching should be acceptable

### Notes
- calling the endpoint data in the tests is not ideal, should probably provide a mock for this