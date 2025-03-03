import os
from dotenv import load_dotenv
from telegram import Update
from telegram.ext import ApplicationBuilder, CommandHandler, ContextTypes
import nest_asyncio
import asyncio

# Load environment variables from .env file
load_dotenv()
TOKEN = os.getenv('TELEGRAM_BOT_TOKEN')

# Define the '/start' command handler
async def start(update: Update, context: ContextTypes.DEFAULT_TYPE) -> None:
    welcome_message = (
        'Welcome! This chat bot is built to interact with the API at msg.baoopn.com\n\n'
        'Use the following commands:\n'
        ' - /start - Show welcome message\n'
        ' - /info - Show information about this chat bot\n'
        ' - /chatid - Get your chat ID\n'
        ' - /dev - Show developer information\n'
        ' - /commands - List all available commands\n\n'
        'For instructions on how to use the API with this chat bot, visit: msg.baoopn.com'
    )
    await update.message.reply_text(welcome_message)

# Define the '/chatid' command handler
async def chatid(update: Update, context: ContextTypes.DEFAULT_TYPE) -> None:
    chat_id = update.message.chat_id
    await update.message.reply_text(f'Your chat ID is: `{chat_id}`', parse_mode="MarkdownV2")

# Define the '/dev' command handler
async def dev(update: Update, context: ContextTypes.DEFAULT_TYPE) -> None:
    developer_info = (
        '*Developer Information*\n\n'
        'Name: Bao Nguyen\n'
        'Website: [baoopn\\.com](https://baoopn.com)\n'
    )
    await update.message.reply_text(developer_info, parse_mode="MarkdownV2")

# Define the '/commands' command handler
async def commands(update: Update, context: ContextTypes.DEFAULT_TYPE) -> None:
    commands_list = (
        'Available commands:\n'
        '/start - Show welcome message\n'
        '/info - Show information about this chat bot\n'
        '/chatid - Get your chat ID\n'
        '/dev - Show developer information\n'
        '/commands - List all available commands\n'
    )
    await update.message.reply_text(commands_list)

async def info(update: Update, context: ContextTypes.DEFAULT_TYPE) -> None:
    info_message = (
        'This chat bot is built to interact with the API at msg.baoopn.com\n'
        'An example of using this API and this chat bot is in a contact form on a website. When a user submits the form, the details are sent to the API, and you get notified in your Telegram chat.\n\n'
        'For instructions on how to use the API with this chat bot, visit: msg.baoopn.com'
    )
    await update.message.reply_text(info_message)

# Main function to set up the bot
async def main() -> None:
    application = ApplicationBuilder().token(TOKEN).build()

    # Add command handlers
    application.add_handler(CommandHandler("start", start))
    application.add_handler(CommandHandler("info", info))
    application.add_handler(CommandHandler("chatid", chatid))
    application.add_handler(CommandHandler("dev", dev))
    application.add_handler(CommandHandler("commands", commands))

    # Start the bot
    await application.run_polling()

if __name__ == '__main__':
    nest_asyncio.apply()
    asyncio.run(main())