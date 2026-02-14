# 🛡️ Threat Intelligence Platform

A comprehensive, professional-grade cybersecurity threat intelligence platform built with React, Spring Boot, and MySQL. This platform provides real-time threat monitoring, analysis, and management capabilities for security teams.

## ✨ Features

### 🎯 Core Functionality
- **Real-time Threat Monitoring** - Live dashboard with threat statistics
- **Advanced Search & Filtering** - Search by title, description, source, severity, category
- **Threat Management** - Create, read, update, delete threats
- **Severity Classification** - Critical, High, Medium, Low threat levels
- **Category Management** - Malware, Phishing, Ransomware, DDoS, Vulnerabilities, etc.
- **Status Tracking** - Active, Investigating, Mitigated, Resolved statuses
- **Source Attribution** - Track intelligence sources (CISA, FBI, CERT, etc.)

### 🎨 Professional UI/UX
- **Modern Dark Theme** - Professional cybersecurity aesthetic
- **Responsive Design** - Works on desktop, tablet, and mobile
- **Interactive Dashboard** - Real-time statistics and charts
- **Advanced Search** - Powerful filtering and sorting capabilities
- **Status Indicators** - Visual threat status and severity indicators
- **Smooth Animations** - Professional transitions and effects

### 🚀 Technical Features
- **RESTful API** - Complete CRUD operations
- **Database Integration** - MySQL with H2 fallback
- **Docker Support** - Containerized deployment
- **CI/CD Pipeline** - GitHub Actions automation
- **AWS Deployment** - ECS Fargate and CloudFormation
- **Security Headers** - Comprehensive security implementation

## 🏗️ Architecture

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   React SPA     │    │  Spring Boot    │    │     MySQL       │
│   (Frontend)    │◄──►│   (Backend)     │◄──►│   (Database)    │
│   Port: 3000    │    │   Port: 8080    │    │   Port: 3306    │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

## 🚀 Quick Start

### Prerequisites
- Java 17+
- Node.js 18+
- MySQL 8.0+ (optional - H2 in-memory database is used by default)
- Docker & Docker Compose (for containerized deployment)

### Local Development

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd threat-platform
   ```

2. **Start the Backend**
   ```bash
   cd backend
   ./mvnw spring-boot:run
   ```

3. **Start the Frontend** (in a new terminal)
   ```bash
   cd frontend
   npm install
   npm run dev
   ```

4. **Access the Application**
   - Frontend: http://localhost:5173
   - Backend API: http://localhost:8080
   - Swagger UI: http://localhost:8080/swagger-ui.html
   - H2 Console: http://localhost:8080/h2-console

### Docker Deployment

1. **Build and run with Docker Compose**
   ```bash
   docker-compose up -d
   ```

2. **Access the Application**
   - Frontend: http://localhost:3000
   - Backend API: http://localhost:8080
   - MySQL: localhost:3306

## 📊 API Endpoints

### Threats
- `GET /api/threats` - List all threats
- `POST /api/threats` - Create a new threat
- `GET /api/threats/{id}` - Get threat by ID
- `PUT /api/threats/{id}` - Update threat
- `DELETE /api/threats/{id}` - Delete threat

### Sample Threat Object
```json
{
  "id": 1,
  "title": "Malware Campaign: Emotet Banking Trojan",
  "description": "A new variant of the Emotet banking trojan...",
  "severity": "HIGH",
  "category": "MALWARE",
  "status": "ACTIVE",
  "source": "CERT-EU",
  "createdAt": "2024-01-15T10:30:00Z",
  "updatedAt": "2024-01-15T10:30:00Z"
}
```

## 🎨 UI Components

### Dashboard
- **Statistics Cards** - Real-time threat counts by severity
- **Search & Filters** - Advanced search and filtering capabilities
- **Threat Table** - Comprehensive threat listing with actions
- **Add/Edit Form** - Modal form for threat management

### Features
- **Real-time Updates** - Live data refresh
- **Responsive Design** - Mobile-first approach
- **Dark Theme** - Professional cybersecurity aesthetic
- **Interactive Elements** - Hover effects and animations
- **Status Indicators** - Visual severity and status badges

## 🐳 Docker Configuration

### Services
- **mysql** - MySQL 8.0 database
- **backend** - Spring Boot application
- **frontend** - React application with Nginx

### Environment Variables
```yaml
# Database
MYSQL_ROOT_PASSWORD: secret
MYSQL_DATABASE: threatintel
MYSQL_USER: threatuser
MYSQL_PASSWORD: threatpass

