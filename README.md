# Project Details
Sprint 2: Server
Ethan Jones (ejones38) Nick Li (idk)
Time Estimate: too long
https://github.com/cs0320-s24/server-nli24-ejones38.git 
# Design Choices

# Errors/Bugs


# Tests
Our project has three testing suites: testCensusApiUtilities, testHandlers, and testHandlersMock.
testCensusApiUtilities tests our API utilities class ability to obtain a JSON 
# How to

## Configure Eviction Policy
The project supports no eviction policy, time based after access eviction policy, reference based, and size based
eviction policies. When initializing the cache in Server, change the eviction policy enum to the type that you want.
Time based and size based eviction policies both require a number that you must input, along with the type. 
The number for time based should be inputted in minutes after access. 
## Cacheless
In the event that you don't want to use a cache, simply comment out the initialization of the cache in the server class.
You also should remove this argument from initialization of the broadbandHandler in the following line. 
This will also use the second version of the broadbandHandler, where there is no cache passed in. 