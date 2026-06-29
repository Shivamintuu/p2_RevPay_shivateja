# 🚀 RevPay Deployment Guide (Render)

This guide walks you through the process of making your **RevPay** project live on **Render** so that anyone can access it online.

---

## 📋 Prerequisites
1. A **GitHub** account (free).
2. A **Render** account (free) — sign up at [render.com](https://render.com).
3. **Git** installed on your local machine.

---

## 🛠️ Step 1: Push Your Code to GitHub

Since Render deploys directly from a Git repository, you need to push your local project to GitHub.

1. **Open your terminal / PowerShell** in the project directory (`c:\Users\ADMIN\Downloads\Revpay\Revpay1`).
2. **Initialize Git** (if not already initialized):
   ```bash
   git init
   ```
3. **Add all files** to the staging area:
   ```bash
   git add .
   ```
4. **Commit your changes**:
   ```bash
   git commit -m "Configure Dockerfile and render.yaml for Render deployment"
   ```
5. **Create a new repository on GitHub**:
   - Go to [github.com/new](https://github.com/new).
   - Name your repository (e.g., `revpay`).
   - Keep it **Public** (or Private, Render supports both).
   - Leave "Add a README", "Add .gitignore", and "Choose a license" **unchecked** (since your project already has them).
   - Click **Create repository**.
6. **Link and push your local repository** to GitHub:
   - Copy the commands shown on GitHub under "...or push an existing repository from the command line":
     ```bash
     git branch -M main
     git remote add origin https://github.com/YOUR_GITHUB_USERNAME/YOUR_REPOSITORY_NAME.git
     git push -u origin main
     ```
     *(Replace `YOUR_GITHUB_USERNAME` and `YOUR_REPOSITORY_NAME` with your actual GitHub username and repository name).*

---

## 🚀 Step 2: Deploy to Render

Render will automatically read the `render.yaml` file we created and set up the Web Service for you.

1. Log in to your **[Render Dashboard](https://dashboard.render.com/)**.
2. Click the **New +** button in the top right corner and select **Blueprint**.
3. **Connect your GitHub account** (if you haven't already) and select your `revpay` repository.
4. Render will automatically detect the `render.yaml` file.
5. Provide a **Group Name** (e.g., `revpay-group`).
6. Click **Apply**.

Render will now:
- Pull your code from GitHub.
- Build the optimized multi-stage Docker container.
- Spin up the service on a free instance.
- Expose it on a public URL (e.g., `https://revpay-xxxx.onrender.com`).

---

## 🔒 Step 3: Managing Credentials & Environment Variables (Optional)

Currently, the application uses default/hardcoded values in `application.properties` (like the Oracle FreeSQL Database and Gmail SMTP credentials). 

To secure these or change them without modifying your code:
1. Go to your **Render Dashboard**.
2. Click on your **revpay** Web Service.
3. Go to the **Environment** tab on the left menu.
4. Click **Add Environment Variable** to override any of the following:

| Spring Property | Environment Variable Name | Example Value |
| :--- | :--- | :--- |
| `spring.datasource.url` | `SPRING_DATASOURCE_URL` | `jdbc:oracle:thin:@your-db-host:1521/service` |
| `spring.datasource.username` | `SPRING_DATASOURCE_USERNAME` | `YOUR_DB_USER` |
| `spring.datasource.password` | `SPRING_DATASOURCE_PASSWORD` | `YOUR_DB_PASSWORD` |
| `spring.mail.username` | `SPRING_MAIL_USERNAME` | `your-email@gmail.com` |
| `spring.mail.password` | `SPRING_MAIL_PASSWORD` | `your-gmail-app-password` |
| `razorpay.key.id` | `RAZORPAY_KEY_ID` | `rzp_test_xxxxxx` |
| `razorpay.key.secret` | `RAZORPAY_KEY_SECRET` | `xxxxxx` |

5. Click **Save Changes**. Render will automatically redeploy your application with the new secure variables.

---

## 🌐 Step 4: Verify Your Live App

Once the build is complete (the status changes to **Live** in the Render Dashboard):
1. Click the **public URL** provided at the top of your Render service page.
2. Access the **UI**: `https://your-app-name.onrender.com/`
3. Access the **Swagger API Docs**: `https://your-app-name.onrender.com/swagger-ui/index.html`

> [!NOTE]
> Render's free tier services spin down after 15 minutes of inactivity. If you access the site after a period of inactivity, it may take 30–50 seconds to "wake up" and load. This is normal behavior for the free tier.
