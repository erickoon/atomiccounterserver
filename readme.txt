Please have the following installed:
JDK 1.8
Apache Benchmark tool - https://httpd.apache.org/docs/current/programs/ab.html

To start the server:
javac CounterServer.java
java CounterServer

Server has the following urls:
http://localhost:8000/
http://localhost:8000/index - This page has the increment counter button

To run the automation test:
Start the server or restart if it is already running
./test.sh
