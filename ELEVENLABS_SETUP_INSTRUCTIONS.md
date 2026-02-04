# ElevenLabs Configuration Instructions for Bulgarian Caller ID

This guide walks you through configuring ElevenLabs to use your Bulgarian phone number (`+359892226252`) as the caller ID for outbound calls.

## Prerequisites

- An active ElevenLabs account with Conversational AI access
- Your Bulgarian phone number `+359892226252` already verified in Twilio as a Caller ID
- Twilio account connected to ElevenLabs
- Access to your ElevenLabs API key (`ELEVEN_LABS_ENCRYPTED_KEY`)

---

## Part 1: Configure Phone Number in ElevenLabs Dashboard

### Step 1: Access ElevenLabs Dashboard

1. Log in to your ElevenLabs account at [https://elevenlabs.io](https://elevenlabs.io)
2. Navigate to **Conversational AI** → **Phone Numbers** (or **Agents Platform** → **Phone Numbers** depending on your account type)

### Step 2: Connect Twilio Account (if not already connected)

1. In the Phone Numbers section, look for **"Connect Twilio"** or **"Add Integration"**
2. Click to connect your Twilio account
3. You'll need:
   - Your Twilio Account SID
   - Your Twilio Auth Token
   - These should already be configured since you're using `TWILIO_ENCRYPTED_KEY`

### Step 3: Add/Configure Bulgarian Phone Number

Since you mentioned using call forwarding/shadow numbers, you have two options:

#### Option A: Use Verified Caller ID (Recommended for your use case)

1. In the ElevenLabs Phone Numbers section, click **"Add Phone Number"** or **"Import from Twilio"**
2. Select **"Verified Caller ID"** option
3. Choose your verified Bulgarian number: `+359892226252`
4. If the number doesn't appear:
   - Ensure it's verified in your Twilio account under **Phone Numbers** → **Verified Caller IDs**
   - You may need to verify it in ElevenLabs as well
5. Complete the setup wizard

#### Option B: Use Purchased Twilio Number with Call Forwarding

1. If you have a Twilio-purchased number that forwards to `+359892226252`:
   - Add that Twilio number to ElevenLabs
   - Configure forwarding in Twilio dashboard separately
2. This number will be used as the caller ID

### Step 4: Note the Phone Number ID

1. After adding the phone number, you'll see it listed in your ElevenLabs dashboard
2. **Copy the `agent_phone_number_id`** - it will look like: `phnum_01jxz13wtzfpjs8fanpkf9ynsc`
3. **Save this value** - you'll need it for the environment variable `ELEVENLABS_AGENT_PHONE_ID`

**Where to find it:**
- In the phone number list, click on your Bulgarian number
- The ID may be shown in:
  - The URL: `.../phone-numbers/phnum_xxx...`
  - The phone number details page
  - The API response if you're using the API to list numbers

---

## Part 2: Find Your Agent ID

### Step 1: Access Your Conversational AI Agent

1. In ElevenLabs dashboard, go to **Conversational AI** → **Agents** (or **Agents Platform** → **Agents`)
2. Find the agent you're using for outbound calls
3. If you don't have one yet, create a new agent:
   - Click **"Create Agent"**
   - Configure the agent settings (voice, behavior, etc.)
   - Save the agent

### Step 2: Get the Agent ID

1. Click on your agent to open its details
2. **Copy the Agent ID** - it will look like: `agent_01jxy9g3rbfsjsev7faxxd6vdv`
3. **Save this value** - you'll need it for the environment variable `ELEVENLABS_AGENT_ID`

**Where to find it:**
- In the agent details page
- In the URL: `.../agents/agent_xxx...`
- In the API response if listing agents

### Step 3: Associate Phone Number with Agent (if needed)

1. In your agent's settings, go to **Phone Numbers** or **Integrations** section
2. Ensure your Bulgarian phone number (`+359892226252` or the `agent_phone_number_id`) is associated with this agent
3. This ensures calls made with this agent use the correct caller ID

---

## Part 3: Set Environment Variables

### Step 1: Update Your `.env` File (Local Development)

1. Open or create a `.env` file in your project root directory (same level as `docker-compose.yaml`)
2. Add or update these variables:

```bash
# Existing variables (keep these)
AUTHORIZATION_TOKEN=your_authorization_token
POSTGRES_USER=your_postgres_user
POSTGRES_PASSWORD=your_postgres_password
POSTGRES_DB=your_database_name
TWILIO_ENCRYPTED_KEY=your_twilio_encrypted_key
ELEVEN_LABS_ENCRYPTED_KEY=your_elevenlabs_api_key

# NEW: Add these ElevenLabs configuration values
ELEVENLABS_AGENT_ID=agent_01jxy9g3rbfsjsev7faxxd6vdv
ELEVENLABS_AGENT_PHONE_ID=phnum_01jxz13wtzfpjs8fanpkf9ynsc
```

**Replace the placeholder values:**
- `ELEVENLABS_AGENT_ID` = The agent ID you copied from Step 2 of Part 2
- `ELEVENLABS_AGENT_PHONE_ID` = The phone number ID you copied from Step 4 of Part 1

### Step 2: Update Production/Staging Environment Variables

Depending on your deployment method:

#### If using Docker Compose:

1. Your `docker-compose.yaml` is already configured to read these variables
2. Ensure your `.env` file (or environment) has the values set
3. Restart your containers:
   ```bash
   docker-compose down
   docker-compose up -d
   ```

#### If using a cloud platform (AWS, Azure, GCP, Heroku, etc.):

1. Navigate to your platform's environment variables/configuration section
2. Add these two new variables:
   - `ELEVENLABS_AGENT_ID` = `agent_xxx...`
   - `ELEVENLABS_AGENT_PHONE_ID` = `phnum_xxx...`
3. Save and restart your application

#### If using Kubernetes:

1. Update your ConfigMap or Secret:
   ```yaml
   apiVersion: v1
   kind: ConfigMap
   metadata:
     name: app-config
   data:
     ELEVENLABS_AGENT_ID: "agent_xxx..."
     ELEVENLABS_AGENT_PHONE_ID: "phnum_xxx..."
   ```
2. Or update your deployment's environment variables section
3. Apply changes: `kubectl apply -f your-config.yaml`

### Step 3: Verify Environment Variables Are Loaded

1. Restart your Spring Boot application
2. Check application logs for any configuration errors
3. If you see errors like `Could not resolve placeholder 'elevenlabs.agent.id'`, the environment variables are not being read correctly

---

## Part 4: Testing

### Step 1: Verify Configuration

1. Make a test outbound call using your existing endpoint:
   ```bash
   POST http://localhost:8080/eleven-labs/call
   ```
   (or your production URL)

2. Check the request payload in your application logs - you should see:
   ```json
   {
     "agent_id": "agent_xxx...",
     "agent_phone_number_id": "phnum_xxx...",
     "to_number": "+359xxxxxxxxx"
   }
   ```

### Step 2: Test Call

1. Place a test call to a Bulgarian phone number you control
2. **Answer the call** and verify:
   - The caller ID displayed on your phone shows: `+359892226252`
   - The AI conversation works as expected

### Step 3: Troubleshooting

**If caller ID doesn't show correctly:**
- Verify `ELEVENLABS_AGENT_PHONE_ID` matches the phone number ID in ElevenLabs dashboard
- Check that the phone number is properly verified in Twilio
- Ensure the phone number is associated with your agent in ElevenLabs
- Check ElevenLabs API logs/dashboard for any errors

**If calls fail:**
- Verify `ELEVENLABS_AGENT_ID` is correct
- Check that `ELEVEN_LABS_ENCRYPTED_KEY` is valid
- Ensure `TWILIO_ENCRYPTED_KEY` is properly configured
- Review application logs for detailed error messages

---

## Quick Reference: Values You Need

Before starting, make sure you have:

1. ✅ **Agent ID**: `agent_xxx...` (from ElevenLabs Agents section)
2. ✅ **Phone Number ID**: `phnum_xxx...` (from ElevenLabs Phone Numbers section)
3. ✅ **ElevenLabs API Key**: Already configured as `ELEVEN_LABS_ENCRYPTED_KEY`
4. ✅ **Twilio Encrypted Key**: Already configured as `TWILIO_ENCRYPTED_KEY`

---

## Additional Resources

- [ElevenLabs Twilio Integration Docs](https://elevenlabs.io/docs/conversational-ai/phone-numbers/twilio-integration)
- [ElevenLabs Outbound Call API Reference](https://elevenlabs.io/docs/conversational-ai/api-reference/twilio/outbound-call)
- [Twilio Verified Caller IDs](https://www.twilio.com/docs/voice/api/outgoing-caller-ids)

---

## Summary Checklist

- [ ] Connected Twilio account to ElevenLabs
- [ ] Added/configured Bulgarian phone number (`+359892226252`) in ElevenLabs
- [ ] Copied `agent_phone_number_id` (starts with `phnum_`)
- [ ] Found or created your Conversational AI agent
- [ ] Copied `agent_id` (starts with `agent_`)
- [ ] Associated phone number with agent
- [ ] Updated `.env` file with `ELEVENLABS_AGENT_ID` and `ELEVENLABS_AGENT_PHONE_ID`
- [ ] Updated production environment variables (if applicable)
- [ ] Restarted application
- [ ] Tested outbound call and verified caller ID shows `+359892226252`
