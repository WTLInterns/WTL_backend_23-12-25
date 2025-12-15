# WTL Live Location Tracking System

## Overview

The WTL Live Location Tracking System provides real-time location tracking between drivers and users during cab bookings. The system includes proximity detection, OTP verification, trip management, and comprehensive REST APIs.

## Features

### 🚗 Real-time Location Tracking
- Live location updates for both drivers and users
- Distance calculation and ETA estimation
- Automatic proximity detection (50m threshold)

### 🔐 OTP Verification System
- 6-digit OTP generation for trip verification
- Start trip OTP verification
- End trip OTP verification
- Secure OTP storage and validation

### 📱 Trip Management
- Trip start with odometer reading
- Trip end with final odometer reading
- Trip status tracking
- Automatic booking status updates

### 🌐 WebSocket Communication
- Real-time bidirectional communication
- Booking-specific channels
- Automatic reconnection handling

### 📊 REST APIs
- Location update endpoints
- Trip status queries
- Distance calculation
- Proximity checking

## System Architecture

### Components

1. **WebSocketConfig.java** - WebSocket configuration
2. **LiveTrackingController.java** - WebSocket message handling
3. **LocationTrackingController.java** - REST API endpoints
4. **LocationService.java** - Location management and proximity detection
5. **WebTripService.java** - Trip and OTP management
6. **LocationMessage.java** - Location data transfer object
7. **TripStatusMessage.java** - Trip status data transfer object

### Database Entities

- **CarRentalUser** - User location storage
- **DriveAdmin** - Admin driver location storage
- **VendorDrivers** - Vendor driver location storage
- **Booking** - Trip and OTP information

## API Endpoints

### WebSocket Endpoints

#### Connection
```
WebSocket URL: /ws-trip-tracking
```

#### Message Mappings
- `/app/driver-location` - Update driver location
- `/app/user-location` - Update user location
- `/app/send-otp` - Send/request OTP
- `/app/verify-otp` - Verify OTP
- `/app/start-trip` - Start trip
- `/app/end-trip` - End trip

#### Subscription Topics
- `/topic/booking/{bookingId}/driver-location` - Driver location updates
- `/topic/booking/{bookingId}/user-location` - User location updates
- `/topic/booking/{bookingId}/user-notifications` - User notifications
- `/topic/booking/{bookingId}/driver-notifications` - Driver notifications

### REST API Endpoints

#### Location Management
```
POST /api/location/driver/update
POST /api/location/user/update
GET /api/location/driver/{driverId}/booking/{bookingId}
GET /api/location/user/{userId}/booking/{bookingId}
```

#### Trip Management
```
GET /api/location/trip/status/{bookingId}
GET /api/location/booking/{bookingId}
```

#### Utility Endpoints
```
POST /api/location/calculate-distance
POST /api/location/check-proximity
```

## Usage Instructions

### 1. Starting the Application

```bash
# Navigate to the backend directory
cd WTL_BACKEND_07_08_25/WTL_BACKEND_07_08_25

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

### 2. Testing with the Demo Interface

1. Open your browser and navigate to:
   ```
   http://localhost:8080/live-tracking-example.html
   ```

2. The demo interface provides two panels:
   - **User Panel**: Simulates user location tracking
   - **Driver Panel**: Simulates driver location tracking

### 3. WebSocket Connection Steps

#### For Users:
1. Enter User ID and Booking ID
2. Click "Connect WebSocket"
3. Click "Start Location Tracking"
4. Monitor driver location updates

#### For Drivers:
1. Enter Driver ID and Booking ID
2. Click "Connect WebSocket"
3. Click "Start Location Tracking"
4. Use trip control buttons as needed

### 4. Trip Flow

1. **Driver Assignment**: Admin assigns booking to driver
2. **Location Tracking**: Both user and driver locations are tracked
3. **Proximity Detection**: When driver is within 50m, proximity alert is triggered
4. **OTP Generation**: Driver requests OTP for trip verification
5. **OTP Verification**: User receives OTP and driver verifies it
6. **Trip Start**: Driver enters start odometer and starts trip
7. **Trip End**: Driver enters end odometer and verifies final OTP
8. **Trip Completion**: Trip status is updated to completed

## API Examples

### Update Driver Location
```javascript
// WebSocket
const locationMessage = {
    bookingId: "WTL1234567890",
    driverId: 1,
    latitude: 19.0760,
    longitude: 72.8777,
    userType: "DRIVER",
    timestamp: new Date().toISOString()
};

