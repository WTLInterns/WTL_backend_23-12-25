# WTL Backend Deployment Guide

## 🔒 Security Notice
This backend uses environment variables for all sensitive configurations including Firebase service account credentials. No sensitive data is committed to the repository.

## 📋 Prerequisites
- Java 17 or higher
- Maven 3.6+
- MySQL database
- Firebase project with service account

## 🚀 Deployment Steps

### 1. Clone Repository
```bash
git clone https://github.com/WTLInterns/WTL_Backend_11-08-25-final.git
cd WTL_Backend_11-08-25-final
```

### 2. Create Environment File
```bash
cp .env.example .env
```

### 3. Configure Environment Variables
Edit `.env` file with your actual values:

```bash
# Database
DB_URL=jdbc:mysql://your-host:3306/your-database
DB_USERNAME=your_username
DB_PASSWORD=your_password

# Firebase (Base64 encoded JSON)
FIREBASE_SERVICE_ACCOUNT_BASE64=your_base64_encoded_firebase_json

# Other configurations...
```

### 4. Generate Firebase Base64
To convert your Firebase service account JSON to base64:

**Linux/Mac:**
```bash
cat serviceAccountKey.json | base64 -w 0
```

**Windows PowerShell:**
```powershell
[Convert]::ToBase64String([IO.File]::ReadAllBytes("serviceAccountKey.json"))
```

### 5. Build and Deploy
```bash
chmod +x deploy.sh
./deploy.sh
```

Or manually:
```bash
mvn clean package -DskipTests
java -jar target/asu-0.0.1-SNAPSHOT.jar
```

## 🔧 Configuration Details

### Firebase Configuration
- Firebase credentials loaded from `FIREBASE_SERVICE_ACCOUNT_BASE64` environment variable
- Falls back to classpath resource if environment variable not set (development only)
- JSON file should never be committed to repository

### Database Configuration
- Uses environment variables for database connection
- Supports MySQL with configurable host, port, username, password

## 🛡️ Security Features

1. **Environment Variables**: All sensitive data in environment variables
2. **Git Ignore**: Sensitive files are ignored by git
3. **Base64 Encoding**: Firebase credentials are base64 encoded
4. **Fallback Support**: Development fallback for local testing

## 🚨 Important Notes

1. **Never commit .env file** - It contains sensitive information
2. **Firebase JSON file removed** - Use base64 environment variable instead
3. **Update your VPS** - Make sure .env file is present on deployment server
4. **Rotate credentials** - If any credentials were exposed, rotate them immediately
