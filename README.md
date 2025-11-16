🏨 Hostelo - Hotel Management System
<div align="center">
![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-

![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo

![Angular](https://img.shields.io/badge/Angular-DD0031?style=for-the-badge://img.shields.io/badge/TypeScript-007ACC?style=for-the-badge&logo=typescript&logoColorio/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor-ready, full-stack hotel management solution with real-time booking, role-based access control, and modern UI/UX**

View Demo · Report Bug · Request Feature

</div>
📋 Table of Contents
Overview

Key Features

Screenshots

Architecture

Tech Stack

Getting Started

API Documentation

Roadmap

Contributing

License

🎯 Overview
Hostelo is an enterprise-grade hotel management system built to streamline daily operations for hotels of all sizes. The platform handles end-to-end workflows including guest reservations, room inventory tracking, service management, and administrative oversight with a secure, role-based authentication system.​

Why Hostelo?
Real-time Operations: Live updates on room availability and booking status​

Scalable Architecture: Layered design separating concerns (Controller → Service → Repository → DTO → Entity)​

Security First: JWT-based authentication with Spring Security​

Modern UI/UX: Responsive design using Angular 15 with PrimeNG and Carbon Design components​

✨ Key Features
🔐 Authentication & Authorization
JWT-based secure authentication with token refresh mechanism

Role-based access control (Admin, Receptionist, Guest)

Session management with automatic timeout

🛏️ Reservation Management
Multi-room booking capability for single guests

Real-time availability checking with conflict prevention

Booking modification and cancellation workflows

Automated checkout based on reservation expiry

👥 Client Management
Comprehensive customer profile system

CNIC, phone, and email validation during booking

Booking history tracking per customer

Guest preference storage

🏢 Room Management
Dynamic room inventory with real-time status updates

Room type categorization (Standard, Deluxe, Suite)

Maintenance mode flagging

Pricing and availability management

🍽️ Service Management
Additional service offerings (Dining, Spa, Laundry, etc.)

Service request tracking

Integration with billing system

📊 Admin Dashboard
Real-time analytics on occupancy rates

Revenue tracking and reporting

User activity monitoring

System health indicators

🖼️ Screenshots
<details open> <summary><b>Click to expand/collapse</b></summary>
Home Page
![Home Page](demoth modern UI showcasing hotel services and booking CTA*

Authentication
![Login Screen](demo/Login-page JWT authentication and role-based routing*

Reservation Dashboard
![Reservation Dashboard](demo/ booking management with filtering and search capabilities*

Room Management
![Room Management](demo/room for managing room inventory, pricing, and availability*

Client Management
![Client Management](demo/ database with search, edit, and booking history features*

Service Management
![Service Management](demo/services hotel services management panel*

Admin Control Panel
![Admin Panel](demo/admin-panel.pngh analytics and system controls*

Account Settings
![Account Edit](demo/account-editference settings*

</details>
🏗️ Architecture
System Design
text
┌─────────────────────────────────────────────────────────┐
│                     Client Layer                        │
│  (Angular 15 + TypeScript + Bootstrap + PrimeNG)       │
└────────────────┬────────────────────────────────────────┘
                 │ HTTP/REST
┌────────────────▼────────────────────────────────────────┐
│                  API Gateway Layer                      │
│           (Spring Boot REST Controllers)                │
└────────────────┬────────────────────────────────────────┘
                 │
┌────────────────▼────────────────────────────────────────┐
│                   Service Layer                         │
│        (Business Logic + Data Validation)               │
└────────────────┬────────────────────────────────────────┘
                 │
┌────────────────▼────────────────────────────────────────┐
│              Repository Layer (JPA)                     │
│             (Data Access Abstraction)                   │
└────────────────┬────────────────────────────────────────┘
                 │
┌────────────────▼────────────────────────────────────────┐
│                PostgreSQL Database                      │
│         (Persistent Data Storage)                       │
└─────────────────────────────────────────────────────────┘
Project Structure
text
hostelo/
│
├── backend/                     # Spring Boot application
│   ├── src/main/java/
│   │   ├── controller/          # REST API endpoints
│   │   ├── service/             # Business logic layer
│   │   ├── repository/          # Data access layer (JPA)
│   │   ├── model/               # Entity classes
│   │   ├── dto/                 # Data Transfer Objects
│   │   ├── config/              # Security & app configuration
│   │   └── exception/           # Custom exception handlers
│   ├── src/main/resources/
│   │   ├── application.properties
│   │   └── schema.sql           # Database initialization
│   └── pom.xml
│
├── frontend/                    # Angular application
│   ├── src/
│   │   ├── app/
│   │   │   ├── components/      # UI components
│   │   │   ├── services/        # API communication
│   │   │   ├── models/          # TypeScript interfaces
│   │   │   ├── guards/          # Route protection
│   │   │   └── interceptors/    # HTTP interceptors
│   │   ├── assets/              # Images, icons, fonts
│   │   └── environments/        # Environment configs
│   ├── angular.json
│   └── package.json
│
├── demo/                        # Screenshots and demo assets
└── README.md
💻 Tech Stack
Frontend
Technology	Purpose	Version
Angular	SPA Framework	15.x
TypeScript	Type-safe JavaScript	4.9+
Bootstrap 5	Responsive grid & utilities	5.3
PrimeNG	Enterprise UI components	15.x
Carbon Design	IBM design system	Latest
RxJS	Reactive programming	7.x
Backend
Technology	Purpose	Version
Spring Boot	Application framework	3.x
Spring Security	Authentication & authorization	6.x
Spring Data JPA	ORM abstraction	3.x
Hibernate	JPA implementation	6.x
JWT	Token-based auth	Latest
Maven	Build automation	3.9+
Database
Technology	Purpose
PostgreSQL	Primary relational database
DevOps & Tools
Technology	Purpose
Git/GitHub	Version control
Postman	API testing & documentation
Maven	Dependency management
npm	Frontend package management
🚀 Getting Started
Prerequisites
Ensure you have the following installed on your system:

bash
- Java JDK 17 or higher
- Node.js 16+ and npm 8+
- PostgreSQL 14+
- Maven 3.9+
- Git
Installation
1️⃣ Clone the Repository
bash
git clone https://github.com/yourusername/hostelo.git
cd hostelo
2️⃣ Database Setup
Create a PostgreSQL database:

sql
CREATE DATABASE hostelo_db;
CREATE USER hostelo_user WITH PASSWORD 'your_secure_password';
GRANT ALL PRIVILEGES ON DATABASE hostelo_db TO hostelo_user;
3️⃣ Backend Configuration
Navigate to backend directory and configure database connection:

bash
cd backend
Edit src/main/resources/application.properties:

text
# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/hostelo_db
spring.datasource.username=hostelo_user
spring.datasource.password=your_secure_password

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# JWT Configuration
jwt.secret=your_jwt_secret_key_here_min_256_bits
jwt.expiration=86400000

# Server Configuration
server.port=8080
Build and run the backend:

bash
mvn clean install
mvn spring-boot:run
Backend will start on http://localhost:8080

4️⃣ Frontend Setup
Open a new terminal and navigate to frontend directory:

bash
cd frontend
npm install
Configure API endpoint in src/environments/environment.ts:

typescript
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080/api'
};
Start the development server:

bash
ng serve
Frontend will start on http://localhost:4200

5️⃣ Access the Application
Frontend: http://localhost:4200

Backend API: http://localhost:8080/api

Default Admin Credentials (if seeded):

Username: admin@hostelo.com

Password: admin123

📡 API Documentation
Authentication Endpoints
Method	Endpoint	Description	Auth Required
POST	/api/auth/register	Register new user	❌
POST	/api/auth/login	User login	❌
POST	/api/auth/refresh	Refresh JWT token	✅
POST	/api/auth/logout	User logout	✅
Reservation Endpoints
Method	Endpoint	Description	Auth Required
GET	/api/reservations	Get all reservations	✅
GET	/api/reservations/{id}	Get reservation by ID	✅
POST	/api/reservations	Create new reservation	✅
PUT	/api/reservations/{id}	Update reservation	✅
DELETE	/api/reservations/{id}	Cancel reservation	✅
Room Endpoints
Method	Endpoint	Description	Auth Required
GET	/api/rooms	Get all rooms	❌
GET	/api/rooms/available	Get available rooms	❌
POST	/api/rooms	Add new room	✅ (Admin)
PUT	/api/rooms/{id}	Update room details	✅ (Admin)
DELETE	/api/rooms/{id}	Delete room	✅ (Admin)
📝 Note: For complete API documentation with request/response examples, import the Postman collection from /docs/Hostelo-API.postman_collection.json (create this file)​

🗺️ Roadmap
Current Version (v1.0)
✅ Core booking system

✅ User authentication & authorization

✅ Admin dashboard

✅ Room & service management

Upcoming Features (v1.1)
 Email notifications for bookings

 Payment gateway integration (Stripe/PayPal)

 Multi-language support (i18n)

 Mobile-responsive PWA

 Advanced analytics dashboard

Future Enhancements (v2.0)
 Housekeeping module

 POS integration for restaurant billing

 Mobile app (React Native/Flutter)

 AI-powered pricing optimization

 Integration with booking.com/Airbnb APIs

🤝 Contributing
Contributions make the open-source community an amazing place to learn, inspire, and create. Any contributions you make are greatly appreciated.​

How to Contribute
Fork the Project

Create your Feature Branch (git checkout -b feature/AmazingFeature)

Commit your Changes (git commit -m 'Add some AmazingFeature')

Push to the Branch (git push origin feature/AmazingFeature)

Open a Pull Request

Please read CONTRIBUTING.md for details on our code of conduct and development process.

📄 License
This project is licensed under the MIT License - see the LICENSE file for details.


🙏 Acknowledgments
Spring Boot Documentation

Angular Documentation

PrimeNG Components

Shields.io for README badges

<div align="center">
⭐ If you find this project helpful, please give it a star! ⭐

Made with ❤️ by [Your Name]

</div>
