# 🔐 NotesVault – Secure Notes Management System

<p align="center">

![Java](https://img.shields.io/badge/Java-17-orange?style=for-the-badge\&logo=java)
![Spring Boot](https://img.shields.io/badge/SpringBoot-Backend-green?style=for-the-badge\&logo=springboot)
![React](https://img.shields.io/badge/React-Frontend-blue?style=for-the-badge\&logo=react)
![MySQL](https://img.shields.io/badge/MySQL-Database-blue?style=for-the-badge\&logo=mysql)
![JWT](https://img.shields.io/badge/JWT-SecureAuth-black?style=for-the-badge\&logo=jsonwebtokens)
![Vite](https://img.shields.io/badge/Vite-DevServer-purple?style=for-the-badge\&logo=vite)

</p>

---

## 🚀 About the Project

NotesVault is a **full-stack secure notes management application** built using **React.js and Spring Boot**, designed to provide a seamless and secure way to manage personal notes.

It implements **JWT-based authentication**, ensuring that each user’s data is protected and isolated.

---

## ✨ Features

* 🔐 Secure Authentication using **JWT (JSON Web Tokens)**
* 📝 Full **CRUD Operations** for notes
* 🔍 Search and filter functionality
* 👤 User-specific data isolation
* ⚡ Real-time UI updates
* 🌐 RESTful API architecture
* 🔗 Seamless frontend-backend integration

---

## 🛠️ Tech Stack

### 💻 Frontend

* ⚛️ React.js
* 📜 TypeScript / JavaScript
* 🔗 Axios
* 🎨 CSS

### ⚙️ Backend

* ☕ Java
* 🌱 Spring Boot
* 🔐 JWT Authentication
* 🔄 REST APIs

### 🗄️ Database

* 🐬 MySQL

### 🔧 Tools

* 🧰 Git & GitHub
* 📮 Postman
* 💻 VS Code

---

## 📂 Project Structure

```bash
notesvault/
│
├── backend/       # Spring Boot Backend
├── frontend/      # React Frontend
└── README.md
```

---

## ⚡ Getting Started

### 1️⃣ Clone the Repository

```bash
git clone https://github.com/ThirumuruganR02/notesvault.git
cd notesvault
```

---

## ▶️ Run Backend

```bash
cd backend
mvn spring-boot:run
```

Backend runs at:
👉 http://localhost:8080

---

## ▶️ Run Frontend

```bash
cd frontend
npm install
npm run dev
```

Frontend runs at:
👉 http://localhost:5173

---

## 🔐 Authentication Flow

```text
User → Login → Receive JWT → Store Token → Send with API Requests
```

Example header:

```bash
Authorization: Bearer <your-token>
```

---

## 📡 API Endpoints

### 🔑 Auth

| Method | Endpoint           | Description       |
| ------ | ------------------ | ----------------- |
| POST   | /api/auth/register | Register new user |
| POST   | /api/auth/login    | Login and get JWT |

---

### 📝 Notes

| Method | Endpoint    | Description   |
| ------ | ----------- | ------------- |
| GET    | /notes      | Get all notes |
| POST   | /notes      | Create note   |
| PUT    | /notes/{id} | Update note   |
| DELETE | /notes/{id} | Delete note   |

---

## 🧪 API Testing

* Swagger UI → http://localhost:8080/swagger-ui/index.html
* Postman

---

## 🔥 Key Highlights

* 🔐 Secure authentication using JWT
* ⚡ High-performance REST APIs
* 🔄 Full-stack integration
* 🧠 Clean and scalable architecture
* 📈 Interview-ready project

---

## 🌟 Future Enhancements

* 📄 Pagination support
* 🏷️ Tag management system
* 🔒 End-to-end encryption
* ☁️ Cloud deployment (AWS / Render)
* 🤖 AI-based note summarization

---

## 👨‍💻 Author

**Thirumurugan R**

* 📧 [thirumurugan.ramse@gmail.com](mailto:thirumurugan.ramse@gmail.com)
* 🔗 GitHub: https://github.com/ThirumuruganR02

---

## ⭐ Show your support

If you like this project:

* ⭐ Star the repo
* 🍴 Fork it
* 📢 Share it

---

<p align="center">
🚀 Built with passion for learning and development
</p>
