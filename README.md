# Project Details
Sprint 2: Server
Ethan Jones (ejones38) Nick Li (nli24)
Time Estimate: 28 hours 
https://github.com/cs0320-s24/server-nli24-ejones38.git 
# Design Choices
utiilites, county, state, datasourec interface, mocked, eviction policy, acsdata,

We chose to have a CensusAPIUtilities class 


# Errors/Bugs
no current bugs 
# Tests
Our project has four testing suites: testCensusApiUtilities, testHandlers, testHandlersMock, and testCache. All methods
first if a connection is properly established before testing specific handler functionality.

testCensusApiUtilities tests our API utilities class' ability to obtain and deserialize a JSON from the API and properly
return its values. Each of the classes methods test CensusAPiUtilities ability to obtain state, county or broadband data

testCache tests our cache's ability to store, retain and remove values based on a given size and evictionPolicy. The 
first method tests cache's ability to store data up to the specified size. The second method tests to make sure that our
cache adds and remove values based on the size eviction policy. The third method tests the same criteria for the 
restriction eviction policy. 

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

testHandlersMock tests broadbandHandlers outputs when given a pre-set url containing both the state and county info. 
This allows us to test broadband without its integration with CensusAPIUtilities.  Both the response message and 
broadband data are tested. Additionally, we test to make sure broadband outputs an error if given a state or county 
that does not exist.
# How to
In order run our program you must first run the server either through intelliJ via the play button or run the program 
using mvn package and ./run from a command terminal. Once the server is initialized navigate to a web browser and input 
http://localhost:3232/(choice of chandler) with a handlers inputs being specified as handler?paramName=value&paramName..
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