# 📁 File Storage Microservice (Spring Boot + AWS S3 + SSO)

---

# 🚀 Overview

This service provides secure file handling using **Spring Boot** and **Amazon S3**, with authentication handled via AWS **SSO (Single Sign-On)**.

### ✅ Features

* Upload file(s)
* Download / view file
* Generate pre-signed URLs for private access
* Store file metadata in DB
* User-specific secure access (no public exposure)

---

# 🏗️ Tech Stack

* Java 17+
* Spring Boot
* AWS SDK v2
* Amazon S3
* AWS SSO
* MySQL / PostgreSQL

---

# 🎯 Purpose of This Service

This service is designed to:

* Store files securely in S3
* Prevent direct public access
* Allow only authenticated users to access files
* Avoid storing sensitive AWS credentials in code/config

---

# 🔐 Why SSO Instead of Access Keys?

## ❌ Traditional Approach (Access Keys)

Using:

```text
AWS_ACCESS_KEY_ID
AWS_SECRET_ACCESS_KEY
```

### Problems:

* Keys can leak (GitHub, logs, configs)
* No automatic expiry
* Manual rotation required
* No proper user-level tracking
* High security risk in production

---

## ✅ SSO-Based Approach (Recommended)

### Benefits:

* 🔐 No credentials stored in code
* ⏳ Temporary credentials (auto-expire)
* 👤 Identity-based access via IAM roles
* 🔄 Automatic credential refresh
* 🧾 Full audit trail (CloudTrail)
* 🔒 Least privilege access control

---

## ⚖️ Comparison

| Feature          | Access Keys ❌ | SSO / IAM Role ✅ |
| ---------------- | ------------- | ---------------- |
| Stored in code   | Yes           | No               |
| Expiry           | No            | Yes              |
| Rotation         | Manual        | Automatic        |
| Security         | Low           | High             |
| Audit            | Weak          | Strong           |
| Production Ready | Risky         | Recommended      |

---

# ⚙️ AWS Setup (SSO)

## 1. Configure SSO

```bash
aws configure sso
```

Example config:

```ini
[profile Girish_aws]
sso_session = Girish_aws
sso_account_id = XXXXX
sso_role_name = XXXXX
region = ap-south-1
output = json

[sso-session Girish_aws]
sso_start_url = https://your-sso-url
sso_region = ap-south-1
sso_registration_scopes = sso:account:access
```

---

## 2. Login

```bash
aws sso login --profile Girish_aws
```

---

## 3. Set Profile

### Windows (CMD)

```bash
set AWS_PROFILE=Girish_aws
```

### IntelliJ

```
AWS_PROFILE=Girish_aws
```

---

# 📦 Maven Dependencies

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>software.amazon.awssdk</groupId>
            <artifactId>bom</artifactId>
            <version>2.25.0</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>

<dependencies>
    <!-- S3 -->
    <dependency>
        <groupId>software.amazon.awssdk</groupId>
        <artifactId>s3</artifactId>
    </dependency>

    <!-- SSO Support -->
    <dependency>
        <groupId>software.amazon.awssdk</groupId>
        <artifactId>sso</artifactId>
    </dependency>

    <dependency>
        <groupId>software.amazon.awssdk</groupId>
        <artifactId>ssooidc</artifactId>
    </dependency>
</dependencies>
```

---

# ⚙️ Configuration

## S3 Client Config

```java
@Bean
public S3Client s3Client() {
    return S3Client.builder()
            .region(Region.AP_SOUTH_1)
            .build(); // auto uses AWS_PROFILE
}
```

---

# 🗂️ Database Schema

```sql
CREATE TABLE files (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    file_name VARCHAR(255),
    s3_key VARCHAR(500),
    content_type VARCHAR(100),
    size BIGINT,
    user_id BIGINT,
    uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

---

# 📡 API Endpoints

## 1. Upload File

```
POST /api/files/upload
```

**Body (form-data):**

* file → File
* userId → Text

---

## 2. Download File

```
GET /api/files/download/{fileId}
```

---

## 3. Get Pre-Signed URL

```
GET /api/files/url/{fileId}
```

---

## 4. List Files

```
GET /api/files?userId=101
```

---

# 🔐 Security Model

* Files stored as **private in S3**
* Access via:

    * Pre-signed URLs (temporary)
    * User-based filtering
* No public bucket access

---

# 🧠 Key Concepts

## Why store `s3_key`?

* Unique identifier of file in S3
* Used for:

    * Download
    * Delete
    * URL generation

---

## Private File Access

Files remain private but accessible using:

```java
s3Presigner.presignGetObject(...)
```

👉 Generates secure temporary URL

---

# 🔄 Credential Flow

```
aws sso login
        ↓
Token stored (~/.aws/sso/cache)
        ↓
Spring Boot starts
        ↓
AWS SDK reads AWS_PROFILE
        ↓
Credentials resolved
        ↓
S3 operations work
```

---

# ❗ Common Errors & Fixes

## 1. Access Denied

* Check IAM role permissions
* Verify bucket policy

---

## 2. File not present

* Use `form-data` in Postman
* Set file type correctly

---

## 3. SSO not working

✔ Fix:

* Upgrade AWS SDK (≥ 2.25)
* Add `sso` + `ssooidc` dependencies

---

## 4. Profile not found

```bash
set AWS_PROFILE=Girish_aws
```

---

## 5. Old SDK issue

❌ 2.8.x not supported
✔ Use latest AWS SDK

---

# 🧪 Run Project

```bash
mvn clean install
mvn spring-boot:run
```

---

# 📌 Final Notes

* No access keys stored → secure
* Uses AWS SSO → enterprise-grade
* Scalable with S3
* DB stores only metadata

---

# 🚀 Future Enhancements

* File sharing with expiry
* Folder structure
* File versioning
* Encryption (KMS)
* Role-based access (RBAC)

---

# ✅ Status

✔ Production-ready with SSO-based authentication