stompClient.send('/app/driver-location', {}, JSON.stringify(locationMessage));

// REST API
fetch('/api/location/driver/update', {
    method: 'POST',
    headers: {
        'Content-Type': 'application/json'
    },
    body: JSON.stringify(locationMessage)
});
```

### Send OTP
```javascript
const otpMessage = {
    bookingId: "WTL1234567890",
    action: "SEND_OTP",
    driverId: 1,
    userId: 1
};

stompClient.send('/app/send-otp', {}, JSON.stringify(otpMessage));
```

### Start Trip
```javascript
const startTripMessage = {
    bookingId: "WTL1234567890",
    action: "START_TRIP",
    startOdometer: 12345.6,
    destination: "Destination Address",
    destinationLatitude: 19.0760,
    destinationLongitude: 72.8777,
    driverId: 1,
    userId: 1
};

stompClient.send('/app/start-trip', {}, JSON.stringify(startTripMessage));
```

## Configuration

### WebSocket Configuration
```java
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic", "/queue");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-trip-tracking")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }
}
```

### Proximity Threshold
The proximity detection threshold is set to 50 meters (0.05 km) in the `LocationService.java`:
```java
if (distance <= 0.05) { // 50 meters = 0.05 km
    checkAndTriggerProximityAlert(locationMessage);
}
```

## Database Schema

### Location Fields
- **CarRentalUser**: `userlatitude`, `userlongitude`
- **DriveAdmin**: `driverLatitude`, `driverLongitude`
- **VendorDrivers**: `driverLatitude`, `driverLongitude`

### Trip Fields
- **Booking**: `startOtp`, `endOtp`, `startOdometer`, `endOdometer`, `tripStatus`

## Security Considerations

1. **OTP Security**: OTPs are stored in memory and automatically cleaned up
2. **Location Privacy**: Location data is only shared between connected parties
3. **WebSocket Security**: Implement authentication for production use
4. **Input Validation**: All inputs are validated before processing

## Error Handling

The system includes comprehensive error handling:
- WebSocket connection failures
- Invalid OTP attempts
- Missing location data
- Database connection issues

## Monitoring and Logging

- Real-time logs in the demo interface
- WebSocket connection status indicators
- Trip status tracking
- Location update frequency monitoring

## Production Deployment

### Requirements
- Java 17+
- Spring Boot 3.x
- MySQL/PostgreSQL database
- WebSocket-enabled load balancer

### Environment Variables
```properties
# Database
spring.datasource.url=jdbc:mysql://localhost:3306/wtl_db
spring.datasource.username=your_username
spring.datasource.password=your_password

# WebSocket
spring.websocket.max-text-message-size=8192
spring.websocket.max-binary-message-size=8192
```

### Load Balancer Configuration
For production deployment with multiple instances, configure your load balancer to support WebSocket sticky sessions.

## Troubleshooting

### Common Issues

1. **WebSocket Connection Failed**
   - Check if the application is running
   - Verify the WebSocket endpoint URL
   - Check browser console for errors

2. **Location Not Updating**
   - Verify WebSocket connection status
   - Check if location tracking is started
   - Monitor browser console logs

3. **OTP Not Working**
   - Ensure both user and driver are connected
   - Check if booking ID matches
   - Verify OTP format (6 digits)

4. **Proximity Not Detected**
   - Check distance calculation
   - Verify location coordinates
   - Monitor proximity threshold settings

### Debug Mode
Enable debug logging by adding to `application.properties`:
```properties
logging.level.com.workshop.CarRental=DEBUG
logging.level.org.springframework.web.socket=DEBUG
```

## Support

For technical support or questions about the live location tracking system, please refer to the code comments or contact the development team.

## License

This system is part of the WTL (World Trip Link) project and is proprietary software.
