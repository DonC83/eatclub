# EatClub

### Assumptions

- input time is in 24 hour format
- Deals have a start and end time or an open and close time
- Data returned from the end point is not changing at a frequent rate so storing data as a class variable as an arbitrary form of caching should be acceptable
- deals with no start and end time or no open and close time are not considered active and are ruled out

### Notes
- calling the endpoint data in the tests is not ideal, should probably provide a mock for this


### Testing 
The API has been deployed to AWS and can be tested using the following command:

Get Deals by Time of Day
```bash
curl 'https://fvt3a61408.execute-api.ap-southeast-2.amazonaws.com/dev/eatclub/deals?timeOfDay=18%3A00'
```

Get peak time period
```bash
curl https://fvt3a61408.execute-api.ap-southeast-2.amazonaws.com/dev/eatclub/deals/peaktime
```