# 💰 BudgetAI – AI-Powered Personal Finance Management System

BudgetAI is a full-stack personal finance management application designed to help users track their income, manage expenses, analyze spending patterns, and make smarter financial decisions. It provides an interactive dashboard, secure authentication, financial reports, and an AI-powered chatbot to deliver personalized insights into users' financial activities.

## 🚀 Features

- **User Authentication & Authorization:** Secure login and registration using Spring Security and JWT-based authentication.
- **Income Management:** Add, update, delete, and track income transactions.
- **Expense Management:** Record and manage daily expenses with detailed transaction information.
- **Category Management:** Organize income and expenses into different categories.
- **Interactive Dashboard:** Visualize total income, expenses, balance, and financial summaries.
- **AI-Powered Chatbot:** Ask questions about expenses, income, budgeting, and spending patterns to receive intelligent financial insights.
- **Advanced Filtering:** Filter transactions based on categories and other available criteria.
- **Excel Export:** Export financial transaction records for offline analysis and record keeping.
- **Profile Management:** Manage personal account information.
- **Email Notifications:** Receive relevant email notifications.
- **Secure Data Access:** Users can access only their own financial information.

## 🛠️ Tech Stack

### Frontend
- React.js
- Tailwind CSS
- Axios
- React Hooks

### Backend
- Java
- Spring Boot
- Spring Security
- Spring Data JPA
- JWT Authentication
- RESTful APIs
- Maven

### Database
- PostgreSQL

### Tools & Technologies
- Git & GitHub
- Postman
- IntelliJ IDEA
- VS Code

## 🏗️ System Architecture

BudgetAI follows a layered backend architecture to maintain separation of concerns, modularity, and maintainability.

```text
                 React.js Frontend
                        |
                        |
                   Axios (HTTP)
                        |
                        |
              Spring Boot Backend
                        |
          +-------------+-------------+
          |             |             |
      Controller     Service     Security
          |             |             |
          +-------------+-------------+
                        |
                   Repository
                        |
                  Spring Data JPA
                        |
                    PostgreSQL
```

### Backend Architecture

- **Controller Layer:** Handles incoming HTTP requests and returns API responses.
- **Service Layer:** Contains business logic and processes financial operations.
- **Repository Layer:** Communicates with the database using Spring Data JPA.
- **Entity Layer:** Defines database entities and relationships.
- **DTO Layer:** Transfers data between frontend and backend.
- **Security Layer:** Manages authentication and authorization using Spring Security and JWT.

## 📂 Project Structure

```text
BudgetAI/
│
├── frontend/
│   ├── public/
│   └── src/
│       ├── components/
│       ├── pages/
│       ├── services/
│       ├── hooks/
│       └── App.jsx
│
├── backend/
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       │   └── ...
│   │       └── resources/
│   │           └── application.properties
│   │
│   ├── pom.xml
│   └── mvnw
│
└── README.md
```

*Note: Adjust the directory structure according to your actual repository.*

## ⚙️ Installation & Setup

### 1. Clone the Repository

```bash
git clone https://github.com/anuj-31/BudgetAI.git
cd BudgetAI
```

### 2. Backend Setup

Navigate to the backend directory:

```bash
cd backend
```

Configure your PostgreSQL database and application properties.

Example configuration:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/budgetai
spring.datasource.username=YOUR_DB_USERNAME
spring.datasource.password=YOUR_DB_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

Configure the required JWT secret and other application-specific environment variables.

Run the Spring Boot application:

```bash
./mvnw spring-boot:run
```

On Windows:

```bash
mvnw.cmd spring-boot:run
```

### 3. Frontend Setup

Navigate to the frontend directory:

```bash
cd frontend
```

Install dependencies:

```bash
npm install
```

Start the development server:

```bash
npm run dev
```

The frontend will be available at the local development URL displayed in your terminal.

## 🔐 Authentication Flow

1. Users register or log in through the React frontend.
2. The backend validates the submitted credentials.
3. Spring Security authenticates the user.
4. A JWT token is generated after successful authentication.
5. The frontend uses the token for subsequent protected API requests.
6. Backend security validates the token and ensures users can access only their own financial records.

## 🤖 AI-Powered Financial Assistant

BudgetAI integrates an AI chatbot that helps users understand their financial activities.

Users can ask questions such as:

- How much did I spend this month?
- What are my highest spending categories?
- How much income did I receive?
- Give me an analysis of my spending habits.
- How can I reduce my monthly expenses?

The chatbot provides insights based on financial information to help users make informed budgeting decisions.

## 📊 Key Highlights

- Developed a full-stack personal finance management system using React.js and Spring Boot.
- Implemented secure authentication and authorization using Spring Security and JWT.
- Designed RESTful APIs following a layered Controller-Service-Repository architecture.
- Integrated PostgreSQL for persistent financial data management.
- Implemented an AI-powered chatbot for financial queries and spending analysis.
- Developed dashboard analytics, transaction filtering, and Excel export functionality.

## 🔮 Future Enhancements

- Monthly and yearly budget planning.
- Spending prediction using historical transaction data.
- Automated expense categorization.
- Financial goal tracking.
- Enhanced data visualization and personalized recommendations.

## 👨‍💻 Developer

**Anuj Kumar**

B.Tech – Metallurgical and Materials Engineering  
National Institute of Technology, Jamshedpur

- GitHub: https://github.com/anuj-31
- Project Repository: https://github.com/anuj-31/BudgetAI

---

⭐ If you find this project useful, consider giving it a star!
