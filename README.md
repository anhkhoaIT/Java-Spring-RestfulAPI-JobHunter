# JobHunter - Backend

JobHunter là nền tảng tuyển dụng được xây dựng với **Java Spring Boot**, tập trung vào hiệu suất, bảo mật và khả năng mở rộng.  
Dự án triển khai hệ thống xác thực, phân quyền, quản lý API, gửi email và thao tác cơ sở dữ liệu.

## 🚀 Tech Stack

**Backend:**

- Java Spring
- Spring Boot – Cấu hình và khởi chạy dự án nhanh chóng.
- Spring Security + JWT – Xác thực (Authentication) và phân quyền người dùng (Authorization) với JSON Web Token.
- Spring JPA – Xử lý và thao tác dữ liệu với cơ sở dữ liệu quan hệ.

**Frontend:**

- React + Vite (TypeScript) – Được cung cấp sẵn trong khoá học.

**Build Tool:**

- Gradle (Kotlin DSL)

**Database:**

- MySQL (MySQL Workbench)

**Khác:**

- Dependency Injection – Viết code tách biệt, dễ bảo trì.
- Debugging với Spring.
- Quản lý API với Swagger.
- Gửi email theo template.

## ✨ Features

- Đăng ký, đăng nhập, xác thực người dùng bằng JWT.
- Phân quyền truy cập theo vai trò.
- CRUD dữ liệu tuyển dụng (ứng viên, công việc, nhà tuyển dụng).
- API Documentation với Swagger UI.
- Hệ thống gửi email với template HTML.
- Tích hợp MySQL để lưu trữ dữ liệu.

## ⚙️ Setup & Run

1. **Clone repo**
   ```bash
   git clone https://github.com/anhkhoaIT/Java-Spring-RestfulAPI-JobHunter.git
   cd jobhunter-backend
   ```
