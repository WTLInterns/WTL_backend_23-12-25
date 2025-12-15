#!/bin/bash

# WTL Backend Deployment Script
# This script helps deploy the backend securely on VPS

echo "🚀 WTL Backend Deployment Script"
echo "================================="

# Check if .env file exists
if [ ! -f ".env" ]; then
    echo "❌ Error: .env file not found!"
    echo "💡 Please create .env file with your environment variables"
    echo "   Copy .env.example and fill in your values"
    exit 1
fi

# Load environment variables
echo "📋 Loading environment variables..."
export $(cat .env | grep -v '^#' | xargs)

# Check if Firebase credentials are set
if [ -z "$FIREBASE_SERVICE_ACCOUNT_BASE64" ]; then
    echo "❌ Error: FIREBASE_SERVICE_ACCOUNT_BASE64 not set in .env file"
    echo "💡 Please add your base64 encoded Firebase service account to .env"
    exit 1
fi

# Clean and build
echo "🧹 Cleaning previous build..."
mvn clean

echo "🔨 Building application..."
mvn package -DskipTests

# Check if build was successful
if [ $? -eq 0 ]; then
    echo "✅ Build successful!"
    echo "📦 JAR file created: target/asu-0.0.1-SNAPSHOT.jar"
    echo ""
    echo "🚀 To run the application:"
    echo "   java -jar target/asu-0.0.1-SNAPSHOT.jar"
    echo ""
    echo "🔒 Security Notes:"
    echo "   - Firebase credentials are loaded from environment variables"
    echo "   - serviceAccountKey.json is ignored by git"
    echo "   - Make sure .env file is present on your VPS"
else
    echo "❌ Build failed!"
    exit 1
fi
