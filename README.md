# Project Details
Sprint 2: Server
Ethan Jones (ejones38) Nick Li (nli24)
Time Estimate: 28 hours 
https://github.com/cs0320-s24/server-nli24-ejones38.git 
# Design Choices
## State and County
We chose to wrap the data contained for states (name, statewide) and county (county name, statecode, 
and county code) in state and county wrapper classes. This allowed us to avoid having to contain a list 
of strings to represent the data within the statemap and county maps. This also made the readability much easier, 
as well as the getters for when we need to get state codes and county codes.
## Eviction Policy
We chose to use enums to contain the eviction policy. This allowed us for easy access to the different 
cases, which we just different constructors of the cache builder. Because each eviction policy didn’t have 
much difference (At least in terms of the code we need to write for them), they can be easily contained as enums.
## Datasource interface
We chose to have a datasource interface class that allows for different sources of data, 
either the ACSDatasource, or mocked data (to allow for easy testing, and prevent sending too many requests). 
Another important design choice, was the decision to contain a statemap within ACSDatasource, 
but not within the cache. This allows for the class to contain the state map, 
and prevents us from having to call multiple requests for the same state map (since we only need it once). 
This also makes more sense since the class that contains the cache should likely hold all the data that 
needs to be inputted into the final response map.
## Overall Design
CensusAPIUtilities sends the request and deserializes the response. ACSDataSource stores the deserialized data.
Broadband handler processes the data and returns it to the user in the user output format. The handlers 
for csv operate similarly, with loadcsv loading the file and parsing it, storing it in a shared state (CSVWrapper)
Viewcsv essentially just returns the data from the shared state. Searchhandler includes the CSVSearch class 
which performs the search, and the handler class configures the output and returns it to the user.

# Errors/Bugs
As of 6:03 PM on Friday, Feb 16, we have not gotten the run function to work. On both of our computers, 
we can run mvn clean and mvn package, but we get the error saying "ls: src/main/java/edu/brown/cs/*/*/
server/Server.java: No such file or directory You need a Server.java" We have verified that we are calling
this in the right place and have stored Server in the right place with the right name. We both had problems
with run on the last project. We also looked through Ed, implemented the suggestions with getting the pom.xml
file from the gearup, nothing worked. However, the project works fine with the green play button. 
# Tests
## Overview
Our project has four testing suites: testCensusApiUtilities, testHandlers, testHandlersMock, and testCache. All methods
first if a connection is properly established before testing specific handler functionality.
## testCensusApiUtilities
testCensusApiUtilities tests our API utilities class' ability to obtain and deserialize a JSON from the API and properly
return its values. Each of the classes methods test CensusAPiUtilities ability to obtain state, county or broadband data
## testCache
testCache tests our cache's ability to store, retain and remove values based on a given size and evictionPolicy. The 
first method tests cache's ability to store data up to the specified size. The second method tests to make sure that our
cache adds and remove values based on the size eviction policy. The third method tests the same criteria for the 
restriction eviction policy. 
## testHandlers 
testHandlers tests each of our handlers integrated output by first setting up a server and HTTPurlConnection. Our tests 
make an api call using that connection allowing us to test each handler.
For load, we test the handlers ability to successfully load a CSV file as well as responses to incorrect files or no 
input entirely. 
For view, we test views ability to correctly view a loaded csv and its response when no csv is loaded. 
For search, we test its ability to search when provided only a value, value and index, value and column Header. We also 
test its responses when no value is provided, no CSV has been loaded or the index provided is out of bounds. 
For broadband, we test our handlers ability to successfully retrieve data given a proper state/county. Both the response
message and broadband data are tested. Additionally, we test to make sure broadband outputs an error if given a state or
county that does not exist.
## testHandlersMock
testHandlersMock tests broadbandHandlers outputs when given a pre-set url containing both the state and county info. 
This allows us to test broadband without its integration with CensusAPIUtilities.  Both the response message and 
broadband data are tested. Additionally, we test to make sure broadband outputs an error if given a state or county 
that does not exist.
## Note on Mocked Data
We started integration testing, but we didn’t know we were supposed to use mocked data until we had 
finished the test suite. So, instead of deleting it, we just kept it and made a separate file for 
integration testing based on mocked data. It seems that both files work, even though one of them is sending 
repeated requests.

# How to
In order run our program you must first run the server either through intelliJ via the play button or run the program 
using mvn package and ./run from a command terminal. Once the server is initialized navigate to a web browser and input 
http://localhost:3232/(choice of handler) with a handlers inputs being specified as handler?paramName=value&paramName..
this will allow you to use each of the handlers for their intended purposes. In order to run the tests either use the
IntelliJ play button or run mvn site and target/site/jacoco/index.html in a web browser to view the tests. 

## Configure Eviction Policy
The project supports no eviction policy, time based after access eviction policy, reference based, and size based
eviction policies. When initializing the cache in Server, change the eviction policy enum to the type that you want.
Time based and size based eviction policies both require a number that you must input, along with the type. 
The number for time based should be inputted in minutes after access. 
## Cacheless
In the event that you don't want to use a cache, simply comment out the initialization of the cache in the server class.
You also should remove this argument from initialization of the broadbandHandler in the following line. 
This will also use the second version of the broadbandHandler, where there is no cache passed in. 