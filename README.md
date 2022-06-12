# Distributed-whiteboard

- **University:** University of Melbourne
- **Session:** Semester 1 2022
- **Subject:** COMP90015 Distributed Systems

## Getting started

Create Whiteboard Server:

```bash
$ java -jar CreateWhiteBoard.jar 127.0.0.1 2022
```

Join Whiteboard:

```bash
$ java -jar JoinWhiteBoard.jar
```

## Main components of the system

### Message format
Since each drawing by the user on the whiteboard will contain a lot of information (such as coordinates, colors, types, etc.), I choose to use JSON as the message format.  
```yaml
{  
  "cmd": ‘login’ – login; ‘sendAll’ – send data to all user; ‘query’ – get the latest data; ‘queryHistory’ - When a new user joins, it will query all historical data once; queryCollaborator – Find all collaborators.  
  "data": [{  
    "startPointX": The X coordinate of the mouse press  
    "startPointY": The Y coordinate of the mouse press  
    "endPointX": The X coordinate of the mouse release  
    "endPointY": The Y coordinate of the mouse release  
    "color": The string representation of this Color (Use the toString method from javafx.scene.paint.Color)  
    "session": session id  
    "name": username  
    "text": text content  
    "type": User-drawn type (line, circle, triangle, rectangle, and text)  
    "lineWidth": line width  
  }],  
  "status": ‘0’ means no error occurred; ‘-1’ means error occurred.  
}
```

### Sockets
- The java.net package provides support for two common network protocols, TCP and UDP. The difference between TCP and UDP is that TCP provides reliable data transfer services while UDP provides unreliable transfers. At the same time, since UDP needs to be considered reliable at the logical level (packet loss and disorder, etc.), using TCP in this project will also greatly reduce my workload.
- In addition, due to the user's frequent operations during the drawing process, I chose to use long connections in this project to reduce the frequency of connections and save time. However, to keep the server running properly I added an anti-idle mechanism that automatically ends the connection when a client does not send any requests for a minute (e.g., bad network conditions, client program crashes, etc.) to save server resources, and I also created a thread on the client that sends heartbeat packets to the server at regular intervals for normal user use.

### Multithreading
#### Server-side:
- (ServiceHandler.java) In this project, the server will create a new thread for each client. When the user does not send any request to the server for a long time(1 min), this thread will die.
#### Client-side:
- (LongConnection.java) When the user logs in, a SendTask thread will be created to send the request, and it will automatically die after the sending is completed. Similarly, this method is also used when the user sends the drawn information to the server.
- (GeometryRetrieveTask.java) Each client will automatically create a 'GeometryRetrieveTask' after successful login, and periodically(1s) obtain the latest data from the server.


## Interaction diagram

![Interaction diagram](https://user-images.githubusercontent.com/68240769/173244739-7b203a6e-f62b-42af-be4c-7e8b98e1283d.svg)

