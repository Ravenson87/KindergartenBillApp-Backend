# KindergartenBillApp BACKEND🧾

KindergartenBillApp backend is a Spring Boot application designed to manage kindergarten billing. 
It allows administrators to record, update, and track monthly bills for children, 
providing a reliable system for managing payments and generating reports.

✨ Main features include:
- Creating and updating child accounts
- Recording monthly bills and payments
- Searching bills by child, date, or category
- Generating PDF invoices with iText 7
- Sending invoices to users via email
- Database migrations with Flyway

🛠️ Built with:
- Java / Spring Boot
- MariaDB as the database
- Flyway for schema migrations
- iText 7 for PDF generation
- Spring Mail for sending emails
- Git & GitHub for version control

🚀 How to run the application:
```bash
cd KindergartenBillAppBack
mvn spring-boot:run
```
📂 Project Structure
```
KindergartenBillAppBack/
├── src/
│   ├── main/
│   │   ├── java/com/kindergarten/   # Application source code
│   │   └── resources/               # Configuration files (application.properties, db/migration)
│   └── test/                        # Unit and integration tests
├── pom.xml                          # Maven dependencies and build configuration
└── README.md                        # Documentation for backend
```
👤 Author

- Siniša Gavrić

- GitHub: github.com/Ravenson87