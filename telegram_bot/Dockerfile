# Use the official Python image from the Docker Hub
FROM python:3.12-slim

# Set the working directory in the container
WORKDIR /usr/src/app

# Copy the requirements file to the container
COPY requirements.txt ./

# Install the required Python packages
RUN pip install --no-cache-dir -r requirements.txt

# Install procps for health checks (pgrep)
RUN apt-get update && apt-get install -y procps && rm -rf /var/lib/apt/lists/*

# Copy the rest of the application code to the container
COPY main.py .

# Command to run the bot
CMD ["python", "./main.py"]
