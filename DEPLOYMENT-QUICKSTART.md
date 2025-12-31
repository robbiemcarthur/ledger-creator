# Quick Start - Deploy to Oracle Cloud (E2.1.Micro + Neon)

This is a condensed version of DEPLOYMENT.md for the **VM.Standard.E2.1.Micro** (1GB RAM) setup.

## Prerequisites
- Oracle Cloud account
- Neon account (free PostgreSQL)
- GitHub account

---

## 1. Set Up Neon Database (5 minutes)

1. Go to https://neon.tech → Sign up
2. Create a project: `creator-ledger`
3. Region: **Europe (Frankfurt)**
4. Copy the connection string:
   ```
   postgresql://user:pass@ep-xxx.eu-central-1.aws.neon.tech/neondb?sslmode=require
   ```

---

## 2. Create Oracle Cloud VM (10 minutes)

1. Oracle Cloud Console → **Compute** → **Instances** → **Create**
2. Shape: **VM.Standard.E2.1.Micro** (1GB RAM)
3. Image: **Ubuntu 24.04 Minimal**
4. Download SSH key as `oracle-cloud-key.pem`
5. Note the **Public IP**

**Configure Firewall:**
- Security List → Add Ingress Rules
- Ports: 22, 80, 443, 8080 (TCP, source: 0.0.0.0/0)

---

## 3. Set Up VM (15 minutes)

```bash
# Connect
chmod 400 oracle-cloud-key.pem
ssh -i oracle-cloud-key.pem ubuntu@YOUR_PUBLIC_IP

# Install Docker
curl -fsSL https://get.docker.com | sudo sh
sudo usermod -aG docker ubuntu
exit

# Reconnect
ssh -i oracle-cloud-key.pem ubuntu@YOUR_PUBLIC_IP

# Configure firewall
sudo ufw allow 22/tcp
sudo ufw allow 80/tcp
sudo ufw allow 443/tcp
sudo ufw allow 8080/tcp
sudo ufw enable

# Set up app directory
sudo mkdir -p /opt/creator-ledger
sudo chown ubuntu:ubuntu /opt/creator-ledger
cd /opt/creator-ledger

# Create .env file
nano .env
```

**Paste this (update with your values):**
```env
SPRING_DATASOURCE_URL=postgresql://user:pass@ep-xxx.eu-central-1.aws.neon.tech/neondb?sslmode=require
SPRING_DATASOURCE_USERNAME=your_neon_username
SPRING_DATASOURCE_PASSWORD=your_neon_password
SPRING_PROFILES_ACTIVE=prod
APP_PORT=8080
GITHUB_REPOSITORY=your-username/creator-ledger
IMAGE_TAG=latest
```

**Create docker-compose.external-db.yml:**
```bash
nano docker-compose.external-db.yml
```

Paste from repository, then save (Ctrl+X, Y, Enter).

---

## 4. Configure GitHub (5 minutes)

**Repository Settings → Secrets → Actions → New:**

1. `ORACLE_CLOUD_HOST` = Your VM's public IP
2. `ORACLE_CLOUD_USER` = `ubuntu`
3. `ORACLE_CLOUD_SSH_KEY` = Contents of `oracle-cloud-key.pem`

---

## 5. Deploy (2 minutes)

```bash
# On your local machine
git add .
git commit -m "feat: add Oracle Cloud deployment"
git push origin master
```

Watch GitHub Actions → CI → Deploy workflows complete.

---

## 6. Verify

Visit: `http://YOUR_PUBLIC_IP:8080/actuator/health`

Should show: `{"status":"UP"}`

---

## Optional: Add Nginx Reverse Proxy

```bash
sudo apt install nginx -y
sudo nano /etc/nginx/sites-available/creator-ledger
```

**Paste:**
```nginx
server {
    listen 80;
    server_name YOUR_IP_OR_DOMAIN;

    location / {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```

**Enable:**
```bash
sudo ln -s /etc/nginx/sites-available/creator-ledger /etc/nginx/sites-enabled/
sudo nginx -t
sudo systemctl restart nginx
```

Now access at: `http://YOUR_PUBLIC_IP` (port 80)

---

## Troubleshooting

**App won't start:**
```bash
cd /opt/creator-ledger
docker compose -f docker-compose.external-db.yml logs -f app
```

**Database connection fails:**
- Check Neon connection string in `.env`
- Ensure `?sslmode=require` is present
- Test: `nc -zv your-neon-host 5432`

**Out of memory:**
```bash
docker stats
# If high, reduce memory limit in docker-compose.external-db.yml
```

---

## Total Cost: $0/month Forever

- Oracle Cloud E2.1.Micro: Free
- Neon PostgreSQL: Free (0.5GB)
- GitHub Actions + Registry: Free

---

## Want More Power?

Try different Oracle Cloud regions for **VM.Standard.A1.Flex**:
- Frankfurt, Amsterdam, Milan, Marseille
- 4 OCPUs, 24GB RAM possible
- Also free, but often out of capacity

See DEPLOYMENT.md for detailed instructions.