# Backend
SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/threatintel
SPRING_DATASOURCE_USERNAME: threatuser
SPRING_DATASOURCE_PASSWORD: threatpass
```

## ☁️ AWS Deployment

### ECS Fargate
1. **Build and push images to ECR**
2. **Deploy using CloudFormation template**
3. **Configure Application Load Balancer**
4. **Set up auto-scaling**

### CloudFormation Stack
```bash
aws cloudformation create-stack \
  --stack-name threat-intel-platform \
  --template-body file://aws/cloudformation-template.yml \
  --parameters ParameterKey=VpcId,ParameterValue=vpc-12345 \
               ParameterKey=SubnetIds,ParameterValue=subnet-12345,subnet-67890
```

## 🔧 Configuration

### Backend Configuration
```properties
# Database (H2 for development)
spring.datasource.url=jdbc:h2:mem:threatintel
spring.datasource.username=sa
spring.datasource.password=

# Database (MySQL for production)
spring.datasource.url=jdbc:mysql://localhost:3306/threatintel
spring.datasource.username=root
spring.datasource.password=secret

# JPA Configuration
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
```

### Frontend Configuration
```javascript
// API Configuration
const BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080'
```

## 🧪 Testing

### Backend Tests
```bash
cd backend
./mvnw test
```

### Frontend Tests
```bash
cd frontend
npm test
```

### Integration Tests
```bash
docker-compose -f docker-compose.test.yml up --abort-on-container-exit
```

## 📈 Monitoring & Logging

### Health Checks
- Backend: `GET /actuator/health`
- Frontend: `GET /` (Nginx health check)

### Logging
- **Backend** - Spring Boot logging with Logback
- **Frontend** - Browser console logging
- **Docker** - Container logs via `docker-compose logs`

## 🔒 Security Features

### Backend Security
- CORS configuration
- Input validation
- SQL injection prevention
- XSS protection

### Frontend Security
- Content Security Policy
- XSS protection headers
- Secure API communication
- Input sanitization

## 🚀 CI/CD Pipeline

### GitHub Actions
- **Test** - Automated testing on PR
- **Build** - Docker image building
- **Deploy** - AWS ECS deployment
- **Security** - Vulnerability scanning

### Pipeline Stages
1. **Code Quality** - Linting and formatting
2. **Testing** - Unit and integration tests
3. **Building** - Docker image creation
4. **Deployment** - AWS ECS deployment
5. **Monitoring** - Health checks and alerts

## 📚 Documentation

### API Documentation
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI Spec: http://localhost:8080/v3/api-docs

### Database Schema
```sql
CREATE TABLE threats (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    severity VARCHAR(16) NOT NULL,
    source VARCHAR(50),
    category VARCHAR(20),
    status VARCHAR(20),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🆘 Support

For support and questions:
- Create an issue in the repository
- Check the documentation
- Review the API documentation

## 🎯 Roadmap

### Upcoming Features
- [ ] Real-time notifications
- [ ] Advanced analytics dashboard
- [ ] Threat intelligence feeds integration
- [ ] Machine learning threat detection
- [ ] Multi-tenant support
- [ ] Advanced reporting
- [ ] API rate limiting
- [ ] Authentication & authorization

---

**Built with ❤️ for the cybersecurity community**
