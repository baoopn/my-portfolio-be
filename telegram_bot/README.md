# Telegram Message Bot

This is a simple Telegram bot that responds to commands. It can provide the chat ID of the user who sends a message.

## Setup

1. **Clone the repository:**
   ```bash
   git clone https://github.com/baoopn/telegram-msg-bot.git
   cd telegram-msg-bot
   ```

2. **Create a virtual environment and activate it:**
   ```bash
   python -m venv venv
   source venv/bin/activate  # On Windows use `venv\Scripts\activate`
   ```

3. **Install the required packages:**
   ```bash
   pip install -r requirements.txt
   ```

4. **Create a `.env` file and add your Telegram bot token:**
   ```plaintext
   TELEGRAM_BOT_TOKEN=your-telegram-bot-token
   ```

## Usage

1. **Run the bot:**
   ```bash
   python main.py
   ```

2. **Interact with the bot on Telegram:**
   - Use `/start` to receive a welcome message.
   - Use `/chatid` to get your chat ID.

## Deployment with Docker

### Using Dockerfile

1. **Build the Docker image:**
   ```bash
   docker build -t telegram-msg-bot .
   ```

2. **Run the Docker container:**
   ```bash
   docker run --env-file .env --name telegram-bot-container -d telegram-msg-bot
   ```

### Using docker-compose.yml

1. **Deploy the application:**
   ```bash
   docker-compose up -d --build
   ```

2. **Stop the application:**
   ```bash
   docker-compose down
   ```

## License

This project is licensed under the MIT License.