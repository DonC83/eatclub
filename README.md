# EatClub

### Assumptions

- input time is in 24 hour format
- Deals have a start and end time or an open and close time
- Data returned from the end point is not changing at a frequent rate so storing data as a class variable as an arbitrary form of caching should be acceptable
- deals with no start and end time or no open and close time are not considered active and are ruled out

### Notes
- calling the endpoint data in the tests is not ideal, should probably provide a mock for this

```
AWS access portal URL: https://d-9767a376df.awsapps.com/start, Username: eatclub, One-time password: j?FRIj!xbfe&wLG)??nBAyj0b@)hNR.E3bO--7e^m2-B6?XN1
```