# Deployment Guide - Oracle Cloud Always Free Tier

This guide walks you through deploying CreatorLedger to Oracle Cloud's Always Free tier.

## Prerequisites

- Oracle Cloud account (create at https://cloud.oracle.com/free)
- GitHub account with this repository
- Basic familiarity with SSH and command line

---

## Part 1: Set Up Oracle Cloud VM

### 1.1 Create an Oracle Cloud Account

1. Go to https://cloud.oracle.com/free
2. Sign up for an account (requires credit card verification, but won't charge)
3. Complete the identity verification process
4. Wait for account approval (can take 1-2 hours)

### 1.2 Create a Compute Instance (VM)

1. Log in to Oracle Cloud Console
2. Navigate to **Compute** → **Instances**
3. Click **Create Instance**

**Configuration:**
- **Name**: `creator-ledger-app`
- **Image**: Ubuntu 24.04 Minimal
- **Shape**:
  - Click "Change Shape"
  - Select **VM.Standard.E2.1.Micro** (Always Free)
  - Specs: 1 OCPU, 1GB RAM, 47GB storage
  - **Note**: If you see VM.Standard.A1.Flex (Arm) available, prefer that instead (4 OCPUs, 24GB RAM possible). A1.Flex is often out of capacity in London but worth trying other regions.
- **Networking**:
  - Use default VCN and subnet
  - **Assign a public IPv4 address**: ✅ Yes
- **SSH Keys**:
  - Generate a new key pair
  - Download both private and public keys
  - Save the private key as `oracle-cloud-key.pem` (you'll need this later)

4. Click **Create**
5. Wait for the instance to provision (2-3 minutes)
6. Note the **Public IP Address** - you'll need this

**Important**: With E2.1.Micro (1GB RAM), we'll use an external database service (Neon) instead of running PostgreSQL on the VM. See Part 1A below.

### 1.3 Configure Firewall Rules

Oracle Cloud blocks most ports by default. Open the required ports:

1. In your instance details, click on the **Subnet** link
2. Click on the **Default Security List**
3. Click **Add Ingress Rules**

**Rule 1: HTTP**
- Source CIDR: `0.0.0.0/0`
- IP Protocol: `TCP`
- Destination Port Range: `80`
- Description: `HTTP`

**Rule 2: HTTPS**
- Source CIDR: `0.0.0.0/0`
- IP Protocol: `TCP`
- Destination Port Range: `443`
- Description: `HTTPS`

**Rule 3: Application** (temporary - you'll use a reverse proxy later)
- Source CIDR: `0.0.0.0/0`
- IP Protocol: `TCP`
- Destination Port Range: `8080`
- Description: `Spring Boot App`

4. Click **Add Ingress Rules**

---

## Part 1A: Set Up External PostgreSQL Database (Neon)

Since the E2.1.Micro has only 1GB RAM, we'll use a free external PostgreSQL database instead of running it on the VM.

### 1A.1 Create a Neon Account

1. Go to https://neon.tech
2. Sign up with GitHub (easiest) or email
3. Verify your email if needed

### 1A.2 Create a Database

1. Click **Create a project**
2. **Project name**: `creator-ledger`
3. **Region**: Select **Europe (Frankfurt)** or closest to London
4. **PostgreSQL version**: 16 (latest)
5. Click **Create project**

### 1A.3 Get Connection Details

After creation, you'll see the connection details:

1. Select the **Connection string** tab
2. Choose **Parameters only** view
3. Copy these values:
   - **Host**: e.g., `ep-xxx-xxx.eu-central-1.aws.neon.tech`
   - **Database**: `neondb` (default)
   - **User**: your username
   - **Password**: click **Show** to reveal

4. Construct your connection URL:
```
postgresql://username:password@ep-xxx-xxx.eu-central-1.aws.neon.tech/neondb?sslmode=require
```

**Save this connection URL** - you'll need it in Part 2.

### Alternative: Supabase

If you prefer Supabase:
1. Go to https://supabase.com
2. Create a new project
3. Get the connection string from **Project Settings** → **Database**
4. Use the **Connection pooling** → **Transaction mode** URL for better performance

---

## Part 2: Set Up the VM

### 2.1 Connect to Your VM

```bash
# Set correct permissions on your private key
chmod 400 oracle-cloud-key.pem

# Connect via SSH (replace <PUBLIC_IP> with your instance's IP)
ssh -i oracle-cloud-key.pem ubuntu@<PUBLIC_IP>
```

### 2.2 Install Docker and Docker Compose

```bash
# Update system
sudo apt update && sudo apt upgrade -y

# Install Docker
curl -fsSL https://get.docker.com -o get-docker.sh
sudo sh get-docker.sh

# Add ubuntu user to docker group
sudo usermod -aG docker ubuntu

# Log out and back in for group changes to take effect
exit
```

Reconnect to SSH, then verify:

```bash
docker --version
docker compose version
```

### 2.3 Configure Ubuntu Firewall

Oracle Cloud instances have both cloud-level and OS-level firewalls:

```bash
# Allow Docker and application ports
sudo ufw allow 22/tcp    # SSH
sudo ufw allow 80/tcp    # HTTP
sudo ufw allow 443/tcp   # HTTPS
sudo ufw allow 8080/tcp  # Spring Boot (temporary)

# Enable firewall
sudo ufw enable
```

### 2.4 Set Up Application Directory

```bash
# Create application directory
sudo mkdir -p /opt/creator-ledger
sudo chown ubuntu:ubuntu /opt/creator-ledger
cd /opt/creator-ledger

# Create environment file
nano .env
```

**Paste this into `.env`** (update with your Neon connection details from Part 1A):

```env
# External database connection (from Neon)
SPRING_DATASOURCE_URL=postgresql://username:password@ep-xxx.eu-central-1.aws.neon.tech/neondb?sslmode=require
SPRING_DATASOURCE_USERNAME=your_neon_username
SPRING_DATASOURCE_PASSWORD=your_neon_password

SPRING_PROFILES_ACTIVE=prod
APP_PORT=8080

GITHUB_REPOSITORY=YOUR_GITHUB_USERNAME/creator-ledger
IMAGE_TAG=latest
```

Save and exit (Ctrl+X, Y, Enter)

**Create docker-compose file:**

```bash
nano docker-compose.external-db.yml
```

Paste the `docker-compose.external-db.yml` content from this repository:

```yaml
version: '3.8'

services:
  app:
    image: ghcr.io/${GITHUB_REPOSITORY:-robbiemcarthur/creator-ledger}:${IMAGE_TAG:-latest}
    container_name: creator-ledger-app
    restart: unless-stopped
    environment:
      SPRING_PROFILES_ACTIVE: ${SPRING_PROFILES_ACTIVE:-prod}
      SPRING_DATASOURCE_URL: ${SPRING_DATASOURCE_URL:?Database URL required}
      SPRING_DATASOURCE_USERNAME: ${SPRING_DATASOURCE_USERNAME:?Database username required}
      SPRING_DATASOURCE_PASSWORD: ${SPRING_DATASOURCE_PASSWORD:?Database password required}
    ports:
      - "${APP_PORT:-8080}:8080"
    healthcheck:
      test: ["CMD", "wget", "--no-verbose", "--tries=1", "--spider", "http://localhost:8080/actuator/health"]
      interval: 30s
      timeout: 3s
      retries: 3
      start_period: 90s
    deploy:
      resources:
        limits:
          memory: 768M
        reservations:
          memory: 512M
```

Save and exit (Ctrl+X, Y, Enter)

---

## Part 3: Configure GitHub Secrets

GitHub Actions needs access to your Oracle Cloud VM to deploy. Set up these secrets:

1. Go to your GitHub repository
2. Click **Settings** → **Secrets and variables** → **Actions**
3. Click **New repository secret** for each:

### Required Secrets

**`ORACLE_CLOUD_HOST`**
- Value: Your instance's public IP address (e.g., `132.145.123.45`)

**`ORACLE_CLOUD_USER`**
- Value: `ubuntu`

**`ORACLE_CLOUD_SSH_KEY`**
- Value: Contents of your `oracle-cloud-key.pem` file
- To get this: `cat oracle-cloud-key.pem` on your local machine
- Copy the entire output including `-----BEGIN ... KEY-----` and `-----END ... KEY-----`

### Enable GitHub Container Registry

Your Docker images will be stored in GitHub Container Registry (free):

1. Go to your GitHub profile → **Settings** → **Developer settings**
2. Click **Personal access tokens** → **Tokens (classic)**
3. Click **Generate new token (classic)**
4. Name: `ghcr-access`
5. Expiration: Choose your preference
6. Scopes: Select `write:packages` and `read:packages`
7. Click **Generate token**
8. Copy the token - you'll need it once

The deployment workflow uses `GITHUB_TOKEN` automatically, so you don't need to add this token as a secret.

---

## Part 4: Enable Container Registry Access

Make your container images accessible:

1. Push your first commit to trigger the CI/CD
2. After the workflow runs, go to your GitHub repository
3. Click **Packages** (right sidebar)
4. Click on `creator-ledger`
5. Click **Package settings** (right sidebar)
6. Scroll to **Danger Zone** → **Change visibility**
7. Select **Public** (for easier pulling without auth)

---

## Part 5: Deploy!

### 5.1 Trigger First Deployment

```bash
# On your local machine
git add .
git commit -m "feat: add Oracle Cloud deployment configuration"
git push origin master
```

### 5.2 Monitor Deployment

1. Go to GitHub **Actions** tab
2. Watch the **CI** workflow complete
3. Then watch the **Deploy to Oracle Cloud** workflow
4. If successful, the app is now running!

### 5.3 Verify Deployment

```bash
# On your VM
cd /opt/creator-ledger
docker compose -f docker-compose.external-db.yml ps

# Check logs
docker compose -f docker-compose.external-db.yml logs -f app
```

Visit `http://<YOUR_PUBLIC_IP>:8080/actuator/health` - you should see:
```json
{"status":"UP"}
```

The app should be using around 600-700MB of RAM, leaving enough headroom for the OS on your 1GB instance.

---

## Part 6: Production Hardening (Optional but Recommended)

### 6.1 Set Up Nginx Reverse Proxy

```bash
# Install Nginx
sudo apt install nginx -y

# Create Nginx configuration
sudo nano /etc/nginx/sites-available/creator-ledger
```

Paste this configuration:

```nginx
server {
    listen 80;
    server_name YOUR_DOMAIN_OR_IP;

    location / {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }

    location /actuator/health {
        proxy_pass http://localhost:8080/actuator/health;
        access_log off;
    }
}
```

Enable the site:

```bash
sudo ln -s /etc/nginx/sites-available/creator-ledger /etc/nginx/sites-enabled/
sudo nginx -t
sudo systemctl restart nginx
```

Now access your app at `http://<YOUR_PUBLIC_IP>` (port 80).

### 6.2 Set Up SSL with Let's Encrypt (if you have a domain)

```bash
sudo apt install certbot python3-certbot-nginx -y
sudo certbot --nginx -d yourdomain.com
```

---

## Troubleshooting

### Deployment fails with "Connection refused"

Check your SSH key is correctly added to GitHub Secrets:
```bash
# On your local machine
cat oracle-cloud-key.pem
```

### Health check fails

```bash
# On the VM
docker compose -f docker-compose.external-db.yml logs app
docker compose -f docker-compose.external-db.yml exec app wget --spider http://localhost:8080/actuator/health
```

### Can't access the application

1. Verify Oracle Cloud Security List has port 8080 open
2. Verify Ubuntu firewall: `sudo ufw status`
3. Check app logs: `docker compose -f docker-compose.external-db.yml logs app`

### Database connection fails

1. Verify your Neon connection string is correct in `.env`
2. Check Neon dashboard - database might be paused (auto-resumes on connection)
3. Ensure `?sslmode=require` is in your connection URL
4. Test connection from VM: `nc -zv your-neon-host 5432`

### Out of memory on E2.1.Micro

The JVM is configured to use max 75% of 768MB (container limit). If you see OOM errors:

1. Check actual memory usage: `docker stats`
2. Reduce container memory limit in `docker-compose.external-db.yml` to 512M
3. Restart: `docker compose -f docker-compose.external-db.yml restart`

### Want more resources? Try getting A1.Flex

The Arm-based A1.Flex instances are much more powerful but often out of capacity:

**Method 1: Try different regions**
```bash
# Check these regions for availability:
# - Frankfurt (eu-frankfurt-1)
# - Amsterdam (eu-amsterdam-1)
# - Milan (eu-milan-1)
# - Marseille (eu-marseille-1)
```

**Method 2: Script to auto-retry**
Some users have success with scripts that repeatedly try to provision A1.Flex instances when capacity becomes available. Search "oracle cloud a1 flex script" for community tools.

**Method 3: Contact Oracle Support**
Free tier users can request A1.Flex access via support ticket, though success varies.

---

## Cost Estimate

**Oracle Cloud Always Free Tier:**
- ✅ E2.1.Micro Compute: FREE (1 OCPU, 1GB RAM)
- ✅ Block Storage: FREE (up to 200GB total)
- ✅ Outbound Data Transfer: FREE (10TB/month)
- ✅ **Total: $0/month forever**

**Neon PostgreSQL:**
- ✅ Free tier: 0.5GB storage per project, 3GB total
- ✅ Auto-suspend after inactivity
- ✅ **Total: $0/month**

**GitHub:**
- ✅ Actions: 2,000 minutes/month free (private repos)
- ✅ Container Registry: 500MB storage free
- ✅ **Total: $0/month**

**Grand Total: $0/month** 🎉

**If you get A1.Flex instead:**
- Up to 4 OCPUs and 24GB RAM (can run PostgreSQL locally)
- Could skip Neon and use `docker-compose.yml` instead
- Still $0/month forever

---

## Next Steps

1. Set up automated backups for PostgreSQL data
2. Configure log aggregation and monitoring
3. Set up a domain name and SSL certificate
4. Implement application monitoring (Spring Boot Admin)
5. Configure Spring Boot production profiles in `application-prod.yml`

---

## Support

If you encounter issues:
1. Check GitHub Actions logs
2. Check VM logs: `docker compose logs`
3. Verify all secrets are correctly set in GitHub
4. Ensure firewall rules are configured

Happy deploying! 🚀
